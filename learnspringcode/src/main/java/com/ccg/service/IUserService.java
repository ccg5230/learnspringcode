package com.ccg.service;

import com.ccg.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @className IUserService
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 22:30
 * Version 1.0
 **/
public interface IUserService {
    List<User> getAllUser();

    User GetUserInfoById(Integer id);

    boolean updateUser(int id, Map<String, Object> fieldMap);
}