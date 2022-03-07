package com.myxiaowang.logistics.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.myxiaowang.logistics.pojo.Logistics;
import com.myxiaowang.logistics.pojo.Order;
import com.myxiaowang.logistics.pojo.QueryDto.PayOrderQuery;
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


    @PostMapping("/cancelOrder")
    public ResponseResult<String> cancelOrder(@RequestBody PayOrderQuery payOrderQuery){
        return orderService.cancelOrder(payOrderQuery.getUserId(), payOrderQuery.getOrderId());
    }


    @PostMapping("/conditionByUser")
    public ResponseResult<List<Order>> getOrderByCondByUser(@RequestParam("v1") int v1, @RequestParam("v2") String v2,@RequestParam("userId") String userId){
        return orderService.getOrdersByCond(v1,v2,userId);
    }

    /**
     * 获取用户的订单
     * @param userid 用户id
     * @return 用户订单列表
     */
    @GetMapping("/getOrderUser/{userid}")
    public ResponseResult<List<Order>> getOrderByUser(@PathVariable String userid){
        return orderService.getOrderByUser(userid);
    }


    @PostMapping("/getUserOrder")
    public ResponseResult<List<Logistics>> getOrderByUser(@RequestParam("userId")String userId, @RequestParam("type")int type){
        return orderService.getOrderByUser(userId,type);
    }

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
    public ResponseResult<String> getOrder(@RequestParam("orderId") String orderId,@RequestParam("userid") String userId,@RequestParam("address") String address){
       return orderService.getOrder(orderId,userId,address);
    }

    @PostMapping("/createOrder")
    public ResponseResult<String> createOrder(@RequestBody Order order){
        return orderService.createOrder(order);
    }
}
