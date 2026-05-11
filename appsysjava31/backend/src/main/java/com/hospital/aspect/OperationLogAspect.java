package com.hospital.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.annotation.OperationLog;
import com.hospital.repository.OperationLogRepository;
import com.hospital.util.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志切面
 * 拦截@OperationLog注解的方法，记录操作日志
 * 
 * @author hospital
 * @version 1.0.0
 */
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation(com.hospital.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        com.hospital.entity.OperationLog logEntity = new com.hospital.entity.OperationLog();
        
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                logEntity.setRequestMethod(request.getMethod());
                logEntity.setRequestUrl(request.getRequestURI());
                logEntity.setIpAddress(getIpAddress(request));
                logEntity.setUserAgent(request.getHeader("User-Agent"));
                
                String token = extractToken(request);
                if (token != null && jwtUtil.validateToken(token)) {
                    logEntity.setUserId(jwtUtil.getUserIdFromToken(token));
                    logEntity.setUsername(jwtUtil.getUsernameFromToken(token));
                    logEntity.setUserRole(jwtUtil.getRoleFromToken(token));
                }
            }
            
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            com.hospital.annotation.OperationLog annotation = method.getAnnotation(com.hospital.annotation.OperationLog.class);
            
            logEntity.setOperationType(annotation.operationType());
            logEntity.setModule(annotation.module());
            logEntity.setDescription(annotation.value());
            logEntity.setOperationTime(LocalDateTime.now());
            
            try {
                Object[] args = joinPoint.getArgs();
                if (args.length > 0) {
                    logEntity.setRequestParams(objectMapper.writeValueAsString(args[0]));
                }
            } catch (Exception e) {
                logEntity.setRequestParams("参数序列化失败");
            }
            
            Object result = joinPoint.proceed();
            
            long duration = System.currentTimeMillis() - startTime;
            logEntity.setDuration(duration);
            logEntity.setResult("SUCCESS");
            
            return result;
            
        } catch (Throwable e) {
            logEntity.setResult("FAIL");
            logEntity.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            try {
                operationLogRepository.save(logEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
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
        return ip;
    }

    /**
     * 从请求中提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return request.getParameter("token");
    }
}
