package com.ccg.util;

/**
 * @className StringUtils
 * @Description
 * @Author chungaochen
 * Date 2020/4/13 17:28
 * Version 1.0
 **/
public class StringUtils {
    public static boolean isNotBlank(String str){
        if(str != null && str.trim().length() > 0){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isBlank(String str){
        return !isNotBlank(str);
    }
}
