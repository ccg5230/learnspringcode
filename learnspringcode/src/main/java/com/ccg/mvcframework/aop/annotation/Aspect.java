package com.ccg.mvcframework.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {
    /**
     * 包名
     */
    String pkg() default "";

    /**
     * 类名
     */
    String cls() default "";
}
