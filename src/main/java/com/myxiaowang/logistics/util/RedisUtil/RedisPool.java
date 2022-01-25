package com.myxiaowang.logistics.util.RedisUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 16:43:00
 */
@Component
public class RedisPool {

    @Value("${spring.redis.host}")
    public String host;

    private volatile JedisPool jdb;


    public void createPool(){
        JedisPoolConfig  poolConfig=new JedisPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(50);
        jdb = new JedisPool(poolConfig, host );
    }

    /**
     * 获取连接
     * @return 返回值
     */
    public Jedis getConnection(){
        if(jdb==null){
            synchronized (this){
               if(jdb==null){
                   createPool();
               }
            }
        }
        return jdb.getResource();
    }

}
