package com.medical.registration.aop;

import com.medical.registration.entity.OperationLog;
import com.medical.registration.entity.User;
import com.medical.registration.repository.OperationLogRepository;
import com.medical.registration.repository.UserRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {
    
    @Autowired
    private OperationLogRepository operationLogRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Around("@annotation(com.medical.registration.aop.LogOperation)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        com.medical.registration.entity.OperationLog logEntity = new com.medical.registration.entity.OperationLog();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        LogOperation annotation = method.getAnnotation(LogOperation.class);
        if (annotation != null) {
            logEntity.setOperationType(annotation.operationType());
            logEntity.setOperationDesc(annotation.operationDesc());
            logEntity.setModule(annotation.module());
        }
        
        Long userId = getCurrentUserId();
        logEntity.setUserId(userId);
        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> logEntity.setUsername(user.getUsername()));
        }
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logEntity.setRequestUrl(request.getRequestURI());
            logEntity.setIpAddress(getClientIp(request));
        }
        
        try {
            Object[] args = joinPoint.getArgs();
            String params = Arrays.toString(args);
            if (params.length() > 500) {
                params = params.substring(0, 500) + "...";
            }
            logEntity.setRequestParams(params);
        } catch (Exception e) {
            log.error("获取参数失败", e);
        }
        
        Object result = null;
        try {
            result = joinPoint.proceed();
            logEntity.setStatus("SUCCESS");
        } catch (Throwable e) {
            logEntity.setStatus("FAILED");
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 500) {
                errorMsg = errorMsg.substring(0, 500) + "...";
            }
            logEntity.setErrorMsg(errorMsg);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logEntity.setDuration(duration);
            
            try {
                operationLogRepository.save(logEntity);
            } catch (Exception e) {
                log.error("保存操作日志失败", e);
            }
        }
        
        return result;
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            try {
                return (Long) authentication.getPrincipal();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
