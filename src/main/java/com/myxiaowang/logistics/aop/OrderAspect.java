package com.myxiaowang.logistics.aop;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.myxiaowang.logistics.dao.LogisticsMapper;
import com.myxiaowang.logistics.dao.OrderMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Logistics;
import com.myxiaowang.logistics.pojo.Order;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.Impl.OrderServiceImpl;
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
import redis.clients.jedis.params.SetParams;

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

    @Autowired
    private LogisticsMapper logisticsMapper;
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    private TransactionStatus transaction;
    @Autowired
    private UserMapper userMapper;


    /**
     * 环绕点
     * 所有带上了注解的类 都会走这个AOP
     * 不适用
     * 不建议使用
     */
    @Pointcut("@annotation(com.myxiaowang.logistics.util.Annotation.MyAop)")
    private void cutMethod(){

    }

    @After("cutMethod()")
    public void after(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String arg = args[0].toString();
        //从redis中删除订单
        try(Jedis jedis=redisPool.getConnection()){
            String s = jedis.get(arg);
            if(Objects.nonNull(s)){
                Order order = JSON.parseObject(s, Order.class);
                //我还需要更新我自己的订单表
                Logistics logistics = new Logistics();
                logistics.setLogistics(order.getOrderId());
                logistics.setStatus(1);
                logistics.setGoods(order.getGoodsName());
                logistics.setGetUser(order.getUserId());
                logistics.setCode(order.getCode());
                logistics.setUserId(args[1].toString());
                logistics.setMoney(order.getMoney());
                logistics.setCreateTime(order.getCreateTime());
                logisticsMapper.insert(logistics);
                dataSourceTransactionManager.commit(transaction);
                jedis.del(arg);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        // 最后通知用户 订单被接手了 消息框内 发送消息(RabbitMq)

    }

    /**
     * 这个地方才是织入点
     * @param proceedingJoinPoint 植入点
     * @return 返回
     * @throws Throwable 异常
     */
    @Around("cutMethod()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if(transaction==null){
            transaction = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
        }
        Object[] args = proceedingJoinPoint.getArgs();
        String arg = args[0].toString();
        // 先去查询订单是否还在
        try(Jedis jedis=redisPool.getConnection()){
            Order target = null;
            String order = jedis.get(arg);
            if(Objects.isNull(order)){
               target = orderMapper.selectOne(new QueryWrapper<Order>().eq("orderid", arg));
            }
            else{
                target=JSON.parseObject(order,Order.class);
            }
            if(Objects.nonNull(target)){
                // 抢到订单的先去判断是不是自己的订单 自己的订单不允许抢
                if(Objects.equals(target.getUserId(),args[1])){
                    return ResponseResult.error("自己不允许抢自己的订单");
                }
                // 判断自己有没有认证
                User user = userMapper.selectOne(new QueryWrapper<User>().eq("userid", args[1]));
                if(StringUtils.isEmpty(user.getIdCard())){
                    return ResponseResult.error("用户未认证");
                }
                // 订单状态必须是可用的情况下
                if( target.getStatus().equals(1)){
                    SetParams setParams = new SetParams();
                    setParams.ex(86000);
                    jedis.set(target.getOrderId(),JSON.toJSONString(target),setParams);
                }
            }else{
                return ResponseResult.error("订单不存在");
            }

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return  proceedingJoinPoint.proceed();
    }

    /**
     * 出现异常的时候
     * @param exception 异常
     */
    @AfterThrowing(value = "cutMethod()",throwing = "exception")
    public void afterError(Throwable exception){
        dataSourceTransactionManager.rollback(transaction);
    }

}
