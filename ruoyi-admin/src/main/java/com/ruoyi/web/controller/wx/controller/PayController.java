package com.ruoyi.web.controller.wx.controller;

import com.alipay.api.AlipayApiException;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.PayOrderQuery;
import com.ruoyi.system.domain.PayOrder;
import com.ruoyi.system.service.wx.WxPayOrderService;
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
@RequestMapping("/wx/pay")
public class PayController {
    @Autowired
    private WxPayOrderService payOrderService;


    @GetMapping("/pay/onlineOrder/{userId}")
    public ResponseResult<PayOrder> hasOnlineOrder(@PathVariable String userId){
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
