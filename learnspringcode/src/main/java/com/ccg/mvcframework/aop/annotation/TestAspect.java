package com.ccg.mvcframework.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@className TestAspect
 *@Description
 *@Author chungaochen
 *Date 2020/4/14 14:41
 *Version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestAspect {

    String value() default "";
}