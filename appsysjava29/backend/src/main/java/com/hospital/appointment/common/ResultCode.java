package com.hospital.appointment.common;

/**
 * 响应状态码枚举类
 * 
 * @author hospital
 * @version 1.0.0
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    
    LOGIN_SUCCESS(200, "登录成功"),
    LOGIN_FAIL(401, "用户名或密码错误"),
    TOKEN_EXPIRED(401, "Token已过期"),
    TOKEN_INVALID(401, "无效的Token"),
    
    SLOT_NOT_AVAILABLE(1001, "号源不可用"),
    SLOT_LOCKED(1002, "号源已被锁定"),
    SLOT_BOOKED(1003, "号源已被预约"),
    LOCK_EXPIRED(1004, "锁号已过期"),
    
    PAY_SUCCESS(200, "支付成功"),
    PAY_FAIL(1010, "支付失败"),
    PAY_CANCELLED(1011, "支付已取消"),
    PAY_TIMEOUT(1012, "支付超时"),
    
    APPOINTMENT_NOT_FOUND(1020, "预约记录不存在"),
    APPOINTMENT_CANCELLED(1021, "预约已取消"),
    APPOINTMENT_ALREADY_PAID(1022, "预约已支付");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
