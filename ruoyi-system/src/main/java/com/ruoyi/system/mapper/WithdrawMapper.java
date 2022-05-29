package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Withdraw;

/**
 * 提现管理Mapper接口
 * 
 * @author 23909
 * @date 2022-03-10
 */
public interface WithdrawMapper 
{
    /**
     * 查询提现管理
     * 
     * @param id 提现管理主键
     * @return 提现管理
     */
    public Withdraw selectWithdrawById(Long id);

    /**
     * 查询提现管理列表
     * 
     * @param withdraw 提现管理
     * @return 提现管理集合
     */
    public List<Withdraw> selectWithdrawList(Withdraw withdraw);

    /**
     * 新增提现管理
     * 
     * @param withdraw 提现管理
     * @return 结果
     */
    public int insertWithdraw(Withdraw withdraw);

    /**
     * 修改提现管理
     * 
     * @param withdraw 提现管理
     * @return 结果
     */
    public int updateWithdraw(Withdraw withdraw);

    /**
     * 删除提现管理
     * 
     * @param id 提现管理主键
     * @return 结果
     */
    public int deleteWithdrawById(Long id);

    /**
     * 批量删除提现管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWithdrawByIds(String[] ids);
}
