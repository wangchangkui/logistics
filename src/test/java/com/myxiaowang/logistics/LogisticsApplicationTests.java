package com.myxiaowang.logistics;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myxiaowang.logistics.common.TxSms.SendSms;
import com.myxiaowang.logistics.config.PropertiesConfig;
import com.myxiaowang.logistics.dao.AddressMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Address;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
class LogisticsApplicationTests {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SendSms sendSms;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Test
    void contextLoads() {
        rabbitTemplate.convertAndSend(propertiesConfig.getExchange(),propertiesConfig.getRoute_key(),"等待死亡");
    }
}
