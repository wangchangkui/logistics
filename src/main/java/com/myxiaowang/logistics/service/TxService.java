package com.myxiaowang.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myxiaowang.logistics.pojo.Tx;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:21:00
 */
public interface TxService extends IService<Tx> {
    /**
     * 新增提现接口
     * @param tx 提现
     * @return 返回今儿个
     */
    ResponseResult<String> insertTx(Tx tx);
}
