package com.ccg.mvcframework.aop.proxy.impl;

import com.ccg.mvcframework.aop.annotation.Transactional;
import com.ccg.mvcframework.aop.proxy.Proxy;
import com.ccg.mvcframework.aop.proxy.ProxyChain;
import com.ccg.mvcframework.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @className TransactionProxy
 * @Description
 * <P>
 *     事务切面类, 同样实现了Proxy接口, 其 doProxy() 方法就是先判断代理方法上有没有 @Transactional 注解,
 *     如果有就加上事务管理, 没有就直接执行
 * </P>
 * @Author chungaochen
 * Date 2020/4/13 23:09
 * Version 1.0
 **/
public class TransactionProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Method method = proxyChain.getTargetMethod();
        //加了@Transactional注解的方法要做事务处理
        if (method.isAnnotationPresent(Transactional.class)) {
            try {
                DatabaseHelper.beginTransaction();
                LOGGER.info("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.info("commit transaction");
            } catch (Exception e) {
                DatabaseHelper.rollbackTransaction();
                LOGGER.error("rollback transaction");
                throw e;
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
