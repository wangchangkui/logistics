package com.myxiaowang.logistics.aop;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.myxiaowang.logistics.dao.*;
import com.myxiaowang.logistics.pojo.*;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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

    private final Logger log = LoggerFactory.getLogger(ConfirmOrderAop.class);

    /**
     * 用于判断是否是进入赊账表
     * 默认不进入赊账表
     */
    private Integer status=2;

    private TransactionStatus transaction;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private LogisticsMapper logisticsMapper;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private RedisPool redisPool;

    @Pointcut("execution(* com.myxiaowang.logistics.service.OrderService.confirmOrder(*,*))")
    public void aspect(){}

    @Around(value = "aspect()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        if(transaction==null){
            transaction= dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
        }
        Object[] args = joinPoint.getArgs();
        try(Jedis jedis = redisPool.getConnection()){
            String s = jedis.get(args[1].toString());
            Order order=null;
            // 如果redis不存在数据
            if(Objects.isNull(s)){
                // 查询数据库
                Order order1 = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_id", args[1].toString()));
                if(Objects.nonNull(order1)){
                    order=order1;
                    jedis.set(args[1].toString(),JSON.toJSONString(order));
                }
            }else{
                order=JSON.parseObject(s,Order.class);
            }
            // 有且仅有订单存在的时候，才去判断用户钱是否够扣
            if(Objects.nonNull(order)){
                User user2=userMapper.selectOne(new QueryWrapper<User>().eq("user_id",order.getUserId()));
                if (user2.getDecimals().compareTo(order.getMoney())<1) {
                    status=3;
                }
                // 当前用户的数据
                User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", args[0]));
                if(Objects.isNull(user)){
                    return ResponseResult.error("不存在的用户");
                }
                if(StringUtils.isEmpty(user.getIdCard())){
                    return ResponseResult.error("用户未认证，请先认证用户");
                }
                return joinPoint.proceed();
            }
        }
        return ResponseResult.error("未找到订单信息");

    }

    @After(value = "aspect()")
    public void after(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        // 从redis中获取数据
        try(Jedis jedis = redisPool.getConnection()){
            String s = jedis.get(args[1].toString());
            Order order;
            // 可能回出现redis数据丢失的情况
            // 所以建议还是查询数据库一次
            if(Objects.nonNull(s)){
                order = JSON.parseObject(s, Order.class);
            }else{
                order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_id", args[1].toString()));
            }
            if(Objects.nonNull(order)){
                Logistics logistics = logisticsMapper.selectOne(new QueryWrapper<Logistics>().eq("user_id", args[0].toString()).eq("logistics_id", args[1].toString()));
                logistics.setStatus(status);
                logisticsMapper.updateById(logistics);
                // 最后删除redis的订单数据
                jedis.del(args[1].toString());
                // 修改订单状态
                order.setStatus(5);
                order.setOverTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                orderMapper.updateById(order);
                dataSourceTransactionManager.commit(transaction);
                // 改回默认值
                status=2;
            }
        }
    }

    @AfterThrowing(value = "aspect()",throwing = "exception")
    public void afterThrowing(Throwable exception){
        log.error(exception.getMessage());
        dataSourceTransactionManager.rollback(transaction);
    }

}
