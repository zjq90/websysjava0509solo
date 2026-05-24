package com.bikesystem.interceptor;

import com.bikesystem.common.ResultCode;
import com.bikesystem.common.BusinessException;
import com.bikesystem.utils.JwtUtils;
import com.bikesystem.utils.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT认证拦截器
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ResultCode.NOT_LOGIN);
        }
        
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        if (!jwtUtils.validateToken(token)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        String phone = jwtUtils.getPhoneFromToken(token);
        
        UserContext.setUserId(userId);
        UserContext.setPhone(phone);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
