package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.User;
import com.agriculture.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证控制器
 * 处理用户登录、注册等认证相关操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "认证管理", description = "用户登录、注册、信息获取等接口")
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @Operation(summary = "用户登录", description = "使用用户名和密码登录系统")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "密码") @RequestParam String password) {
        
        User user = userService.login(username, password);
        
        if (user != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("userId", user.getId());
            result.put("username", user.getUsername());
            result.put("realName", user.getRealName());
            result.put("role", user.getRole());
            result.put("department", user.getDepartment());
            return Result.success("登录成功", result);
        } else {
            return Result.unauthorized("用户名或密码错误");
        }
    }

    /**
     * 用户注册
     * 
     * @param user 用户信息
     * @return 注册结果
     */
    @Operation(summary = "用户注册", description = "新用户注册账号")
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        try {
            User created = userService.register(user);
            created.setPassword(null);
            return Result.success("注册成功", created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据ID获取用户详细信息")
    @GetMapping("/user/{id}")
    public Result<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(null);
            return Result.success(user);
        } else {
            return Result.notFound("用户不存在");
        }
    }

    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新结果
     */
    @Operation(summary = "更新用户信息", description = "更新用户个人信息")
    @PutMapping("/user")
    public Result<User> updateUser(@RequestBody User user) {
        try {
            User updated = userService.update(user);
            updated.setPassword(null);
            return Result.success("更新成功", updated);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
