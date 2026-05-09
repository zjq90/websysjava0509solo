package com.example.websys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 校验错误结果封装类
 * 用于返回字段级别的校验错误信息
 */
public class ValidationError implements Serializable {

    private static final long serialVersionUID = 1L;

    private String field;
    private String message;
    private Object rejectedValue;

    public ValidationError() {
    }

    public ValidationError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public static List<ValidationError> of(String field, String message) {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError(field, message, null));
        return errors;
    }
}
