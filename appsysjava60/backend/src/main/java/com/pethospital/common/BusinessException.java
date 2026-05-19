package com.pethospital.common;

import lombok.Getter;

/**
 * 业务异常类
 * 用于封装业务逻辑中的异常信息
 * 
 * @author Pet Hospital Team
 */
@Getter
public class BusinessException extends RuntimeException {
    
    /**
     * 错误状态码
     */
    private final Integer code;
    
    /**
     * 构造方法
     * 
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
    
    /**
     * 构造方法
     * 
     * @param code 错误状态码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
