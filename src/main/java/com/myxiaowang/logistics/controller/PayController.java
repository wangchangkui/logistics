package com.myxiaowang.logistics.controller;

import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.service.PayOrderService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 16:58:00
 */
@RestController
@RequestMapping("pay")
public class PayController {
    @Autowired
    private PayOrderService payOrderService;

    @PostMapping("/onlinePay")
    public ResponseResult<String> onlinePay(@RequestBody PayOrder payOrder){
        return payOrderService.onlinePay(payOrder);
    }
}
