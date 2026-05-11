package com.hospital.appointment.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.appointment.repository.OperationLogRepository;
import com.hospital.appointment.security.JwtTokenUtil;
import com.hospital.appointment.security.UserPrincipal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 操作日志切面
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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("@annotation(com.hospital.appointment.annotation.OperationLog)")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.hospital.appointment.annotation.OperationLog annotation = method.getAnnotation(com.hospital.appointment.annotation.OperationLog.class);
        
        com.hospital.appointment.entity.OperationLog log = new com.hospital.appointment.entity.OperationLog();
        log.setModule(annotation.module());
        log.setOperation(annotation.operation());
        log.setMethod(method.getName());
        
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                try {
                    StringBuilder params = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] != null && !isSensitiveParam(args[i])) {
                            if (i > 0) params.append(", ");
                            params.append(args[i].getClass().getSimpleName())
                                  .append(": ")
                                  .append(objectMapper.writeValueAsString(args[i]));
                        }
                    }
                    if (params.length() > 1000) {
                        log.setRequestParams(params.substring(0, 1000) + "...");
                    } else {
                        log.setRequestParams(params.toString());
                    }
                } catch (Exception e) {
                    log.setRequestParams("参数序列化失败");
                }
            }
        } catch (Exception e) {
            log.setRequestParams("获取参数失败");
        }
        
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                log.setRequestUri(request.getRequestURI());
                log.setIp(getClientIp(request));
                log.setUserAgent(request.getHeader("User-Agent"));
                
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    if (jwtTokenUtil.validateToken(token)) {
                        log.setUserId(jwtTokenUtil.getUserIdFromToken(token));
                        log.setUsername(jwtTokenUtil.getUsernameFromToken(token));
                    }
                }
                
                if (log.getUserId() == null) {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
                        UserPrincipal user = (UserPrincipal) auth.getPrincipal();
                        log.setUserId(user.getUserId());
                        log.setUsername(user.getUsername());
                    }
                }
            }
        } catch (Exception e) {
            log.setErrorMsg("获取请求信息失败: " + e.getMessage());
        }
        
        Object result;
        try {
            result = joinPoint.proceed();
            log.setStatus(1);
        } catch (Throwable e) {
            log.setStatus(0);
            log.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            try {
                operationLogRepository.save(log);
            } catch (Exception e) {
                System.err.println("保存操作日志失败: " + e.getMessage());
            }
        }
        
        return result;
    }

    private boolean isSensitiveParam(Object arg) {
        return arg instanceof String && (
            arg.toString().contains("password") ||
            arg.toString().contains("token") ||
            arg.toString().contains("secret")
        );
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
