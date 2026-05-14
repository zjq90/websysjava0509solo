package com.photostudio.common;

import lombok.Data;

/**
 * 通用响应结果类
 * 统一封装API响应数据
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
public class Result<T> {
    
    private boolean success;
    private String message;
    private T data;
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> success() {
        return new Result<>(true, "操作成功", null);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(false, message, null);
    }

    public static <T> Result<T> error() {
        return new Result<>(false, "操作失败", null);
    }
}
