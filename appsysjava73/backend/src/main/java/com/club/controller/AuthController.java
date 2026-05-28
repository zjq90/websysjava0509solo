package com.club.controller;

import com.club.common.Result;
import com.club.dto.LoginDTO;
import com.club.dto.LoginResultDTO;
import com.club.dto.RegisterDTO;
import com.club.entity.User;
import com.club.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户登录、注册、认证相关接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户使用用户名和密码登录，返回JWT token")
    public Result<LoginResultDTO> login(@Validated @RequestBody LoginDTO loginDTO) {
        LoginResultDTO result = authService.login(loginDTO);
        return Result.success(result);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public Result<User> register(@Validated @RequestBody RegisterDTO registerDTO) {
        User user = authService.register(registerDTO);
        return Result.success(user);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "根据token获取当前登录用户信息")
    public Result<User> getCurrentUser(@RequestAttribute("userId") Long userId) {
        User user = authService.getUserById(userId);
        return Result.success(user);
    }
}
