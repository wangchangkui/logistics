package com.myxiaowang.logistics.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.dao.ArrearsMapper;
import com.myxiaowang.logistics.dao.LogisticsMapper;
import com.myxiaowang.logistics.dao.OrderMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.*;
import com.myxiaowang.logistics.service.OrderService;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import com.myxiaowang.logistics.util.Reslut.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.Jedis;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;



/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月20日 17:08:00
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private RedisPool redisPool;

    @Autowired
    private LogisticsMapper logisticsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArrearsMapper arrearsMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult<String> confirmOrder(String userId, String orderId) {
        Order order = getOne(new QueryWrapper<Order>().eq("userid", userId).eq("orderid", orderId));
        if(Objects.isNull(order)){
            ResponseResult.error(ResultInfo.NO_RESULT);
        }
        Logistics logistics = new Logistics();

        // 当前用户的数据
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("userid", userId));
        // 被扣钱的用户的数据
        User user2 = userMapper.selectOne(new QueryWrapper<User>().eq("userid", order.getUserId()));
        // 如果用户的数据钱不够扣 怎么办？？？
        if (user2.getDecimals().compareTo(order.getMoney())<1) {
            logistics.setStatus(2);
            Arrears arrears = new Arrears();
            arrears.setOrderId(orderId);
            arrears.setMoney(order.getMoney());
            arrears.setGoodsName(order.getGoodsName());
            arrears.setUsername(user.getUsername());
            arrears.setUserId(user2.getUserid());
            arrearsMapper.insert(arrears);
        }
        else{
            // 乐观锁的保证
            BigDecimal bigDecimal=user.getDecimals();
            BigDecimal bigDecimal2=user2.getDecimals();
            Integer version=user.getVersion();
            Integer version2 = user2.getVersion();
            // 需要修改的值
            user.setDecimals(bigDecimal.add(order.getMoney()));
            user.setVersion(version+1);
            user2.setDecimals(bigDecimal2.subtract(order.getMoney()));
            user2.setVersion(version2+1);
            // 更新数据
            int update1 = userMapper.update(user, new QueryWrapper<User>().eq("userid", user.getUserid()).eq("money", bigDecimal).eq("version", version));
            int update2 = userMapper.update(user2, new QueryWrapper<User>().eq("userid", user2.getUserid()).eq("money", bigDecimal2).eq("version", version));
            // 需要保证2条数据能正常返回
            if(update1 <1 || update2<1){
                throw new RuntimeException("数据不正确");
            }
        }

        logistics.setLogistics(IdUtil.simpleUUID());
        logistics.setUserId(userId);
        logistics.setGoods(order.getGoodsName());
        logistics.setMoney(order.getMoney());
        logistics.setCreateTime(order.getCreationTime());
        logistics.setGetUser(order.getUserId());
        logisticsMapper.insert(logistics);

       return ResponseResult.success(ResultInfo.SUCCESS.getMessage());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult<String> getOrder(String orderId) {
        Order order;
        try (Jedis jedis=redisPool.getConnection()){
            String rs = jedis.get(orderId);
            if(Objects.isNull(rs)){
               order=getOne(new QueryWrapper<Order>().eq("orderid",orderId));
               if(Objects.isNull(order)){
                   return ResponseResult.success("抢到订单");
               }
            }else{
                order= JSON.parseObject(rs, Order.class);
            }
            // 如果status 不是1 则说明 已经被别人拿到了
            if(!Stutas.hasStutasWithWait(order.getStatus())){
                return ResponseResult.success("订单已经被别人抢到手了");
            }
            Integer temp= order.getVersion();
            order.setVersion(order.getVersion()+1);
            order.setStatus(Stutas.ORDER.getId());
            // 乐观锁 在传入之前我需要先保证这个没有被修改果
            int update = orderMapper.update(order, new QueryWrapper<Order>().eq("orderid", orderId).eq("version", temp));
            if(update<1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("订单被占用");
            }
            jedis.set(orderId,JSON.toJSONString(order));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return ResponseResult.success("抢到订单");
    }

    @Override
    public ResponseResult<String> createOrder(Order order) {
        order.setOrderId(IdUtil.objectId());
        if (save(order)) {
            return ResponseResult.success("生成订单成功");
        }
        logger.error(JSON.toJSONString(order));
        return ResponseResult.success("失败");
    }

    @Override
    public ResponseResult<String> cancelOrder(String userId, String orderId) {
        Order order = getOne(new QueryWrapper<Order>().eq("userid", userId).eq("orderid", orderId));
        if(Objects.isNull(order)){
            ResponseResult.error(ResultInfo.NO_RESULT);
        }
        // 只有等待中的订单可以提交
        if(Stutas.hasStutasWithWait(order.getStatus())){
            order.setStatus(Stutas.CANCEL.getId());
            order.setOverTime( Date.from(LocalDateTime.now().atZone( ZoneId.systemDefault()).toInstant()));
            return ResponseResult.success("订单取消成功");
        }
        return ResponseResult.error(400,"订单已经"+Stutas.getStatus(order.getStatus()));
    }


}
