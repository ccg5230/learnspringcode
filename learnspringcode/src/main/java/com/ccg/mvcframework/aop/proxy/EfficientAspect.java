package com.ccg.mvcframework.aop.proxy;

import com.ccg.mvcframework.aop.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @className EfficientAspect
 * @Description 接口性能监控切面
 * @Author chungaochen
 * Date 2020/4/13 22:36
 * Version 1.0
 **/
@Aspect(pkg = "com.ccg.mvcframework.ioc.controller", cls = "UserController")
public class EfficientAspect extends AspectProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(EfficientAspect.class);

    private long begin;

    /**
     * 切入点判断
     */
    @Override
    public boolean intercept(Method method, Object[] params) throws Throwable {
        return method.getName().equals("getUserList");
    }

    @Override
    public void before(Method method, Object[] params) throws Throwable {
        LOGGER.debug("---------- begin ----------");
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Method method, Object[] params) throws Throwable {
        LOGGER.info("execute UserController.getUserList method use" + String.format("time: %dms",
                System.currentTimeMillis() - begin +"milliseconds!"));
        LOGGER.debug("----------- end -----------");
    }
}
