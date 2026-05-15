package com.referee.controller;

import com.referee.common.Result;
import com.referee.dto.LoginRequest;
import com.referee.entity.User;
import com.referee.repository.UserRepository;
import com.referee.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author Referee System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "登录、密码重置等认证相关接口")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户通过用户名和密码登录系统")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * 重置密码接口
     */
    @PostMapping("/reset-password/{id}")
    @Operation(summary = "重置密码", description = "管理员重置其他用户的密码")
    public Result<String> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        return authService.resetPassword(id, newPassword);
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping("/users")
    @Operation(summary = "获取所有用户", description = "获取系统所有用户列表")
    public Result<List<User>> getAllUsers() {
        return Result.success(userRepository.findAll());
    }

    /**
     * 根据角色获取用户列表
     */
    @GetMapping("/users/role/{role}")
    @Operation(summary = "根据角色获取用户", description = "根据用户角色获取用户列表")
    public Result<List<User>> getUsersByRole(
            @Parameter(description = "用户角色") @PathVariable String role) {
        return Result.success(userRepository.findByRole(role));
    }
}
