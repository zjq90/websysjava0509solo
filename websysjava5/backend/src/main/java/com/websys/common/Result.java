package com.websys.common;

import lombok.Data;

/**
 * 统一响应结果封装类
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
public class Result<T> {
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     * 
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 失败响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（使用默认错误码）
     * 
     * @param message 错误消息
     * @return 失败响应结果
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
}
