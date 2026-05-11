package com.medical.registration.common;

public enum ErrorCode {
    
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统错误"),
    LOGIN_ERROR(1001, "用户名或密码错误"),
    USER_NOT_EXIST(1002, "用户不存在"),
    USER_DISABLED(1003, "用户已禁用"),
    USER_ALREADY_EXIST(1004, "用户名已存在"),
    PHONE_ALREADY_EXIST(1005, "手机号已注册"),
    TOKEN_INVALID(1006, "Token无效或已过期"),
    SCHEDULE_NOT_AVAILABLE(2001, "号源不足"),
    REGISTRATION_ALREADY_EXIST(2002, "已预约该号源"),
    REGISTRATION_NOT_EXIST(2003, "挂号记录不存在"),
    CANCEL_TIME_EXPIRED(2004, "取消时间已过，请联系客服处理"),
    INVALID_STATUS(2005, "状态无效"),
    PAYMENT_ERROR(3001, "支付失败"),
    NETWORK_ERROR(9001, "网络异常，请稍后重试");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
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
