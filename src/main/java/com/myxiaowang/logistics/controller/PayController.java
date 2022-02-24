package com.myxiaowang.logistics.controller;

import com.alipay.api.AlipayApiException;
import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.pojo.QueryDto.PayOrderQuery;
import com.myxiaowang.logistics.service.PayOrderService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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


    @GetMapping("/pay/onlineOrder/{userId}")
    public ResponseResult<PayOrder> hasOnlineOrder( @PathVariable String userId){
        return payOrderService.hasOnlineOrder(userId);
    }


    @PostMapping("/pay/notPay")
    public ResponseResult<String> notPayOrder(@RequestBody PayOrderQuery payOrderQuery){
        return payOrderService.notPayOrder(payOrderQuery.getUserId(), payOrderQuery.getUserId());
    }

    @PostMapping("/pay/success")
    public ResponseResult<String> sucPay(@RequestBody PayOrderQuery payOrderQuery) throws AlipayApiException {
        return payOrderService.sucPay(payOrderQuery.getUserId(),payOrderQuery.getOrderId());
    }

    @GetMapping("/order/status/{orderId}")
    public ResponseResult<String> orderStatus(@PathVariable String orderId){
        return payOrderService.orderStatus(orderId);
    }

    @GetMapping("/user/{userId}")
    public  ResponseResult<List<PayOrder>> getUserOrder(@PathVariable("userId") String userId){
        return payOrderService.getUserOrder(userId);
    }

    @PostMapping("/onlinePay")
    public ResponseResult<HashMap<String, String>> onlinePay(@RequestBody PayOrder payOrder){
        return payOrderService.onlinePay(payOrder);
    }
}
