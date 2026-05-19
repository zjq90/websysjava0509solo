package com.petclinic.config;

import com.petclinic.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 处理内容审核等业务异常
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理内容审核不通过的异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        String message = e.getMessage();
        if (message != null && message.contains("内容审核不通过")) {
            return Result.error(403, message);
        }
        return Result.error(message);
    }
}
