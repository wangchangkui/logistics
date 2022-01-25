package com.myxiaowang.logistics.aop;

import org.springframework.stereotype.Component;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月25日 09:04:00
 */
@Component
public class OrderAop {

    public void preOrder(){
        System.out.println("在创建订单之前");
    }
}
