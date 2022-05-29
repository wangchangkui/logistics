package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Withdraw;

/**
 * 提现管理Service接口
 * 
 * @author 23909
 * @date 2022-03-10
 */
public interface IWithdrawService {
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
     * 批量删除提现管理
     * 
     * @param ids 需要删除的提现管理主键集合
     * @return 结果
     */
    public int deleteWithdrawByIds(String ids);

    /**
     * 删除提现管理信息
     * 
     * @param id 提现管理主键
     * @return 结果
     */
    public int deleteWithdrawById(Long id);

    /**
     * 拒绝 提现 请求
     *
     * @param id 请求ID
     * @return 请求结果
     */
    int refuseOrder(Long id);

    /**
     * 同意 提现 请求
     *
     * @param id 请求ID
     * @return 请求结果过
     */
    int agreeOrder(Long id);

}
