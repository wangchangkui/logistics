package com.myxiaowang.logistics.controller;

import com.myxiaowang.logistics.pojo.Tx;
import com.myxiaowang.logistics.service.TxService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:24:00
 */
@RestController
@RequestMapping("/tx")
public class TxController {
    @Autowired
    private TxService txService;
    @PostMapping("/insertTx")
    public ResponseResult<String> insertTx(@RequestBody Tx tx){
        return txService.insertTx(tx);
    }
}
