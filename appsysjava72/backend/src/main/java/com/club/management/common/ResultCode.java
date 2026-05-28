package com.club.management.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author club-management
 * @since 2024-01-01
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    LOGIN_ERROR(1001, "用户名或密码错误"),
    USER_DISABLED(1002, "账号已被禁用"),
    USER_NOT_EXIST(1003, "用户不存在"),
    USER_ALREADY_EXIST(1004, "用户已存在"),
    PHONE_ALREADY_EXIST(1005, "手机号已注册"),
    VERIFY_CODE_ERROR(1006, "验证码错误"),
    VERIFY_CODE_EXPIRED(1007, "验证码已过期"),
    TOKEN_INVALID(1008, "Token无效"),
    TOKEN_EXPIRED(1009, "Token已过期"),

    CLUB_NOT_EXIST(2001, "社团不存在"),
    CLUB_ALREADY_JOINED(2002, "已加入该社团"),
    CLUB_FULL(2003, "社团成员已满"),
    APPLICATION_ALREADY_EXIST(2004, "已提交过申请"),
    APPLICATION_NOT_EXIST(2005, "申请不存在"),

    ACTIVITY_NOT_EXIST(3001, "活动不存在"),
    ACTIVITY_NOT_STARTED(3002, "活动未开始"),
    ACTIVITY_ENDED(3003, "活动已结束"),
    ACTIVITY_FULL(3004, "活动名额已满"),
    ALREADY_SIGNED_UP(3005, "已报名该活动"),
    NOT_SIGNED_UP(3006, "未报名该活动"),
    ALREADY_SIGNED_IN(3007, "已签到"),
    SIGN_IN_TIME_ERROR(3008, "不在签到时间范围内");

    private final Integer code;
    private final String message;
}
