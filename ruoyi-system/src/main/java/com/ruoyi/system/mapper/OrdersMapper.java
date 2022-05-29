package com.ruoyi.system.mapper;

import java.sql.Timestamp;
import java.util.List;
import com.ruoyi.system.domain.Orders;

/**
 * 订单管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-02-23
 */
public interface OrdersMapper {
    /**
     * 查询订单管理
     * 
     * @param id 订单管理主键
     * @return 订单管理
     */
    public Orders selectOrdersById(Long id);

    /**
     * 查询订单管理列表
     * 
     * @param orders 订单管理
     * @return 订单管理集合
     */
    public List<Orders> selectOrdersList(Orders orders);

    /**
     * 查询订单管理列表
     *
     * @param timestamp 查询时间下限
     * @return 订单管理集合
     */
    public List<Orders> selectOrdersListByCreateTime(Timestamp timestamp);

    /**
     * 新增订单管理
     * 
     * @param orders 订单管理
     * @return 结果
     */
    public int insertOrders(Orders orders);

    /**
     * 修改订单管理
     * 
     * @param orders 订单管理
     * @return 结果
     */
    public int updateOrders(Orders orders);

    /**
     * 删除订单管理
     * 
     * @param id 订单管理主键
     * @return 结果
     */
    public int deleteOrdersById(Long id);

    /**
     * 批量删除订单管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrdersByIds(String[] ids);
}
