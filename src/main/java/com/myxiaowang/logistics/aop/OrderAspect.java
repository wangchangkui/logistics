package com.myxiaowang.logistics.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 *
 * Aop执行方法前
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月25日 17:37:00
 */
@Aspect
@Component
public class OrderAspect {

    @Pointcut("@annotation(com.myxiaowang.logistics.util.Annotation.MyAop)")
    private void cutMethod(){

    }

    @Before("cutMethod()")
    public void before(){
        System.out.println("我在插入订单之前事情");
    }

    @After("cutMethod()")
    public void after(){
        System.out.println("我在插入订单之后的事情");
    }

    /**
     * 这个地方才是织入点
     * @param proceedingJoinPoint 植入点
     * @return 返回
     * @throws Throwable 异常
     */
    @Around("cutMethod()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      return  proceedingJoinPoint.proceed();
    }



}
