package com.myxiaowang.logistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
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
    void contextLoads() throws ExecutionException, InterruptedException {
        String url1="https://ocr-demo-1254418846.cos.ap-guangzhou.myqcloud.com/card/IDCardOCR/IDCardOCR1.jpg";
        String requestUrl="https://cardnumber.market.alicloudapi.com/rest/160601/ocr/ocr_idcard.json";
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json; charset=UTF-8");
        header.put("Authorization","APPCODE "+"7dd5ccbdac4d4e52834b512d73c163dd");
        HashMap<String, String> json = new HashMap<>(8);
        json.put("side","face");
        HashMap<String, Object> query = new HashMap<>(8);
        query.put("image",url1);
        query.put("configure",json);
        String s = JSONObject.toJSONString(query);
        String s1 = HttpRequest.postRequest(requestUrl, s, header);

    }
}
