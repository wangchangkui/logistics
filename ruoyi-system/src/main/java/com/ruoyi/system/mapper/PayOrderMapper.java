package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PayOrder;

/**
 * 支付管理Mapper接口
 * 
 * @author 23909
 * @date 2022-02-26
 */
public interface PayOrderMapper 
{
    /**
     * 查询支付管理
     * 
     * @param id 支付管理主键
     * @return 支付管理
     */
    public PayOrder selectPayOrderById(Integer id);

    /**
     * 查询支付管理列表
     * 
     * @param payOrder 支付管理
     * @return 支付管理集合
     */
    public List<PayOrder> selectPayOrderList(PayOrder payOrder);

    /**
     * 新增支付管理
     * 
     * @param payOrder 支付管理
     * @return 结果
     */
    public int insertPayOrder(PayOrder payOrder);

    /**
     * 修改支付管理
     * 
     * @param payOrder 支付管理
     * @return 结果
     */
    public int updatePayOrder(PayOrder payOrder);

    /**
     * 删除支付管理
     * 
     * @param id 支付管理主键
     * @return 结果
     */
    public int deletePayOrderById(Integer id);

    /**
     * 批量删除支付管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayOrderByIds(String[] ids);
}
