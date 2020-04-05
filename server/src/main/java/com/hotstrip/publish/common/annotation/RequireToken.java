package com.hotstrip.publish.common.annotation;

import java.lang.annotation.*;

/**
 * Created by idiot on 2018/2/5.
 * 注解  标识是否需要token验证
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface RequireToken {
    // 是否需要token验证
    boolean value() default true;
}
