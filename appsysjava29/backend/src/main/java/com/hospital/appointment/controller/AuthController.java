package com.hospital.appointment.controller;

import com.hospital.appointment.common.Result;
import com.hospital.appointment.entity.User;
import com.hospital.appointment.security.UserPrincipal;
import com.hospital.appointment.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户登录、注册、信息管理接口")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return Result.success(authService.login(username, password));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<User> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String realName = params.get("realName");
        String phone = params.get("phone");
        return Result.success(authService.register(username, password, realName, phone));
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息")
    public Result<User> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(authService.getUserInfo(userPrincipal.getUserId()));
    }

    @PutMapping("/me")
    @Operation(summary = "更新用户信息")
    public Result<User> updateUserInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody User user) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(authService.updateUserInfo(userPrincipal.getUserId(), user));
    }

    @PostMapping("/elder-mode")
    @Operation(summary = "切换长辈模式")
    public Result<User> toggleElderMode(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Map<String, Integer> params) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        Integer elderMode = params.get("elderMode");
        return Result.success(authService.toggleElderMode(userPrincipal.getUserId(), elderMode));
    }

    @PostMapping("/password")
    @Operation(summary = "修改密码")
    public Result<Boolean> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Map<String, String> params) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        return Result.success(authService.changePassword(userPrincipal.getUserId(), oldPassword, newPassword));
    }
}
