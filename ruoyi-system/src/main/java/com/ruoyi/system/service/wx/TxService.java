package com.ruoyi.system.service.wx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.QueryDto;
import com.ruoyi.system.domain.Withdraw;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:21:00
 */
public interface TxService extends IService<Withdraw> {
    /**
     * 新增提现接口
     * @param tx 提现
     * @return 返回今儿个
     */
    ResponseResult<String> insertTx(Withdraw tx);

    /**
     * 提现查询接口
     *
     * @param queryDto
     * @return
     */
    IPage<Withdraw> getTxPage(QueryDto<Withdraw,Withdraw> queryDto);
}
