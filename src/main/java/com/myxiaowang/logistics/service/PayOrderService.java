package com.myxiaowang.logistics.service;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import java.util.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 16:15:00
 */

public interface PayOrderService extends IService<PayOrder> {


    /**
     * 获取所有的用户支付记录
     * @param userId 用户id'
     * @return 集合
     */
    ResponseResult<List<PayOrder>> getUserOrder(String userId);

    /**
     * 查询订单的清空
     * @param orderId 订单Id
     * @return 订单的清空
     */
    ResponseResult<String> orderStatus(String orderId);
    /**
     * 支付成功截图
     * @param userId userId
     * @param orderId orderId
     * @return 操作结果
     */
    ResponseResult<String> sucPay(String userId,String orderId) throws AlipayApiException;

    /**
     * 如果到期没有支付订单
     * @param userId userId
     * @param orderId orderId
     * @return 返回结果
     */
    ResponseResult<String> notPayOrder(String userId,String orderId);

    /**
     * 获取是否存在订单 如果redis还在 那一定是存在的
     * @param userId 用户id
     * @return  订单
     */
    ResponseResult<PayOrder> hasOnlineOrder(String userId);

    /**
     * 支付接口
     * @param payOrder 支付参数
     * @return 返回结果
     */
    ResponseResult<HashMap<String, String>> onlinePay(PayOrder payOrder);
}
