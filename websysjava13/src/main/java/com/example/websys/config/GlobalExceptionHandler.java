package com.example.websys.config;

import com.example.websys.dto.Result;
import com.example.websys.dto.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常处理器
 * 统一处理请求校验异常，返回标准化的错误响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @RequestBody 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<List<ValidationError>>> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        List<ValidationError> errors = new ArrayList<>();
        
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ValidationError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()
            ));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("errors", errors);
        data.put("count", errors.size());

        String firstMessage = errors.isEmpty() ? "参数校验失败" : errors.get(0).getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, firstMessage));
    }

    /**
     * 处理表单数据绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<List<ValidationError>>> handleBindException(BindException ex) {
        
        List<ValidationError> errors = new ArrayList<>();
        
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ValidationError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()
            ));
        }

        String firstMessage = errors.isEmpty() ? "参数校验失败" : errors.get(0).getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, firstMessage));
    }

    /**
     * 处理 @RequestParam @PathVariable 参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<List<ValidationError>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        
        List<ValidationError> errors = new ArrayList<>();
        
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String field = violation.getPropertyPath().toString();
            if (field.contains(".")) {
                field = field.substring(field.lastIndexOf(".") + 1);
            }
            
            errors.add(new ValidationError(
                    field,
                    violation.getMessage(),
                    violation.getInvalidValue()
            ));
        }

        String firstMessage = errors.isEmpty() ? "参数校验失败" : errors.get(0).getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, firstMessage));
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> handleGenericException(Exception ex) {
        ex.printStackTrace();
        
        String message = "服务器内部错误: " + ex.getMessage();
        if (ex.getCause() != null) {
            message += " - 原因: " + ex.getCause().getMessage();
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(500, message));
    }
}
