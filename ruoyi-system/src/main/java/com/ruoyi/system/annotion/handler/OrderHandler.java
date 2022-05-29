package com.ruoyi.system.annotion.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.common.utils.redis.RedisPool;
import com.ruoyi.system.domain.Orders;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.Logistics;
import com.ruoyi.system.plus.mapper.wx.LogisticsMapper;
import com.ruoyi.system.plus.mapper.wx.OrderMapper;
import com.ruoyi.system.plus.mapper.wx.UserMapper;
import com.ruoyi.system.utils.alipay.PayStatus;
import com.ruoyi.system.utils.emu.OrderStutas;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Objects;

/**
 * @author Jandmla
 * @version 1.0
 * @description OrderAop 注解解析
 * @data 2022/3/17 19:45
 */
@Aspect
@Component
public class OrderHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OrderHandler.class);

    private final RedisPool redisPool;

    private final OrderMapper orderMapper;

    private final LogisticsMapper logisticsMapper;

    private final DataSourceTransactionManager dataSourceTransactionManager;

    private TransactionStatus transaction;

    private final UserMapper userMapper;

    public OrderHandler(RedisPool redisPool, OrderMapper orderMapper, LogisticsMapper logisticsMapper, DataSourceTransactionManager dataSourceTransactionManager, UserMapper userMapper) {
        this.redisPool = redisPool;
        this.orderMapper = orderMapper;
        this.logisticsMapper = logisticsMapper;
        this.dataSourceTransactionManager = dataSourceTransactionManager;
        this.userMapper = userMapper;
    }


    /**
     * 环绕点
     * 所有带上了注解的类 都会走这个AOP
     * 不适用
     * 不建议使用
     */
    @Pointcut("@annotation(com.ruoyi.system.annotion.OrderAop)")
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
                Orders order = JSON.parseObject(s, Orders.class);
                //我还需要更新我自己的订单表
                Logistics logistics = new Logistics();
                logistics.setLogistics(order.getOrderId());
                logistics.setStatus(OrderStutas.ORDER.getId());
                logistics.setGoods(order.getGoodsName());
                logistics.setUserMeId(order.getUserId());
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
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if(transaction==null){
            transaction = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
        }
        Object[] args = proceedingJoinPoint.getArgs();
        String arg = args[0].toString();
        // 先去查询订单是否还在
        try(Jedis jedis=redisPool.getConnection()){
            Orders target = null;
            String order = jedis.get(arg);
            if(Objects.isNull(order)){
                target = orderMapper.selectOne(new LambdaQueryWrapper<Orders>().eq(Orders::getOrderId, arg).eq(Orders::getStatus,1));
            } else{
                target=JSON.parseObject(order,Orders.class);
            }
            if(Objects.nonNull(target)){
                // 抢到订单的先去判断是不是自己的订单 自己的订单不允许抢
                if(Objects.equals(target.getUserId(),args[1])){
                    return ResponseResult.error("自己不允许抢自己的订单");
                }
                // 判断自己有没有认证
                WxUser user = userMapper.selectOne(new LambdaQueryWrapper<WxUser>().eq(WxUser::getUserId, args[1]));
                // todo 自己认证问题，暂时不管，任何人可以接单
//                if(StringUtils.isEmpty(user.getIdCard())){
//                    return ResponseResult.error("用户未认证");
//                }
                // 订单状态必须是可用的情况下
                SetParams setParams = new SetParams();
                setParams.ex(86000);
                jedis.set(target.getOrderId(),JSON.toJSONString(target),setParams);
            }else{
                return ResponseResult.error("订单目前为不可接单状态！");
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
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
        LOG.error(exception.getMessage());
    }
}
