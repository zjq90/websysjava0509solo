package com.club.management.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 操作日志切面
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Pointcut("execution(* com.club.management.controller..*.*(..))")
    public void operationLogPointcut() {
    }

    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        String requestUri = request != null ? request.getRequestURI() : "";
        String requestMethod = request != null ? request.getMethod() : "";
        String clientIp = request != null ? getClientIp(request) : "";

        Object[] args = joinPoint.getArgs();
        String argsStr = Arrays.toString(args);
        if (argsStr.length() > 1000) {
            argsStr = argsStr.substring(0, 1000) + "...";
        }

        log.info("【请求开始】URI: {}, Method: {}, IP: {}, {}.{}, 参数: {}",
                requestUri, requestMethod, clientIp, className, methodName, argsStr);

        Object result = null;
        try {
            result = joinPoint.proceed();
            long costTime = System.currentTimeMillis() - startTime;

            String resultStr = result != null ? result.toString() : "null";
            if (resultStr.length() > 1000) {
                resultStr = resultStr.substring(0, 1000) + "...";
            }

            log.info("【请求结束】URI: {}, {}.{}, 耗时: {}ms, 返回: {}",
                    requestUri, className, methodName, costTime, resultStr);

            return result;
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("【请求异常】URI: {}, {}.{}, 耗时: {}ms, 异常: {}",
                    requestUri, className, methodName, costTime, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
