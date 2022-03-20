package com.myxiaowang.logistics.aop;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.myxiaowang.logistics.pojo.Log;
import com.myxiaowang.logistics.service.LogService;
import org.apache.poi.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月22日 10:28:00
 */

@Component
@Aspect
@Order(-1)
public class LogAop {
    @Autowired
    private LogService logService;

    @Pointcut("execution(* com.myxiaowang.logistics.controller..*.*(..))")
    public void cutPoint(){}

    @Before(value = "cutPoint()")
    public void before( JoinPoint joinPoint){
        Log log = new Log();
        // 获取请求的url
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest= (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String requestUrl = Objects.requireNonNull(httpServletRequest).getRequestURI();
        // 这里后期需要加上用户 根据token 现在方便 暂时不需要使用token
        log.setUrl(requestUrl);
        log.setMethod(joinPoint.getSignature().getName());
        log.setArgs(StringUtil.join(joinPoint.getArgs()));
        log.setRequestClass(joinPoint.getTarget().getClass().getName().substring(joinPoint.getTarget().getClass().getName().lastIndexOf(".")+1));
        logService.save(log);
    }

}
