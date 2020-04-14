package com.ccg.mvcframework.helper;

import com.ccg.util.ClassLoaderUtil;

/**
 * @className HelperLoader
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 22:28
 * Version 1.0
 **/
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassLoaderUtil.loadClass(cls.getName());
        }
    }
}
