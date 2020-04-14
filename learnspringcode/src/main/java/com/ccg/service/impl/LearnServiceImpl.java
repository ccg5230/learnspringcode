package com.ccg.service.impl;

import com.ccg.mvcframework.ioc.annotation.Service;
import com.ccg.service.ILearnService;

/**
 * @className LearnServiceImpl
 * @Description
 * @Author chungaochen
 * Date 2020/4/4 14:48
 * Version 1.0
 **/
@Service
public class LearnServiceImpl implements ILearnService {
    @Override
    public String getName(String name) {
        return "Welcome My MVC, " +name;
    }
}