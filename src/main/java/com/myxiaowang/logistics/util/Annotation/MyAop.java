package com.myxiaowang.logistics.util.Annotation;

/*
  @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月25日 17:35:00
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Myxiaowang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAop {
    String module();
}
