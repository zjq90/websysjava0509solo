package com.medical.registration.controller;

import com.medical.registration.common.Result;
import com.medical.registration.dto.LoginDTO;
import com.medical.registration.dto.LoginVO;
import com.medical.registration.dto.RegisterDTO;
import com.medical.registration.entity.User;
import com.medical.registration.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }
    
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.success(authService.register(dto));
    }
    
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        User user = authService.getUserById(userId);
        user.setPassword(null);
        user.setIdCard(user.getIdCard() != null ? "已加密" : null);
        user.setPhone(user.getPhone() != null ? "已加密" : null);
        return Result.success(user);
    }
    
    @Operation(summary = "更新用户信息")
    @PutMapping("/me")
    public Result<Void> updateUserInfo(@RequestBody RegisterDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        authService.updateUserInfo(userId, dto);
        return Result.success();
    }
}
