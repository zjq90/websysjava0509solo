package com.websys.controller;

import com.websys.common.Result;
import com.websys.dto.LoginRequest;
import com.websys.dto.LoginResponse;
import com.websys.entity.User;
import com.websys.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * 
 * @author websys
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * 
     * @param request 登录请求
     * @param httpRequest HTTP请求
     * @return 登录响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取JWT令牌")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest request, 
                                        HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        LoginResponse response = authService.login(request, ipAddress);
        return Result.success(response);
    }

    /**
     * 获取当前用户信息
     * 
     * @param userDetails 用户详情
     * @return 用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return Result.error("用户未登录");
        }
        User user = authService.getCurrentUser(userDetails.getUsername());
        return Result.success(user);
    }
}
