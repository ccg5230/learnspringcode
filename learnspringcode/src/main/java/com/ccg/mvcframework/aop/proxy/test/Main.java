package com.ccg.mvcframework;

import com.ccg.mvcframework.aop.annotation.Pointcut;
import com.ccg.mvcframework.aop.annotation.TestAspect;
import com.ccg.mvcframework.aop.proxy.test.SpecificCglibProxyFactory;
import com.ccg.mvcframework.aop.proxy.test.target.CglibProxyTarget;
import com.ccg.util.ClassLoaderUtil;
import com.ccg.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className Main
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 18:37
 * Version 1.0
 **/
public class Main {
    public static void main(String[] args){
        //模拟容器初始化
        //生成代理对象，getSimpleName()为该类名的全小写
        CglibProxyTarget target = (CglibProxyTarget)proxyBeanMap.get("cglibproxytarget");
        target.doSomeThing();
        System.out.println("------------");
        target.doWithNotProxy();
    }


    /**
     * 存放被代理类的切面扫描包名
     */
    public static final ConcurrentHashMap<String, Object> proxyBeanMap = new ConcurrentHashMap<>();

    static {
        initAopBeanMap("com.ccg.mvcframework.aop.proxy.target");
    }

    /**
     * 初始化容器
     *
     * @param basePath
     */
    public static void initAopBeanMap(String basePath) {
        try {
            Set<Class<?>> classSet = ClassLoaderUtil.getClassSet(basePath);
            for (Class clazz : classSet) {
                if (clazz.isAnnotationPresent(TestAspect.class)) {
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Pointcut.class)) {
                            //被代理的类名
                            //System.out.println(clazz.getName());//com.ccg.mvcframework.aop.proxy.target.CglibProxyTarget
                            //System.out.println(clazz.getClass().getName());//java.lang.Class
                            //被代理的方法名
                            String methodName = method.getName();

                            //根据切点 创建被代理对象
                            Object targeObj = ReflectionUtil.newInstance(clazz);
                            //根据切面类创建代理者
                            SpecificCglibProxyFactory proxyer = new SpecificCglibProxyFactory(targeObj);
                            //设置代理的方法
                            proxyer.setProxyMethodName(methodName);

                            Object object = proxyer.createProxyObject(targeObj);

                            if (object != null) {
                                proxyBeanMap.put(targeObj.getClass().getSimpleName().toLowerCase(), object);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}