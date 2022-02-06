package com.ming.util;

/**
 * @author ming
 * @date 2022/2/5 14:11
 */
public class UserUtil {

    private static final ThreadLocal<String> USER_NAME = new ThreadLocal<>();

    public static void setUsername(String username) {
        USER_NAME.set(username);
    }

    public static String getUsername() {
        return USER_NAME.get();
    }

    public static void removeUsername() {
        USER_NAME.remove();
    }
}
