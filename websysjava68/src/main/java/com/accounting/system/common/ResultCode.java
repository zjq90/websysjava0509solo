package com.accounting.system.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    PARAM_VALIDATE_ERROR(1001, "参数校验失败"),
    DATA_NOT_EXIST(1002, "数据不存在"),
    DATA_ALREADY_EXIST(1003, "数据已存在");

    private final Integer code;
    private final String message;
}
