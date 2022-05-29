package com.ruoyi.web.controller.wx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.PayOrderQuery;
import com.ruoyi.system.domain.Orders;
import com.ruoyi.system.domain.wx.Logistics;
import com.ruoyi.system.service.wx.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2022年01月21日 14:38:00
 */
@RestController
@RequestMapping("/wx/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 取消订单
     */
    @PostMapping("/cancelOrder")
    public ResponseResult<String> cancelOrder(@RequestBody PayOrderQuery payOrderQuery){
        return orderService.cancelOrder(payOrderQuery.getUserId(), payOrderQuery.getOrderId());
    }

    /**
     * 条件查询排序
     * @param v1 查询条件
     * @param v2 排序条件
     * @param userId 用户ID
     * @return 查询结果
     */
    @PostMapping("/conditionByUser")
    public ResponseResult<List<Orders>> getOrderByCondByUser(@RequestParam("v1") int v1, @RequestParam("v2") String v2, @RequestParam("userId") String userId){
        return orderService.getOrdersByCond(v1,v2,userId);
    }

    /**
     * 获取用户的订单
     * @param userid 用户id
     * @return 用户订单列表
     */
    @GetMapping("/getOrderUser/{userid}")
    public ResponseResult<List<Orders>> getOrderByUser(@PathVariable String userid){
        return orderService.getOrderByUser(userid);
    }


    /**
     * 获取用户以及完成的订单
     * @param userId 用户ID
     * @param type 订单类型
     * @return 订单列表
     */
    @PostMapping("/getUserOrder")
    public ResponseResult<List<Logistics>> getOrderByUser(@RequestParam("userId")String userId, @RequestParam("type")Integer type){
        return orderService.getOrderByUser(userId,type);
    }

    /**
     * 查询在线订单
     */
    @PostMapping("/condition")
    public ResponseResult<List<Orders>> getOrderByCond(@RequestParam("v1") int v1, @RequestParam("v2") String v2){
        return orderService.getOrdersByCond(v1,v2);
    }


    @PostMapping("/confirm")
    public ResponseResult<String> confirmOrder(@RequestParam("userId")String userId, @RequestParam("orderId") String orderId){
         return orderService.confirmOrder(userId,orderId);
    }

    @GetMapping("/orderList/{page}/{size}")
    public ResponseResult<IPage<Orders>> getOrderList(@PathVariable("page") int page, @PathVariable("size") int size){
        return orderService.getOrderList(page,size);
    }

    @PostMapping("/getOrder")
    public ResponseResult<String> getOrder(@RequestParam("orderId") String orderId, @RequestParam("userId") String userId, @RequestParam("address") String address){
       return orderService.getOrder(orderId,userId,address);
    }

    @PostMapping("/createOrder")
    public ResponseResult<String> createOrder(@RequestBody Orders order){
        return orderService.createOrder(order);
    }

    @PostMapping("/payMoney")
    public ResponseResult<Integer> payMoney(@RequestBody Orders orders){
        return orderService.payMoney(orders);
    }
}
