package com.ccg.mvcframework.aop.proxy.test;

import com.ccg.util.StringUtils;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @className TestAspect
 * @Description Cglib具体代理类
 * @Author chungaochen
 * Date 2020/4/13 17:56
 * Version 1.0
 **/
public class SpecificCglibProxyFactory extends AbsCglibProxyFactory {

    /**
     * 被代理的方法名
     */
    private String proxyMethodName;

    public SpecificCglibProxyFactory(Object target) {
        this.target = target;
    }

    @Override
    public void doBefore() {
        System.out.println("do before");
    }

    @Override
    public void doAfter() {
        System.out.println("do after");
    }

    @Override
    public void doAround() {

    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result;
        String proxyMethod = getProxyMethodName();
        if(StringUtils.isNotBlank(proxyMethod) && proxyMethod.equals(method.getName())){
            doBefore();
        }
        //执行拦截的方法
        result = methodProxy.invokeSuper(proxy, args);
        if(StringUtils.isNotBlank(proxyMethod) && proxyMethod.equals(method.getName())){
            doAfter();
        }
        return result;
    }

    public String getProxyMethodName(){
        return proxyMethodName;
    }
    public void setProxyMethodName(String proxyMethodName){
        this.proxyMethodName = proxyMethodName;
    }
}
