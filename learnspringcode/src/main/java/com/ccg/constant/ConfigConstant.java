package com.ccg.constant;

/**
 * @className ConfigConstant
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 23:52
 * Version 1.0
 **/
public interface ConfigConstant {
    //配置文件的名称
    String CONFIG_FILE = "application.properties";

    //数据库
    String JDBC_DRIVER = "framework.jdbc.driver";
    String JDBC_URL = "framework.jdbc.url";
    String JDBC_USERNAME = "framework.jdbc.username";
    String JDBC_PASSWORD = "framework.jdbc.password";

    //文件地址
    String APP_BASE_PACKAGE = "framework.app.base_package";
    String APP_JSP_PATH = "framework.app.jsp_path";
    String APP_ASSET_PATH = "framework.app.asset_path";
}
