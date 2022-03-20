package com.myxiaowang.logistics.aop;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Order;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;



/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年03月20日 10:34:00
 */
@Component
@Aspect
@org.springframework.core.annotation.Order(1)
public class CreateOrderAop {

    private final Logger logger = LoggerFactory.getLogger(CreateOrderAop.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisPool redisPool;

    @Pointcut(value = "execution(* com.myxiaowang.logistics.controller.OrderController.createOrder(..))")
    public void point(){}


    @Around(value = "point()")
    public Object aRoundPoint(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Order arg = (Order) args[0];
        System.out.println(arg);
        try(Jedis jedis=redisPool.getConnection()) {
            String s = jedis.get(arg.getUserId());
            User user;
            if (Strings.isEmpty(s)) {
                user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserid, arg.getUserId()));
            } else {
                user = JSON.parseObject(s, User.class);
            }
            if (user.getDecimals().compareTo(arg.getMoney()) < 0) {
                return ResponseResult.error("金额不足，不能生成订单");
            }
        }
        return pjp.proceed();
    }
}
