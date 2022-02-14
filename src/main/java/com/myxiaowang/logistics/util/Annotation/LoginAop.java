package com.myxiaowang.logistics.util.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月11日 16:03:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAop {
    String login();
}
