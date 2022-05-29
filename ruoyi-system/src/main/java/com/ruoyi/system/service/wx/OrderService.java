package com.ruoyi.system.service.wx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.domain.Orders;
import com.ruoyi.system.domain.wx.Logistics;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月20日 17:07:00
 */
public interface OrderService extends IService<Orders> {


    /**
     * 获取用户发布的订单
     * @param userId 用户Id
     * @return 返回接口
     */
    ResponseResult<List<Orders>> getOrderByUser(String userId);
    /**
     * 根据type类型 返回用户抢到的订单
     * @param userId 用户id
     * @param type 类型
     * @return 订单即集合
     */
    ResponseResult<List<Logistics>> getOrderByUser(String userId, int type);


    /**
     * 根据条件分类获取
     * @param v1 订单类型
     * @param con 条件
     * @param userId 条件
     * @return 集合
     */
    ResponseResult<List<Orders>> getOrdersByCond(int v1,String con,String userId);
    /**
     * 根据条件分类获取
     * @param v1 订单类型
     * @param con 条件
     * @return 集合
     */
    ResponseResult<List<Orders>> getOrdersByCond(int v1,String con);


    /**
     * 分页获取订单数据
     * @param page 值
     * @param size 大小
     * @return 分页数据
     */
    ResponseResult<IPage<Orders>> getOrderList(int page, int size);

    /**
     * 用户确认数据
     * @param userId 确认的用户id
     * @param orderId 订单id
     * @return 提交结果
     */
    ResponseResult<String> confirmOrder(String userId,String orderId);


    /**
     * 获取订单
     * @param orderId 订单id
     * @param userId 订单用户id
     * @param address 取货地址
     * @return 操作
     */
    ResponseResult<String> getOrder(String orderId,String userId,String address);

    /**
     * 创建订单
     * @param order 订单对象
     * @return 创建订单
     */
    ResponseResult<String> createOrder(Orders order);


    /**
     * 用于取消订单 这个接口是用于给下单的用户取消的，不是给抢单的用户取消的
     * @param userId 用户id
     * @param orderId 订单id
     * @return 返回值
     */
     ResponseResult<String> cancelOrder(String userId,String orderId);

    /**
     * 确定订单完成，确定支付
     * @param orders 订单信息
     * @return 确认结果
     */
     ResponseResult<Integer> payMoney(Orders orders);
}
