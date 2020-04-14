package com.ccg.mvcframework.helper;

/**
 * @className BeanHelper
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 23:41
 * Version 1.0
 **/

import com.ccg.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean 助手类：持有IOC容器
 */
public final class BeanHelper {

    /**
     * BEAN_MAP相当于一个bean容器, 拥有项目所有bean的实例
     * key--目标增强Class
     * value--目标类被代理类Class<?>或者其实例
     * AopHelper依赖BeanHelper，BenaHelper加载完，AopHelper调用BeanHelper.setBean
     * 更新value为其代理类
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        //获取所有bean
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        //将bean实例化, 并放入bean容器中
        for (Class<?> beanClass : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, obj);
        }
    }

    /**
     * 获取 Bean 容器
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置 Bean 实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }
}