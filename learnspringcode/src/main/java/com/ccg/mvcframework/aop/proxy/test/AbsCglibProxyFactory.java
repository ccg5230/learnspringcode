package com.ccg.mvcframework.aop.proxy.test;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @className AbsMethodAdvance
 * @Description Cglib抽象代理类（在创建代理对象的同时拦截方法的执行实现before和after逻辑）
 * <P>Cglib 动态代理是针对代理的类, 动态生成一个子类, 然后子类覆盖代理类中的方法,
 * 如果是private或是final类修饰的方法,则不会被重写。</P>
 * @Author chungaochen
 * Date 2020/4/13 17:20
 * Version 1.0
 **/
public abstract class AbsCglibProxyFactory implements MethodInterceptor {
    /**
     * 要被代理的对象
     */
    protected Object target;

    /**
     * 给目标对象创建一个代理对象
     */
    public Object createProxyObject(Object target){
        this.target = target;
        //该类用于生成代理对象
        Enhancer enhancer = new Enhancer();
        //设置目标类为代理对象的父类
        enhancer.setSuperclass(this.target.getClass());
        //设置代理人为本身
        enhancer.setCallback(this);
        return enhancer.create();
    }
    public abstract void doBefore();

    public abstract void doAfter();

    public abstract void doAround();

}
