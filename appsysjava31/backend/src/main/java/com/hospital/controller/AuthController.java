package com.hospital.controller;

import com.hospital.annotation.OperationLog;
import com.hospital.common.Result;
import com.hospital.dto.LoginDTO;
import com.hospital.dto.LoginUserVO;
import com.hospital.dto.RegisterDTO;
import com.hospital.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、注册、登出等认证相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    @OperationLog(value = "用户注册", operationType = "REGISTER", module = "USER")
    public Result<Map<String, Object>> register(@Validated @RequestBody RegisterDTO dto) {
        LoginUserVO vo = userService.register(dto);
        Map<String, Object> data = new HashMap<>();
        data.put("token", vo.getToken());
        data.put("user", vo);
        return Result.success("注册成功", data);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取Token")
    @OperationLog(value = "用户登录", operationType = "LOGIN", module = "USER")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginDTO dto) {
        LoginUserVO vo = userService.login(dto);
        Map<String, Object> data = new HashMap<>();
        data.put("token", vo.getToken());
        data.put("user", vo);
        return Result.success("登录成功", data);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<?> getCurrentUser(@AuthenticationPrincipal Long userId) {
        if (userId == null) {
            return Result.error("请先登录");
        }
        return Result.success(userService.getUserById(userId));
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    @OperationLog(value = "用户登出", operationType = "LOGOUT", module = "USER")
    public Result<?> logout() {
        return Result.success("登出成功");
    }

    /**
     * 用户登出（PUT）
     */
    @PutMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public Result<?> logoutPut() {
        return Result.success("登出成功");
    }
}
