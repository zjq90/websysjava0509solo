package com.chatsystem.controller;

import com.chatsystem.common.Result;
import com.chatsystem.dto.LoginDTO;
import com.chatsystem.dto.RegisterDTO;
import com.chatsystem.entity.User;
import com.chatsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户注册、登录、信息管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public Result<User> register(@Validated @RequestBody RegisterDTO registerDTO) {
        try {
            User user = userService.register(registerDTO);
            user.setPassword(null);
            return Result.success("注册成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户使用账号密码登录")
    public Result<User> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            User user = userService.login(loginDTO);
            user.setPassword(null);
            return Result.success("登录成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout/{userId}")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public Result<Void> logout(@PathVariable Long userId) {
        try {
            userService.logout(userId);
            return Result.success("登出成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public Result<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有用户", description = "获取系统所有用户列表")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(user -> user.setPassword(null));
        return Result.success(users);
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    @Operation(summary = "搜索用户", description = "根据昵称搜索用户")
    public Result<List<User>> searchUsers(@RequestParam String nickname) {
        List<User> users = userService.searchUsers(nickname);
        users.forEach(user -> user.setPassword(null));
        return Result.success(users);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    @Operation(summary = "更新用户信息", description = "更新用户的昵称、头像等信息")
    public Result<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(userId, user);
            updatedUser.setPassword(null);
            return Result.success("更新成功", updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
