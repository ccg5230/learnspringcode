package com.ccg.service.impl;

import com.ccg.domain.User;
import com.ccg.mvcframework.aop.annotation.Transactional;
import com.ccg.mvcframework.ioc.annotation.Service;
import com.ccg.service.IUserService;
import com.ccg.mvcframework.helper.DatabaseHelper;

import java.util.List;
import java.util.Map;

/**
 * @className UserServiceImpl
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 22:30
 * Version 1.0
 **/
@Service
public class UserServiceImpl implements IUserService {
    /**
     * 获取所有用户
     */
    public List<User> getAllUser() {
        String sql = "SELECT * FROM user";
        return DatabaseHelper.queryEntityList(User.class, sql);
    }

    /**
     * 根据id获取用户信息
     */
    public User GetUserInfoById(Integer id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return DatabaseHelper.queryEntity(User.class, sql, id);
    }

    /**
     * 修改用户信息
     */
    @Transactional
    public boolean updateUser(int id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(User.class, id, fieldMap);
    }
}