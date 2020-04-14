package com.ccg.mvcframework.helper;

import com.ccg.bean.Handler;
import com.ccg.bean.Request;
import com.ccg.mvcframework.ioc.annotation.RequestMapping;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *@className ControllerHelper
 *@Description
 *@Author chungaochen
 *Date 2020/4/14 16:38
 *Version 1.0
 **/
public final class ControllerHelper {

    /**
     * REQUEST_MAP为 "请求-处理器" 的映射
     */
    private static final Map<Request, Handler> REQUEST_MAP = new HashMap<>();

    static {
        //遍历所有Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtils.isNotEmpty(controllerClassSet)) {
            for (Class<?> controllerClass : controllerClassSet) {
                String baseUrl = "";
                //判断类是否带RequestMapping注解
                if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping classMapping = controllerClass.getAnnotation((RequestMapping.class));
                    baseUrl = classMapping.value();
                }
                //暴力反射获取所有方法：包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。当然也包括它所实现接口的方法。
                Method[] methods = controllerClass.getDeclaredMethods();
                //遍历方法
                if (ArrayUtils.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        //判断是方法否带RequestMapping注解
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            //请求路径
                            String requestPath = requestMapping.value();
                            requestPath = ("/" + baseUrl + "/" + requestPath).replaceAll("/+", "/");
                            //请求类型 GET POST等
                            String requestMethod = requestMapping.method().name();

                            //封装请求和处理器
                            Request request = new Request(requestMethod, requestPath);
                            Handler handler = new Handler(controllerClass, method);
                            REQUEST_MAP.put(request, handler);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return REQUEST_MAP.get(request);
    }
}