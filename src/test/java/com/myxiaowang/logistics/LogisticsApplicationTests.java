package com.myxiaowang.logistics;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myxiaowang.logistics.dao.AddressMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Address;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.util.List;

@SpringBootTest
class LogisticsApplicationTests {

    @Autowired
    private UserMapper userMapper;


    @Test
    void contextLoads() {
        User s = userMapper.getUser("oYxOB5YFXmYZWH8R6YCE_wqBDZk0");
        System.out.println(s);
    }
}
