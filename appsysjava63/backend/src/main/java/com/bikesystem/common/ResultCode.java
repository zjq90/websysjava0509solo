package com.bikesystem.common;

import lombok.Getter;

/**
 * 响应状态码枚举
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "账号已被禁用"),
    NOT_LOGIN(1005, "请先登录"),
    TOKEN_INVALID(1006, "Token无效或已过期"),
    REAL_NAME_NOT_VERIFIED(1007, "请先完成实名认证"),
    DEPOSIT_NOT_PAID(1008, "请先缴纳押金"),
    
    BIKE_NOT_FOUND(2001, "车辆不存在"),
    BIKE_NOT_AVAILABLE(2002, "车辆暂不可用"),
    BIKE_IN_USE(2003, "车辆正在使用中"),
    BIKE_RESERVED(2004, "车辆已被预约"),
    BIKE_FAULTY(2005, "车辆故障"),
    
    RIDE_NOT_ONGOING(3001, "没有进行中的骑行"),
    RIDE_ALREADY_ONGOING(3002, "已有进行中的骑行"),
    
    RESERVATION_EXPIRED(4001, "预约已过期"),
    RESERVATION_CANCELLED(4002, "预约已取消"),
    ALREADY_HAS_RESERVATION(4003, "您已有一个有效预约"),
    
    BALANCE_NOT_ENOUGH(5001, "余额不足"),
    PAYMENT_FAILED(5002, "支付失败"),
    COUPON_NOT_AVAILABLE(5003, "优惠券不可用");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
