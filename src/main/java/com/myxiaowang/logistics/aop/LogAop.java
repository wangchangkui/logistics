package com.myxiaowang.logistics.aop;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月22日 10:28:00
 */

@Component
@Aspect
public class LogAop {
    private final Logger logger= LoggerFactory.getLogger(LogAop.class);


}
