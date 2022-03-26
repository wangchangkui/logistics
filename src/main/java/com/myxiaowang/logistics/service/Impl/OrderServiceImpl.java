package com.myxiaowang.logistics.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.dao.*;
import com.myxiaowang.logistics.pojo.*;
import com.myxiaowang.logistics.service.OrderService;
import com.myxiaowang.logistics.util.Annotation.MyAop;
import com.myxiaowang.logistics.util.Enum.Stutas;
import com.myxiaowang.logistics.util.OrderUtil;
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
import java.util.List;
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


    @Override
    public ResponseResult<List<Order>> getOrderByUser(String userId) {
        return ResponseResult.success( list(new QueryWrapper<Order>().eq("user_id",userId).orderByDesc("create_time")) );
    }

    @Override
    public ResponseResult<List<Logistics>> getOrderByUser(String userId, int type) {
        return ResponseResult.success(logisticsMapper.getUserLogistics(userId, type));
    }

    @Override
    public ResponseResult<List<Order>> getOrdersByCond(int v1, String con, String userId) {
        System.out.println(v1);
        QueryWrapper<Order> select;
        try {
            select = OrderUtil.getSelect(v1, con,userId);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return null;
        }
        List<Order> list = list(select);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult<List<Order>> getOrdersByCond(int v1, String con) {
        // 修复一个接口共用的bug 别问我为什么这么写 因为我懒
        v1=1;
        QueryWrapper<Order> select;
        try {
            select = OrderUtil.getSelect(v1, con,"");
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return null;
        }
        List<Order> list = list(select);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult<IPage<Order>> getOrderList(int page, int size) {
        IPage<Order> iPage = new Page<>(page, size);
        IPage<Order> target = page(iPage, new QueryWrapper<Order>().eq("status", 1));
        return ResponseResult.success(target);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult<String> confirmOrder(String userId, String orderId) {
        Order order;
        // 由于Aop的原因 只要订单存在，则一定在redis内
        try (Jedis jedis = redisPool.getConnection()) {
            String s = jedis.get(orderId);
            order = JSON.parseObject(s, Order.class);
        }
        if (Objects.isNull(order)) {
            ResponseResult.error(ResultInfo.NO_RESULT);
        }
        // 当前用户的数据
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", userId));
        // 被扣钱的用户的数据
        User user2 = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", order.getUserId()));
        // 乐观锁的保证
        BigDecimal bigDecimal = user.getDecimals();
        BigDecimal bigDecimal2 = user2.getDecimals();
        Integer version = user.getVersion();
        Integer version2 = user2.getVersion();
        // 存在用户钱不够扣的情况，提交数据 并且进入冻结金额
        if (user2.getDecimals().compareTo(order.getMoney()) < 0) {
            Arrears arrears = new Arrears();
            arrears.setOrderId(orderId);
            arrears.setMoney(order.getMoney());
            arrears.setGoodsName(order.getGoodsName());
            arrears.setUsername(user2.getUsername());
            arrears.setUserId(userId);
            arrears.setArrUserid(user2.getUserid());
            arrearsMapper.insert(arrears);
            // 让用户进入冻结金额数据
            user.setFamount(order.getMoney());
            userMapper.updateById(user);
        } else {
            // 需要修改的值
            user.setDecimals(bigDecimal.add(order.getMoney()));
            user.setVersion(version + 1);
            user2.setDecimals(bigDecimal2.subtract(order.getMoney()));
            user2.setVersion(version2 + 1);
            // 更新数据
            int update1 = userMapper.update(user, new QueryWrapper<User>().eq("user_id", user.getUserid()).eq("money", bigDecimal).eq("version", version));
            int update2 = userMapper.update(user2, new QueryWrapper<User>().eq("user_id", user2.getUserid()).eq("money", bigDecimal2).eq("version", version2));
            // 需要保证2条数据能正常返回
            if (update1 < 1 || update2 < 1) {
                throw new RuntimeException("数据不正确");
            }
        }
        return ResponseResult.success(ResultInfo.SUCCESS.getMessage());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @MyAop(module = "getOrder")
    public ResponseResult<String> getOrder(String orderId, String userId, String address) {
        Order order;
        // AOP那里的时候 订单肯定存在redis内，如果redis不存在 说明没有订单
        try (Jedis jedis = redisPool.getConnection()) {
            String rs = jedis.get(orderId);
            order = JSON.parseObject(rs, Order.class);
            // 如果status 不是1 则说明 已经被别人拿到了
            if (!Stutas.hasStutasWithWait(order.getStatus())) {
                return ResponseResult.success("订单已经被别人抢到手了");
            }
            // 乐观锁
            Integer temp = order.getVersion();
            order.setVersion(order.getVersion() + 1);
            order.setStatus(Stutas.ORDER.getId());
            order.setGoodsAddress(address);
            // 乐观锁 在传入之前我需要先保证这个没有被修改果
            int update = orderMapper.update(order, new QueryWrapper<Order>().eq("order_id", orderId).eq("version", temp));
            if (update < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("订单被占用");
            }
            jedis.set(orderId, JSON.toJSONString(order));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseResult.success("抢到订单");
    }

    @Override
    public ResponseResult<String> createOrder(Order order) {
        // 在创建订单之前，我们需要先判断该用户是否有未缴费的订单记录
        Integer userCount = arrearsMapper.selectCount(new QueryWrapper<Arrears>().eq("user_id", order.getUserId()));
        if (userCount > 1) {
            ResultInfo.NO_RESULT.setMessage("该用户存在未缴费的订单");
            return ResponseResult.error(ResultInfo.NO_RESULT);
        }
        String orderid = IdUtil.objectId();
        order.setOrderId(orderid);
        order.setStatus(1);
        if (save(order)) {
            return ResponseResult.success("生成订单成功");
        }
        logger.error(JSON.toJSONString(order));
        return ResponseResult.success("失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> cancelOrder(String userId, String orderId) {
        // 取消订单之前 需要线查询一下订单
        Order order = getOne(new QueryWrapper<Order>().eq("user_id", userId).eq("order_id", orderId));
        if (Objects.isNull(order)) {
            ResponseResult.error(ResultInfo.NO_RESULT);
        }
        // 只有等待中的订单可以提交
        if (Stutas.hasStutasWithWait(order.getStatus())) {
            order.setStatus(Stutas.CANCEL.getId());
            order.setOverTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            orderMapper.updateById(order);
            return ResponseResult.success("订单取消成功");
        }
        return ResponseResult.error(400, "订单已经" + Stutas.getStatus(order.getStatus()));
    }


}
