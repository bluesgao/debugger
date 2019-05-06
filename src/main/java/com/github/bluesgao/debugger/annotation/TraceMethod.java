package com.github.bluesgao.debugger.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TraceMethod {
    String code() default "";//返回码

    String codeDesc() default "";//返回码描述

    String input() default "";//方法入参

    String output() default "";//方法出参
}
