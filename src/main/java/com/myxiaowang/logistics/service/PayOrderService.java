package com.myxiaowang.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myxiaowang.logistics.pojo.PayOrder;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 16:15:00
 */

public interface PayOrderService extends IService<PayOrder> {

    /**
     * 支付接口
     * @param payOrder 支付参数
     * @return 返回结果
     */
    ResponseResult<String> onlinePay(PayOrder payOrder);
}
