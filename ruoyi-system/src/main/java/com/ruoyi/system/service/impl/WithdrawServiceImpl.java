package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WithdrawMapper;
import com.ruoyi.system.domain.Withdraw;
import com.ruoyi.system.service.IWithdrawService;
import com.ruoyi.common.core.text.Convert;

/**
 * 提现管理Service业务层处理
 * 
 * @author 23909
 * @date 2022-03-10
 */
@Service
public class WithdrawServiceImpl implements IWithdrawService 
{
    @Autowired
    private WithdrawMapper withdrawMapper;

    /**
     * 查询提现管理
     * 
     * @param id 提现管理主键
     * @return 提现管理
     */
    @Override
    public Withdraw selectWithdrawById(Long id)
    {
        return withdrawMapper.selectWithdrawById(id);
    }

    /**
     * 查询提现管理列表
     * 
     * @param withdraw 提现管理
     * @return 提现管理
     */
    @Override
    public List<Withdraw> selectWithdrawList(Withdraw withdraw)
    {
        return withdrawMapper.selectWithdrawList(withdraw);
    }

    /**
     * 新增提现管理
     * 
     * @param withdraw 提现管理
     * @return 结果
     */
    @Override
    public int insertWithdraw(Withdraw withdraw)
    {
        withdraw.setCreateTime(DateUtils.getNowDate());
        return withdrawMapper.insertWithdraw(withdraw);
    }

    /**
     * 修改提现管理
     * 
     * @param withdraw 提现管理
     * @return 结果
     */
    @Override
    public int updateWithdraw(Withdraw withdraw)
    {
        return withdrawMapper.updateWithdraw(withdraw);
    }

    /**
     * 批量删除提现管理
     * 
     * @param ids 需要删除的提现管理主键
     * @return 结果
     */
    @Override
    public int deleteWithdrawByIds(String ids)
    {
        return withdrawMapper.deleteWithdrawByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除提现管理信息
     * 
     * @param id 提现管理主键
     * @return 结果
     */
    @Override
    public int deleteWithdrawById(Long id) {
        return withdrawMapper.deleteWithdrawById(id);
    }

    /**
     * 拒绝请求
     *
     * @param id 请求ID
     * @return 结果
     */
    @Override
    public int refuseOrder(Long id) {
        final Withdraw withdraw = new Withdraw();
        withdraw.setId(id);
        withdraw.setStatus(0);
        return withdrawMapper.updateWithdraw(withdraw);
    }

    /**
     * 同意 提现 请求
     *
     * @param id 请求ID
     * @return 结果
     */
    @Override
    public int agreeOrder(Long id) {
        final Withdraw withdraw = new Withdraw();
        withdraw.setId(id);
        withdraw.setStatus(1);
        return withdrawMapper.updateWithdraw(withdraw);
    }
}
