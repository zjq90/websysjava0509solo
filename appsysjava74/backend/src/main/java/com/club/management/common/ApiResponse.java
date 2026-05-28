package com.club.management.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通用API响应类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "通用API响应")
public class ApiResponse<T> {

    @Schema(description = "响应码", example = "200")
    private Integer code;

    @Schema(description = "响应消息", example = "操作成功")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应时间")
    private LocalDateTime timestamp;

    @Schema(description = "是否成功", example = "true")
    private Boolean success;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("操作成功");
        response.setSuccess(true);
        return response;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = success();
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = success(data);
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }

    public static <T> ApiResponse<T> error(Integer code, String message, T data) {
        ApiResponse<T> response = error(code, message);
        response.setData(data);
        return response;
    }
}
