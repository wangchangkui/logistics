package com.ruoyi.system.plus.mapper.wx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.Withdraw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author Jandmla
 * @version 1.0.0
 * @Description TODO
 * @data 2022/3/15 14:46
 */
@Mapper
public interface TxMapper extends BaseMapper<Withdraw> {
    /**
     * 获取用户的提现信息
     * @param txPage 分页信息
     * @param userId 用户ID
     * @return 提现列表
     */
    Page<Withdraw> getUserTx(@Param("page") Page<Withdraw> txPage,@Param("userId") String userId);
}
