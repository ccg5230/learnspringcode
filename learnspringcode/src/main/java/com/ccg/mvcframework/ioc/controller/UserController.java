package com.ccg.mvcframework.ioc.controller;

import com.ccg.bean.Data;
import com.ccg.bean.Param;
import com.ccg.bean.View;
import com.ccg.domain.User;
import com.ccg.mvcframework.ioc.annotation.Autowired;
import com.ccg.mvcframework.ioc.annotation.Controller;
import com.ccg.mvcframework.ioc.annotation.RequestMapping;
import com.ccg.mvcframework.ioc.annotation.RequestMethod;
import com.ccg.service.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className UserController
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 22:32
 * Version 1.0
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 用户列表
     *
     * @return
     */
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public View getUserList() {
        List<User> userList = userService.getAllUser();
        return new View("userList.jsp").addModel("userList", userList);
    }

    /**
     * 用户详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public View getUserInfo(Param param) {
        String id = (String) param.getParamMap().get("id");
        User user = userService.GetUserInfoById(Integer.parseInt(id));
        return new View("userInfo.jsp").addModel("userinfo", user);
    }

    @RequestMapping(value = "/userEdit", method = RequestMethod.GET)
    public Data editUser(Param param) {
        String id = (String) param.getParamMap().get("id");
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("age", 911);
        userService.updateUser(Integer.parseInt(id), fieldMap);
        return new Data("Success.");
    }
}

