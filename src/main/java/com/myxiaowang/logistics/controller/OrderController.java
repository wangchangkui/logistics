package com.myxiaowang.logistics.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.myxiaowang.logistics.pojo.Order;
import com.myxiaowang.logistics.service.OrderService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月21日 14:38:00
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/condition")
    public ResponseResult<List<Order>> getOrderByCond(@RequestParam("v1") int v1, @RequestParam("v2") String v2){
        return orderService.getOrdersByCond(v1,v2);
    }


    @PostMapping("/confirm")
    public ResponseResult<String> confirmOrder(@RequestParam("userId")String userId,@RequestParam("orderId") String orderId){
         return orderService.confirmOrder(userId,orderId);
    }

    @GetMapping("/orderList/{page}/{size}")
    public ResponseResult<IPage<Order>> getOrderList(@PathVariable("page") int page, @PathVariable("size") int size){
        return orderService.getOrderList(page,size);
    }

    @PostMapping("/getOrder")
    public ResponseResult<String> getOrder(@RequestParam("orderId") String orderId){
       return orderService.getOrder(orderId);
    }

    @PostMapping("/createOrder")
    public ResponseResult<String> createOrder(@RequestBody Order order){
        return orderService.createOrder(order);
    }
}
