package com.myxiaowang.logistics.common.RabbitLisener;

import com.alibaba.fastjson.JSON;
import com.myxiaowang.logistics.dao.PayOrderMapper;
import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 *
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月08日 11:42:00
 */

@Component
public class DeadListener {

    private final Logger log = LoggerFactory.getLogger(DeadListener.class);

    @Autowired
    private RedisPool redisPool;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @RabbitHandler
    @RabbitListener(queues = {"dead_ttl.direct.queue"})
    public void receive(String message){
        try(Jedis jedis=redisPool.getConnection()){
            log.info("删除验证码.....");
            jedis.del(message);
        }
    }

    @RabbitHandler
    @RabbitListener(queues = {"order"})
    public void orderReceive(String message){
        PayOrder payOrder = JSON.parseObject(message, PayOrder.class);
        payOrderMapper.insert(payOrder);
    }
}
