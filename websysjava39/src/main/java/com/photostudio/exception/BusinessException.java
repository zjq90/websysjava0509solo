package com.photostudio.exception;

/**
 * 业务异常类
 * 当业务逻辑出现错误时抛出此异常
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
