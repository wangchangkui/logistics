package com.myxiaowang.logistics.aop;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myxiaowang.logistics.dao.OrderMapper;
import com.myxiaowang.logistics.pojo.Order;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * Aop执行方法前
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月25日 17:37:00
 */
@Aspect
@Component
public class OrderAspect {
    Logger log = LoggerFactory.getLogger(OrderAspect.class);

    @Autowired
    private RedisPool redisPool;

    @Autowired
    private OrderMapper orderMapper;

    @Pointcut("@annotation(com.myxiaowang.logistics.util.Annotation.MyAop)")
    private void cutMethod(){

    }
    @Before("cutMethod()")
    public void before(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String arg = args[0].toString();
        // 先去查询订单是否还在
        try(Jedis jedis=redisPool.getConnection()){
            Order target;
            String order = jedis.get(arg);
            if(Objects.isNull(order)){
                Order orderid = orderMapper.selectOne(new QueryWrapper<Order>().eq("orderid", arg));
                // 订单状态必须是可用的情况下
                if(Objects.nonNull(orderid) &&  orderid.getStatus().equals(1)){
                    target=orderid;
                    SetParams setParams = new SetParams();
                    setParams.ex(86000);
                    jedis.set(target.getOrderId(),JSON.toJSONString(target),setParams);
                }

            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @After("cutMethod()")
    public void after(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String arg = args[0].toString();
        //从redis中删除订单
        try(Jedis jedis=redisPool.getConnection()){
            String s = jedis.get(arg);
            if(Objects.nonNull(s)){
                jedis.del(arg);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * 这个地方才是织入点
     * @param proceedingJoinPoint 植入点
     * @return 返回
     * @throws Throwable 异常
     */
    @Around("cutMethod()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      return  proceedingJoinPoint.proceed();
    }



}
