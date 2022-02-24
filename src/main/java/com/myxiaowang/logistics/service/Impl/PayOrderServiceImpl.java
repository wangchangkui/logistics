package com.myxiaowang.logistics.service.Impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.common.AliPay.CommonAlipay;
import com.myxiaowang.logistics.common.RabbitMq.Produce;
import com.myxiaowang.logistics.config.PropertiesConfig;
import com.myxiaowang.logistics.dao.PayOrderMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.PayOrderService;
import com.myxiaowang.logistics.util.Enum.PayEnum;
import com.myxiaowang.logistics.util.Enum.PayStatus;
import com.myxiaowang.logistics.util.OSS.OssUtil;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import com.myxiaowang.logistics.util.TimeUtil;
import com.tencentcloudapi.ame.v20190916.models.UseRange;
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
import java.util.Objects;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 16:16:00
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {
    private final Logger logger = LoggerFactory.getLogger(PayOrderServiceImpl.class);

    @Autowired
    private Produce produce;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Autowired
    private RedisPool redisPool;

    @Autowired
    private CommonAlipay commonAlipay;

    @Autowired
    private OssUtil ossUtil;

    @Value("${filePath.osspath}")
    private String ossPath;

    @Override
    public ResponseResult<List<PayOrder>> getUserOrder(String userId) {
        return ResponseResult.success(list(new QueryWrapper<PayOrder>().eq("user_id",userId).eq("status",2)));
    }

    @Override
    public ResponseResult<String> orderStatus(String orderId) {
        try {
            String message = commonAlipay.hasOverOrder(orderId);
            return ResponseResult.success(message);
        } catch (AlipayApiException e) {
            logger.error(e.getErrMsg());

        }
        return ResponseResult.error("订单查询失败");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> sucPay(String userId,String orderId) throws AlipayApiException {
        String message = commonAlipay.hasOverOrder(orderId);
        if(Strings.isNotBlank(message)){
            if(PayStatus.TRADE_SUCCESS.getValue().equals(message) || PayStatus.TRADE_FINISHED.getValue().equals(message)){
                try(Jedis jedis= redisPool.getConnection()){
                    String payOrder = jedis.get("pay_order:" + userId);
                    if(Strings.isBlank(payOrder)){
                        return ResponseResult.error("未找到订单记录");
                    }
                    PayOrder one = JSON.parseObject(payOrder, PayOrder.class);
                    User user = userMapper.selectOne(new QueryWrapper<User>().eq("userid", userId));
                    // 当前的金额
                    BigDecimal thisMoney = user.getDecimals();
                    // 版本号 乐观锁
                    Integer version = user.getVersion();
                    user.setDecimals(thisMoney.add(one.getMoney()));
                    user.setVersion(version+1);
                    userMapper.update(user,new QueryWrapper<User>().eq("userid",userId).eq("money",thisMoney).eq("version",version));
                    one.setStatus(2);
                    updateById(one);
                    ossUtil.getClient().deleteFile(propertiesConfig.getBUKKET_NAME(),orderId+".png");
                    jedis.del("pay_order:"+userId);
                }
            }
            return ResponseResult.success(message);
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
        try(Jedis jedis=redisPool.getConnection()) {
            String url = commonAlipay.createOrder("充值余额", payOrder.getMoney().toString(),orderId);
            payOrder.setOrderId(orderId);
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            payOrder.setCreateTime(timestamp);
            payOrder.setStatus(1);
            payOrder.setPayType(PayEnum.AliPay.getName());
            SetParams setParams=new SetParams();
            // 15分钟过期
            setParams.ex(30*30*1200);
            File generate = QrCodeUtil.generate(url, 600, 600, new File(propertiesConfig.getFilePath()+"/"+orderId+".png"));
            ossUtil.getClient().uploadFile(propertiesConfig.getBUKKET_NAME(),orderId+".png",generate);
            if (generate.delete()) {
                logger.info("已删除临时文件");
            }
            payOrder.setImage(ossPath+orderId+".png");
            String theOrder = JSON.toJSONString(payOrder);
            produce.sendMessage("fanout_order_exchange",JSON.toJSONString(payOrder));
            jedis.set("pay_order:"+payOrder.getUserId(),theOrder,setParams);
            HashMap<String, String> res = new HashMap<>(8);
            res.put("image",ossPath+orderId+".png");
            res.put("orderId",payOrder.getOrderId());
            return ResponseResult.success(res);
        } catch (AlipayApiException e) {
            logger.error(e.getErrMsg());
        }
        return null;
    }
}
