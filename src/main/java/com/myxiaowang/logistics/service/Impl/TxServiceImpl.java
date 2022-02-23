package com.myxiaowang.logistics.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.dao.TxMapper;
import com.myxiaowang.logistics.pojo.Tx;
import com.myxiaowang.logistics.service.TxService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.stereotype.Service;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:22:00
 */
@Service
public class TxServiceImpl extends ServiceImpl<TxMapper, Tx> implements TxService {
    @Override
    public ResponseResult<String> insertTx(Tx tx) {
        save(tx);
        return ResponseResult.success("操作成功");
    }
}
