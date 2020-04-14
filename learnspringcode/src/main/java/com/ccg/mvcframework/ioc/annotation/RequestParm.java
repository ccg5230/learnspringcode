package com.ccg.mvcframework.ioc.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParm {
    String value() default "";

    boolean required() default false;
}
