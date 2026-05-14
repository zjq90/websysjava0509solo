package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.User;
import com.appsys.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return Result.success("登录成功", user);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        if (registeredUser != null) {
            return Result.success("注册成功", registeredUser);
        }
        return Result.error("用户名已存在");
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return Result.success("更新成功", updatedUser);
        }
        return Result.error("更新失败");
    }

    @PostMapping("/elder-mode")
    @Operation(summary = "切换长辈模式")
    public Result<Boolean> toggleElderMode(@RequestParam Long userId, @RequestParam boolean elderMode) {
        boolean result = userService.toggleElderMode(userId, elderMode);
        if (result) {
            return Result.success(elderMode ? "已开启长辈模式" : "已关闭长辈模式", true);
        }
        return Result.error("切换失败");
    }

    @GetMapping("/rsa-public-key")
    @Operation(summary = "获取RSA公钥")
    public Result<String> getRSAPublicKey() {
        return Result.success(userService.getRSAPublicKey());
    }

    @PostMapping("/encrypt")
    @Operation(summary = "RSA加密数据")
    public Result<String> encryptData(@RequestParam String data) {
        try {
            return Result.success(userService.encryptData(data));
        } catch (Exception e) {
            return Result.error("加密失败：" + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    @Operation(summary = "RSA解密数据")
    public Result<String> decryptData(@RequestParam String encryptedData) {
        try {
            return Result.success(userService.decryptData(encryptedData));
        } catch (Exception e) {
            return Result.error("解密失败：" + e.getMessage());
        }
    }
}
