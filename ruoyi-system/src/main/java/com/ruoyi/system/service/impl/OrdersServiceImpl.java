package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OrdersMapper;
import com.ruoyi.system.domain.Orders;
import com.ruoyi.system.service.IOrdersService;
import com.ruoyi.common.core.text.Convert;

/**
 * 订单管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-02-23
 */
@Service
public class OrdersServiceImpl implements IOrdersService {

    private final OrdersMapper ordersMapper;

    public OrdersServiceImpl(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    /**
     * 查询订单管理
     * 
     * @param id 订单管理主键
     * @return 订单管理
     */
    @Override
    public Orders selectOrdersById(Long id)
    {
        return ordersMapper.selectOrdersById(id);
    }

    /**
     * 查询订单管理列表
     * 
     * @param orders 订单管理
     * @return 订单管理
     */
    @Override
    public List<Orders> selectOrdersList(Orders orders)
    {
        return ordersMapper.selectOrdersList(orders);
    }

    /**
     * 新增订单管理
     * 
     * @param orders 订单管理
     * @return 结果
     */
    @Override
    public int insertOrders(Orders orders)
    {
        orders.setCreateTime(DateUtils.getNowDate());
        return ordersMapper.insertOrders(orders);
    }

    /**
     * 修改订单管理
     * 
     * @param orders 订单管理
     * @return 结果
     */
    @Override
    public int updateOrders(Orders orders)
    {
        return ordersMapper.updateOrders(orders);
    }

    /**
     * 批量删除订单管理
     * 
     * @param ids 需要删除的订单管理主键
     * @return 结果
     */
    @Override
    public int deleteOrdersByIds(String ids)
    {
        return ordersMapper.deleteOrdersByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除订单管理信息
     * 
     * @param id 订单管理主键
     * @return 结果
     */
    @Override
    public int deleteOrdersById(Long id)
    {
        return ordersMapper.deleteOrdersById(id);
    }
}
