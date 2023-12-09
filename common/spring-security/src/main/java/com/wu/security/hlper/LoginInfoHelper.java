package com.wu.security.hlper;

/**
 * @Classname LoginInfoHelper
 * @Description
 * @Date 2023/6/5 14:58
 * @Created by cc
 */
public class LoginInfoHelper {

    private  static ThreadLocal<Long> userId=new ThreadLocal<Long>();
    private static  ThreadLocal<String> userName=new ThreadLocal<String>();

    public static Long getUserId() {
        return LoginInfoHelper.userId.get();
    }
    public static void removeUserId() {
        userId.remove();
    }
    public static void setUsername(String _username) {
        userName.set(_username);
    }
    public static String getUsername() {
        return userName.get();
    }
    public static void removeUsername() {
        userName.remove();
    }

    public static void setUserId(Long _userId) {
        userId.set(_userId);
    }
}
