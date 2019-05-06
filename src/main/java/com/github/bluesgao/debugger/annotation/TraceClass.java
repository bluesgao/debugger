package com.github.bluesgao.debugger.annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TraceClass {
    String appName() default "";//应用名称

    String serviceName() default "";//服务名称
}
