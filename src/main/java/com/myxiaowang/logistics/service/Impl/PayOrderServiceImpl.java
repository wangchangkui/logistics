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
import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.service.PayOrderService;
import com.myxiaowang.logistics.util.Enum.PayEnum;
import com.myxiaowang.logistics.util.OSS.OssUtil;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.File;
import java.sql.Timestamp;
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
    public ResponseResult<Integer> orderStatus(String orderId) {
        try {
            int i = commonAlipay.hasOverOrder(orderId);
            return ResponseResult.success(i);
        } catch (AlipayApiException e) {
            logger.error(e.getErrMsg());

        }
        return ResponseResult.success(-1);
    }

    @Override
    public ResponseResult<String> sucPay(String userId,String orderId) {
        ResponseResult<Integer> integerResponseResult = orderStatus(orderId);
        if(integerResponseResult.getData()==1){
            try(Jedis jedis= redisPool.getConnection()){
                String payOrder = jedis.get("pay_order:" + userId);
                PayOrder one;
                if(Strings.isBlank(payOrder)){
                    one = getOne(new QueryWrapper<PayOrder>().eq("user_id", userId).eq("order_id", orderId).eq("status", 1));
                }else{
                    one = JSON.parseObject(payOrder, PayOrder.class);
                }
                if(Objects.isNull(one)){
                    return ResponseResult.error("为找到订单记录");
                }
                one.setStatus(2);
                updateById(one);
            }
        }
        return ResponseResult.error("订单支付失败");
    }

    @Override
    public ResponseResult<String> notPayOrder(String userId,String orderId) {
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
    public ResponseResult<String> onlinePay(PayOrder payOrder) {
        String orderId = IdUtil.simpleUUID();
        try(Jedis jedis=redisPool.getConnection()) {
            String url = commonAlipay.createOrder("充值余额", payOrder.getMoney().toString(), payOrder.getOrderId());
            payOrder.setOrderId(orderId);
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            payOrder.setCreateTime(timestamp);
            payOrder.setStatus(1);
            payOrder.setPayType(PayEnum.AliPay.getName());
            String theOrder = JSON.toJSONString(orderId);
            SetParams setParams=new SetParams();
            setParams.ex(30*30*1200);
            jedis.set(orderId,theOrder,setParams);
            produce.sendMessage("fanout_order_exchange",JSON.toJSONString(payOrder));
            File generate = QrCodeUtil.generate(url, 600, 600, new File(propertiesConfig.getFilePath()));
            ossUtil.getClient().uploadFile(propertiesConfig.getBUKKET_NAME(),orderId+".png",generate);
            if (generate.delete()) {
                logger.info("已删除临时文件");
            }
            return ResponseResult.success(ossPath+orderId+".png");
        } catch (AlipayApiException e) {
            logger.error(e.getErrMsg());
        }
        return null;
    }
}
