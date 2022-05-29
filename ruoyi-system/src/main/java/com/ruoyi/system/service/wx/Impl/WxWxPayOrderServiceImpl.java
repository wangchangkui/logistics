package com.ruoyi.system.service.wx.Impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.config.PropertiesConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.common.utils.rabbit.Produce;
import com.ruoyi.common.utils.redis.RedisPool;
import com.ruoyi.system.domain.PayOrder;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.plus.mapper.wx.WxPayOrderMapper;
import com.ruoyi.system.plus.mapper.wx.UserMapper;
import com.ruoyi.system.service.wx.WxPayOrderService;
import com.ruoyi.system.utils.alipay.CommonAlipay;
import com.ruoyi.system.utils.alipay.PayStatus;
import com.ruoyi.system.utils.emu.PayEnum;
import com.ruoyi.system.utils.oos.OssUtil;
import com.ruoyi.system.utils.time.TimeUtil;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 16:16:00
 */
@Service
public class WxWxPayOrderServiceImpl extends ServiceImpl<WxPayOrderMapper, PayOrder> implements WxPayOrderService {

    private final Logger logger = LoggerFactory.getLogger(WxWxPayOrderServiceImpl.class);

    private Produce produce;

    private UserMapper userMapper;

    private WxPayOrderMapper payOrderMapper;

    private PropertiesConfig propertiesConfig;

    private RedisPool redisPool;

    private CommonAlipay commonAlipay;

    private OssUtil ossUtil;

    @Value("${filePath.osspath}")
    private String ossPath;

    public WxWxPayOrderServiceImpl(Produce produce) {
        this.produce = produce;
    }

    @Autowired
    public WxWxPayOrderServiceImpl(UserMapper userMapper, WxPayOrderMapper payOrderMapper, PropertiesConfig propertiesConfig, RedisPool redisPool, CommonAlipay commonAlipay, OssUtil ossUtil) {
        this.userMapper = userMapper;
        this.payOrderMapper = payOrderMapper;
        this.propertiesConfig = propertiesConfig;
        this.redisPool = redisPool;
        this.commonAlipay = commonAlipay;
        this.ossUtil = ossUtil;
    }

    @Override
    public ResponseResult<List<PayOrder>> getUserOrder(String userId) {
        return ResponseResult.success(list(new QueryWrapper<PayOrder>().eq("user_id",userId).eq("status",2).orderByDesc("create_time")));
    }

