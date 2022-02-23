package com.myxiaowang.logistics.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.common.AliPay.CommonAlipay;
import com.myxiaowang.logistics.common.RabbitMq.Produce;
import com.myxiaowang.logistics.config.PropertiesConfig;
import com.myxiaowang.logistics.dao.PayOrderMapper;
import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.service.PayOrderService;
import com.myxiaowang.logistics.util.Enum.PayEnum;
import com.myxiaowang.logistics.util.FileUtils;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;

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
            File generate = QrCodeUtil.generate(url, 600, 600, new File(propertiesConfig.getFilePath()));

        } catch (AlipayApiException e) {
            logger.error(e.getErrMsg());
        }
        return null;
    }
}
