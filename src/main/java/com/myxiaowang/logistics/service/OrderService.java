package com.myxiaowang.logistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myxiaowang.logistics.pojo.Order;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月20日 17:07:00
 */
public interface OrderService extends IService<Order> {


    /**
     * 分页获取订单数据
     * @param page 值
     * @param size 大小
     * @return 分页数据
     */
    ResponseResult<IPage<Order>> getOrderList(int page, int size);

    /**
     * 用户确认数据
     * @param userId 确认的用户id
     * @param orderId 订单id
     * @return 提交结果
     */
    ResponseResult<String> confirmOrder(String userId,String orderId);

    /**
     * 抢订单
     * @param orderId orderId
     * @return 抢到的结果
     */
    ResponseResult<String> getOrder(String orderId);

    /**
     * 创建订单
     * @param order 订单对象
     * @return 创建订单
     */
    ResponseResult<String> createOrder(Order order);


    /**
     * 用于取消订单
     * @param userId 用户id
     * @param orderId 订单id
     * @return 返回值
     */
     ResponseResult<String> cancelOrder(String userId,String orderId);
}
