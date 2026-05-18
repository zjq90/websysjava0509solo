package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.LoginRequest;
import com.flowerstore.backend.dto.LoginResponse;
import com.flowerstore.backend.dto.RegisterRequest;
import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.User;
import com.flowerstore.backend.service.UserService;
import com.flowerstore.backend.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口", description = "用户注册、登录、获取公钥等")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 获取RSA公钥
     */
    @GetMapping("/public-key")
    @Operation(summary = "获取RSA公钥", description = "用于加密敏感数据传输")
    public Result<String> getPublicKey() {
        return userService.getRSAPublicKey();
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/send-sms-code")
    @Operation(summary = "发送短信验证码", description = "支持登录、注册、重置密码等场景")
    public Result<String> sendSmsCode(@RequestParam String phone, @RequestParam(defaultValue = "login") String type) {
        return userService.sendSmsCode(phone, type);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "支持密码登录、短信验证码登录、微信登录")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "支持手机号注册、邮箱注册、微信注册")
    public Result<User> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user-info")
    @Operation(summary = "获取当前用户信息", description = "需要登录")
    public Result<User> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return userService.getUserInfo(userId);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/user-info")
    @Operation(summary = "更新用户信息", description = "需要登录")
    public Result<User> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return userService.updateUserInfo(userId, user);
    }

    /**
     * 切换长辈模式
     */
    @PostMapping("/toggle-elder-mode")
    @Operation(summary = "切换长辈模式", description = "开启或关闭长辈模式")
    public Result<User> toggleElderMode(@RequestParam Integer elderMode, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return userService.toggleElderMode(userId, elderMode);
    }
}
