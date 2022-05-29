package com.ruoyi.system.service.wx.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.common.core.domain.resutl.ResultInfo;
import com.ruoyi.common.utils.redis.RedisPool;
import com.ruoyi.system.annotion.OrderAop;
import com.ruoyi.system.bean.vo.OrdersVO;
import com.ruoyi.system.domain.Orders;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.Arrears;
import com.ruoyi.system.domain.wx.Logistics;
import com.ruoyi.system.plus.mapper.wx.ArrearsMapper;
import com.ruoyi.system.plus.mapper.wx.LogisticsMapper;
import com.ruoyi.system.plus.mapper.wx.OrderMapper;
import com.ruoyi.system.plus.mapper.wx.UserMapper;
import com.ruoyi.system.service.wx.OrderService;
import com.ruoyi.system.utils.OrderUtil;
import com.ruoyi.system.utils.emu.OrderStutas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2022年01月20日 17:08:00
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final RedisPool redisPool;

    private final LogisticsMapper logisticsMapper;

    private final OrderMapper orderMapper;

    private final UserMapper userMapper;

    private final ArrearsMapper arrearsMapper;

    @Autowired
    public OrderServiceImpl(RedisPool redisPool, LogisticsMapper logisticsMapper, OrderMapper orderMapper, UserMapper userMapper, ArrearsMapper arrearsMapper) {
        this.redisPool = redisPool;
        this.logisticsMapper = logisticsMapper;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.arrearsMapper = arrearsMapper;
    }


    @Override
    public ResponseResult<List<Orders>> getOrderByUser(String userId) {
        return ResponseResult.success( list(new LambdaQueryWrapper<Orders>().eq(Orders::getUserId,userId).orderByDesc(Orders::getCreateTime)) );
    }

    @Override
    public ResponseResult<List<Logistics>> getOrderByUser(String userId, int type) {
        OrdersVO vo = new OrdersVO();
        vo.setUserId(userId);
        vo.setType(type);
        return ResponseResult.success(logisticsMapper.getUserLogistics(vo));
    }

    @Override
    public ResponseResult<List<Orders>> getOrdersByCond(int v1, String con, String userId) {
        System.out.println(v1);
        Wrapper<Orders> select;
        try {
            select = OrderUtil.getSelect(v1, con,userId);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return null;
        }
        List<Orders> list = list(select);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult<List<Orders>> getOrdersByCond(int v1, String con) {
        // 修复一个接口共用的bug 别问我为什么这么写 因为我懒
        v1=1;
        Wrapper<Orders> select;
        try {
            select = OrderUtil.getSelect(v1, con,"");
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return null;
        }
        List<Orders> list = list(select);
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult<IPage<Orders>> getOrderList(int page, int size) {
        IPage<Orders> iPage = new Page<>(page, size);
        IPage<Orders> target = page(iPage, new QueryWrapper<Orders>().eq("status", 1));
        return ResponseResult.success(target);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult<String> confirmOrder(String userId, String orderId) {
        Orders order;
        // 由于Aop的原因 只要订单存在，则一定在redis内
        try (Jedis jedis = redisPool.getConnection()) {
            String s = jedis.get(orderId);
            order = Optional.ofNullable(JSON.parseObject(s, Orders.class))
                    .orElse(orderMapper
                            .selectOne(new LambdaQueryWrapper<Orders>()
                                .eq(Orders::getOrderId,orderId)));
        }
        if (Objects.isNull(order)) {
            ResponseResult.error(ResultInfo.NO_RESULT);
        }
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<Orders>()
                .set(Orders::getStatus, OrderStutas.PAY.getId())
                .eq(Orders::getOrderId, orderId)
                .eq(Orders::getAcceptUserId, userId)
                .eq(Orders::getStatus, OrderStutas.ORDER.getId());
        logisticsMapper.update(null,new LambdaUpdateWrapper<Logistics>()
                .set(Logistics::getStatus,OrderStutas.PAY.getId())
                .eq(Logistics::getOrderId,orderId));
        return ResponseResult.success(orderMapper.update(null,updateWrapper) > 0 ? ResultInfo.SUCCESS.getMessage() : ResultInfo.REQUEST_FAIL.getMessage());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Override
//    @OrderAop(module = "getOrder")
    public ResponseResult<String> getOrder(String orderId, String userId, String address) {
        Orders order;
        // AOP那里的时候 订单肯定存在redis内，如果redis不存在 说明没有订单
        try (Jedis jedis = redisPool.getConnection()) {
            String rs = jedis.get(orderId);
            order = Optional.ofNullable(JSON.parseObject(rs, Orders.class)).orElse(orderMapper.selectOne(new LambdaQueryWrapper<Orders>().eq(Orders::getOrderId,orderId).eq(Orders::getStatus,1)));
            // 如果status 不是1 则说明 已经被别人拿到了
            if (!OrderStutas.WAIT.getId().equals(order.getStatus())) {
                return ResponseResult.error("订单已经被别人抢到手了");
            }
            if (order.getUserId().equals(userId)){
                return ResponseResult.error("自己不允许抢自己的订单");
            }
            // 乐观锁
            Integer temp = Math.toIntExact(order.getVersion());
            order.setVersion(order.getVersion() + 1);
            order.setStatus(OrderStutas.ORDER.getId());
            order.setGoodsAddress(address);
            order.setAcceptUserId(userId);
            //我还需要更新我自己的订单表
            Logistics logistics = new Logistics();
            logistics.setLogistics(order.getOrderId());
            logistics.setOrderId(orderId);
            logistics.setStatus(OrderStutas.ORDER.getId());
            logistics.setGoods(order.getGoodsName());
            logistics.setUserMeId(order.getUserId());
            logistics.setCode(order.getCode());
            logistics.setUserId(userId);
            logistics.setMoney(order.getMoney());
            logistics.setCreateTime(order.getCreateTime());
            int insert = logisticsMapper.insert(logistics);
            // 乐观锁 在传入之前我需要先保证这个没有被修改果
            int update = orderMapper.update(order, new LambdaQueryWrapper<Orders>().eq(Orders::getOrderId, orderId).eq(Orders::getVersion, temp));
//            jedis.del(orderId);
            if (update < 1 && insert != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("订单被占用");
            }
        }
        return ResponseResult.success("抢到订单");
    }

    @Override
    public ResponseResult<String> createOrder(Orders order) {
        LambdaQueryWrapper<Orders> eq = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, order.getUserId())
                .eq(Orders::getStatus, OrderStutas.PAY.getId());
        if (orderMapper.selectCount(eq) > 0){
            logger.error("生成订单失败，存在未支付订单");
            return ResponseResult.success("生成订单失败，存在未支付订单");
        }
        String orderId = IdUtil.objectId();
        order.setOrderId(orderId);
        order.setStatus(OrderStutas.WAIT.getId());
        if (orderMapper.insert(order) > 0) {
            return ResponseResult.success("生成订单成功");
        }
        logger.error(JSON.toJSONString(order));
        return ResponseResult.success("失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> cancelOrder(String userId, String orderId) {
        // 取消订单之前 需要线查询一下订单
        Orders order = getOne(new LambdaQueryWrapper<Orders>().eq(Orders::getUserId,userId).eq(Orders::getOrderId,orderId));
        if (Objects.isNull(order)) {
            ResponseResult.error(ResultInfo.NO_RESULT);
        }
        // 只有等待中的订单可以提交
        if (OrderStutas.WAIT.getId().equals(order.getStatus())) {
            order.setStatus(OrderStutas.CANCEL.getId());
            order.setOverTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            orderMapper.updateById(order);
            return ResponseResult.success("订单取消成功");
        }
        return ResponseResult.error(400, "订单已经" + OrderStutas.getStatus(order.getStatus()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Integer> payMoney(Orders orders) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<Orders>()
                .set(Orders::getStatus, OrderStutas.OVER.getId())
                .eq(Orders::getOrderId, orders.getOrderId())
                .eq(Orders::getStatus, OrderStutas.PAY.getId());
        LambdaUpdateWrapper<Logistics> loguw = new LambdaUpdateWrapper<Logistics>()
                .set(Logistics::getStatus,OrderStutas.OVER.getId())
                .eq(Logistics::getOrderId,orders.getOrderId());
        return ResponseResult.success(orderMapper.update(null,updateWrapper) + logisticsMapper.update(null,loguw)) ;
    }
}
