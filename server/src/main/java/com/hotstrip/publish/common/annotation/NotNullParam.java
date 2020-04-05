package com.hotstrip.publish.common.annotation;

import java.lang.annotation.*;

/**
 * Created by idiot on 2017/6/27.
 * @description 非空参数校验
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface NotNullParam {
    // 非空校验参数
    String[] params() default {};
}
