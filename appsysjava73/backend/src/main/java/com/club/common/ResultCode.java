package com.club.common;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author club-management
 * @version 1.0.0
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "权限不足，禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    LOGIN_ERROR(1001, "用户名或密码错误"),
    USER_NOT_EXIST(1002, "用户不存在"),
    USER_ALREADY_EXIST(1003, "用户已存在"),
    TOKEN_INVALID(1004, "Token无效或已过期"),
    TOKEN_EMPTY(1005, "Token不能为空"),

    CLUB_NOT_EXIST(2001, "社团不存在"),
    CLUB_NAME_EXIST(2002, "社团名称已存在"),
    FOLLOW_ALREADY(2003, "已关注该社团"),
    FOLLOW_NOT_EXIST(2004, "未关注该社团"),

    RECRUIT_NOT_EXIST(3001, "招新信息不存在"),
    RECRUIT_CLOSED(3002, "招新通道已关闭"),
    RECRUIT_FULL(3003, "招新名额已满"),
    APPLY_ALREADY(3004, "已提交过申请"),
    APPLY_NOT_EXIST(3005, "申请不存在"),

    MEMBER_NOT_EXIST(4001, "成员不存在"),
    PERMISSION_DENIED(4002, "无权限执行此操作"),
    DEPARTMENT_NOT_EXIST(4003, "部门不存在"),

    FILE_UPLOAD_ERROR(5001, "文件上传失败"),
    FILE_NOT_EXIST(5002, "文件不存在"),
    FILE_TOO_LARGE(5003, "文件大小超出限制");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
