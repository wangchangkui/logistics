package com.myxiaowang.logistics.aop;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import com.myxiaowang.logistics.dao.ArrearsMapper;
import com.myxiaowang.logistics.dao.LogisticsMapper;
import com.myxiaowang.logistics.dao.OrderMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Arrears;
import com.myxiaowang.logistics.pojo.Logistics;
import com.myxiaowang.logistics.pojo.Order;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月26日 20:08:00
 */
@Component
@Aspect
public class ConfirmOrderAop {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArrearsMapper arrearsMapper;

    @Autowired
    private LogisticsMapper logisticsMapper;

    @Autowired
    private RedisPool redisPool;

    @Before(value = "execution(* com.myxiaowang.logistics.service.OrderService.confirmOrder(*,*))")
    public void before(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        try(Jedis jedis = redisPool.getConnection()){
            String s = jedis.get(args[1].toString());
            Order order=null;
            // 如果redis不存在数据
            if(Objects.isNull(s)){
                // 查询数据库
                Order order1 = orderMapper.selectOne(new QueryWrapper<Order>().eq("orderid", args[1]));
                if(Objects.nonNull(order1)){
                    order=order1;
                    jedis.set(args[1].toString(),JSON.toJSONString(order));
                }
            }else{
                order=JSON.parseObject(s,Order.class);
            }

            // 有且仅有订单存在的时候，才去判断用户钱是否狗扣
            if(Objects.nonNull(order)){
                // 当前用户的数据
                User user = userMapper.selectOne(new QueryWrapper<User>().eq("userid", args[0]));
                // 被扣钱的用户的数据
                User user2 = userMapper.selectOne(new QueryWrapper<User>().eq("userid", order.getUserId()));
                if (user2.getDecimals().compareTo(order.getMoney())<1) {
                    // 钱不够扣 进入赊账表
                    Arrears arrears = new Arrears();
                    arrears.setOrderId(args[1].toString());
                    arrears.setMoney(order.getMoney());
                    arrears.setGoodsName(order.getGoodsName());
                    arrears.setUsername(user.getUsername());
                    arrears.setUserId(user2.getUserid());
                    arrearsMapper.insert(arrears);
                }
            }
        }
    }

    @After(value = "execution(* com.myxiaowang.logistics.service.OrderService.confirmOrder(*,*))")
    public void after(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        try(Jedis jedis = redisPool.getConnection()){
            String s = jedis.get(args[1].toString());
            if(Objects.nonNull(s)){
                Order order = JSON.parseObject(s, Order.class);
                Logistics logistics = new Logistics();
                logistics.setStatus(2);
                logistics.setLogistics(IdUtil.simpleUUID());
                logistics.setUserId(args[0].toString());
                logistics.setGoods(order.getGoodsName());
                logistics.setMoney(order.getMoney());
                logistics.setCreateTime(order.getCreateTime());
                logistics.setGetUser(order.getUserId());
                logisticsMapper.insert(logistics);
                // 最后删除redis的订单数据
                jedis.del(args[1].toString());
            }
        }

    }

}
