package com.myxiaowang.logistics.aop;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import com.myxiaowang.logistics.config.PropertiesConfig;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import com.myxiaowang.logistics.util.Reslut.ResultInfo;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Objects;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月11日 16:03:00
 */
@Aspect
@Component
public class LoginAop {
   private final  Logger logger=LoggerFactory.getLogger(LoginAop.class);

    @Autowired
    private RedisPool redisPool;


    @Pointcut("@annotation(com.myxiaowang.logistics.util.Annotation.LoginAop)")
    private void cutPoint(){}

    @Around("cutPoint()")
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        try(Jedis jedis=redisPool.getConnection()){
            // 取所有的对象数据
            List<String> userList = jedis.lrange("userList", 0, -1);
            User loginUser = userList.stream().filter(z -> {
                User user1 = JSON.parseObject(z, User.class);
                return args[0].toString().equals(user1.getUsername()) && Md5Crypt.md5Crypt(args[1].toString().getBytes(),"$1$myxiaowang").equals(user1.getPassword());
            }).map(t->
                JSON.parseObject(t,User.class)
            ).findFirst().orElse(null);
            if(Objects.nonNull(loginUser)){
                if(Strings.isBlank(loginUser.getUserid())){
                    return ResponseResult.error("该用户注册但是未认证，请先微信认证");
                }
                return ResponseResult.success(loginUser);
            }
        }
        return proceedingJoinPoint.proceed();
    }

    @AfterThrowing(value = "cutPoint()",throwing = "exception")
    public void throwException(Throwable exception){
        logger.error(exception.getMessage());
    }
}