    @Override
    public ResponseResult<String> orderStatus(String orderId) {
        try {
            if (RuoYiConfig.checkPayMoney()){
                String message = commonAlipay.hasOverOrder(orderId);
                logger.info("\n\n\n支付结果： {}\n\n\n",message);
                return ResponseResult.success(message);
            }
            return ResponseResult.success(PayStatus.TRADE_SUCCESS.getValue());
        } catch (AlipayApiException e) {
            logger.error(e.getErrMsg());

        }
        return ResponseResult.error("订单查询失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> witOutPay(String userId,String orderId) throws AlipayApiException {
        try(Jedis jedis= redisPool.getConnection()){
            String payOrder = jedis.get("pay_order:" + userId);
            if(Strings.isBlank(payOrder)){
                return ResponseResult.error("未找到订单记录");
            }
            PayOrder one = JSON.parseObject(payOrder, PayOrder.class);
            WxUser user = userMapper.selectOne(new LambdaQueryWrapper<WxUser>().eq(WxUser::getUserId, userId));
            // 当前的金额
            BigDecimal thisMoney = user.getDecimals();
            // 版本号 乐观锁
            int version = Math.toIntExact(user.getVersion());
            user.setDecimals(thisMoney.add(one.getMoney()));
            user.setVersion((long) (version+1));
            userMapper.update(user,new LambdaQueryWrapper<WxUser>().eq(WxUser::getUserId,userId).eq(WxUser::getMoney,thisMoney).eq(WxUser::getVersion,version));
            one.setStatus(2);
            update(one,new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOrderId,one.getOrderId()).eq(PayOrder::getStatus,1));
            jedis.del("pay_order:"+userId);
        }
        return ResponseResult.success(PayStatus.TRADE_SUCCESS.getValue());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> sucPay(String userId,String orderId) throws AlipayApiException {
        if (RuoYiConfig.checkPayMoney()){
            String message = commonAlipay.hasOverOrder(orderId);
            if(Strings.isNotBlank(message)){
                if(PayStatus.TRADE_SUCCESS.getValue().equals(message) || PayStatus.TRADE_FINISHED.getValue().equals(message)){
                    witOutPay(userId, orderId);
                    ossUtil.getClient().deleteFile(propertiesConfig.getBUKKET_NAME(),orderId+".png");
                }
                return ResponseResult.success(message);
            }
        }else {
            return witOutPay(userId, orderId);
        }
        return ResponseResult.error("订单支付失败");
    }

    @Override
    public ResponseResult<String> notPayOrder(String userId,String orderId) {
        try (Jedis jedis=redisPool.getConnection()){
            jedis.del("pay_order:"+userId);
        }
        ossUtil.getClient().deleteFile(propertiesConfig.getBUKKET_NAME(),orderId+".png");
        remove(new LambdaQueryWrapper<PayOrder>().ge(PayOrder::getUserId, userId).eq(PayOrder::getOrderId,orderId).eq(PayOrder::getStatus, 1));
        return ResponseResult.success("删除订单成功");
    }

    @Override
    public ResponseResult<PayOrder> hasOnlineOrder(String userId) {
        try (Jedis jedis=redisPool.getConnection()){
            String payOrder = jedis.get("pay_order:" + userId);
            if(Strings.isNotBlank(payOrder)){
               return ResponseResult.success(JSON.parseObject(payOrder,PayOrder.class));
            }
        }
        return ResponseResult.success(new PayOrder());
    }


    @Override
    public ResponseResult<HashMap<String, String>> onlinePay(PayOrder payOrder) {
        String orderId = TimeUtil.getTimeId();
        System.out.println("\n\n\n\n\n\n" + RuoYiConfig.checkPayMoney()+"\n\n\n\n\n\n");
        if (RuoYiConfig.checkPayMoney()){
            try(Jedis jedis=redisPool.getConnection()) {
                String url = commonAlipay.createOrder("充值余额", payOrder.getMoney().toString(),orderId);
                File generate = QrCodeUtil.generate(url, 600, 600, new File(propertiesConfig.getFilePath()+"/"+orderId+".png"));
                ossUtil.getClient().uploadFile(propertiesConfig.getBUKKET_NAME(),orderId+".png",generate);
                if (generate.delete()) {
                    logger.info("已删除临时文件");
                }
            } catch (AlipayApiException e) {
                logger.error(e.getErrMsg());
            }
        }
        return onlineWithOutPay(payOrder,ossPath+orderId+".png");
    }

    public ResponseResult<HashMap<String, String>> onlineWithOutPay(PayOrder payOrder,String payImage) {
        String orderId = TimeUtil.getTimeId();
        payImage = null == payImage ? RuoYiConfig.getWithOutPayImage() : payImage;
        try(Jedis jedis=redisPool.getConnection()) {
            payOrder.setOrderId(orderId);
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            payOrder.setCreateTime(timestamp);
            payOrder.setStatus(1);
            payOrder.setPayType(PayEnum.AliPay.getName());
            SetParams setParams=new SetParams();
            // 15分钟过期
            setParams.ex(30*30*1200);
            payOrder.setImage(payImage);
            payOrderMapper.insert(payOrder);
            String theOrder = JSON.toJSONString(payOrder);
            produce.sendMessage("fanout_order_exchange", JSON.toJSONString(payOrder));
            jedis.set("pay_order:"+payOrder.getUserId(),theOrder,setParams);
            HashMap<String, String> res = new HashMap<>(8);
            res.put("image",payImage);
            res.put("orderId",payOrder.getOrderId());
            return ResponseResult.success(res);
        }
    }
}
