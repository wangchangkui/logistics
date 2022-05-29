package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PayOrderMapper;
import com.ruoyi.system.domain.PayOrder;
import com.ruoyi.system.service.IPayOrderService;
import com.ruoyi.common.core.text.Convert;

/**
 * 支付管理Service业务层处理
 * 
 * @author 23909
 * @date 2022-02-26
 */
@Service
public class PayOrderServiceImpl implements IPayOrderService 
{
    @Autowired
    private PayOrderMapper payOrderMapper;

    /**
     * 查询支付管理
     * 
     * @param id 支付管理主键
     * @return 支付管理
     */
    @Override
    public PayOrder selectPayOrderById(Integer id)
    {
        return payOrderMapper.selectPayOrderById(id);
    }

    /**
     * 查询支付管理列表
     * 
     * @param payOrder 支付管理
     * @return 支付管理
     */
    @Override
    public List<PayOrder> selectPayOrderList(PayOrder payOrder)
    {
        return payOrderMapper.selectPayOrderList(payOrder);
    }

    /**
     * 新增支付管理
     * 
     * @param payOrder 支付管理
     * @return 结果
     */
    @Override
    public int insertPayOrder(PayOrder payOrder)
    {
        payOrder.setCreateTime(DateUtils.getNowDate());
        return payOrderMapper.insertPayOrder(payOrder);
    }

    /**
     * 修改支付管理
     * 
     * @param payOrder 支付管理
     * @return 结果
     */
    @Override
    public int updatePayOrder(PayOrder payOrder)
    {
        return payOrderMapper.updatePayOrder(payOrder);
    }

    /**
     * 批量删除支付管理
     * 
     * @param ids 需要删除的支付管理主键
     * @return 结果
     */
    @Override
    public int deletePayOrderByIds(String ids)
    {
        return payOrderMapper.deletePayOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付管理信息
     * 
     * @param id 支付管理主键
     * @return 结果
     */
    @Override
    public int deletePayOrderById(Integer id)
    {
        return payOrderMapper.deletePayOrderById(id);
    }
}
