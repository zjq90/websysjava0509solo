package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.User;
import com.heritage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public Result<User> login(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "密码") @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return Result.success("登录成功", user);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public Result<User> register(@RequestBody User user) {
        try {
            User registered = userService.register(user);
            return Result.success("注册成功", registered);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public Result<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户信息", description = "更新用户信息")
    public Result<User> updateUser(@RequestBody User user) {
        try {
            User updated = userService.update(user);
            return Result.success("更新成功", updated);
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/experts")
    @Operation(summary = "获取专家列表", description = "获取所有认证专家")
    public Result<List<User>> getExperts() {
        List<User> experts = userService.findAllExperts();
        return Result.success(experts);
    }

    @PostMapping("/elder-mode")
    @Operation(summary = "切换长辈模式", description = "开启或关闭长辈模式")
    public Result<Void> toggleElderMode(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "是否开启") @RequestParam boolean enable) {
        boolean success = userService.toggleElderMode(userId, enable);
        if (success) {
            return Result.success(enable ? "已开启长辈模式" : "已关闭长辈模式", null);
        }
        return Result.error("设置失败");
    }
}
