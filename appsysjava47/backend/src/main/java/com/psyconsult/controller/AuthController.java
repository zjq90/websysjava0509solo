package com.psyconsult.controller;

import com.psyconsult.dto.ApiResponse;
import com.psyconsult.dto.LoginRequest;
import com.psyconsult.dto.RegisterRequest;
import com.psyconsult.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口", description = "用户注册、登录等认证相关接口")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "支持用户名、手机号、邮箱注册，可选择匿名模式")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        Map<String, Object> result = authService.register(request);
        return ApiResponse.success("注册成功", result);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户名密码登录，返回JWT token")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> result = authService.login(request);
        return ApiResponse.success("登录成功", result);
    }
}
