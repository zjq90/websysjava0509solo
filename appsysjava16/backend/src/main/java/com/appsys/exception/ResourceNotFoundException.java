package com.appsys.exception;

/**
 * 资源未找到异常
 * 当查询的资源不存在时抛出
 * 
 * @author appsys-team
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
