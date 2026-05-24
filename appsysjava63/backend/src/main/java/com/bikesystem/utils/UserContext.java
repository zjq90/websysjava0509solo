package com.bikesystem.utils;

/**
 * 用户上下文工具类
 * 用于在当前线程中存储和获取用户信息
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> PHONE_HOLDER = new ThreadLocal<>();

    /**
     * 设置用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 设置手机号
     */
    public static void setPhone(String phone) {
        PHONE_HOLDER.set(phone);
    }

    /**
     * 获取手机号
     */
    public static String getPhone() {
        return PHONE_HOLDER.get();
    }

    /**
     * 清除上下文
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
        PHONE_HOLDER.remove();
    }
}
