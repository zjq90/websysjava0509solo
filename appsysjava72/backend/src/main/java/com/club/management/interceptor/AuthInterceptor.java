package com.club.management.interceptor;

import com.club.management.common.ResultCode;
import com.club.management.exception.BusinessException;
import com.club.management.utils.JwtUtil;
import com.club.management.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader(jwtUtil.getHeader());
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || token.isEmpty()) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);

        if (userId == null || username == null) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        UserContext.setUserId(userId);
        UserContext.setUsername(username);

        log.debug("认证成功，用户ID: {}, 用户名: {}", userId, username);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
