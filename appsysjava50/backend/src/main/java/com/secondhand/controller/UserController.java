package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.User;
import com.secondhand.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 *
 * @author secondhand
 * @version 1.0.0
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户注册、登录、信息管理等接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public Result<User> register(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String nickname) {
        try {
            User user = userService.register(username, password, nickname);
            return Result.success("注册成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取token")
    public Result<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        try {
            Map<String, Object> result = userService.login(username, password);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "根据token获取当前用户信息")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        return Result.success(user);
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户信息", description = "更新当前用户的个人信息")
    public Result<User> updateUser(@RequestHeader("Authorization") String token, @RequestBody User user) {
        User currentUser = userService.getUserByToken(token);
        if (currentUser == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            User updatedUser = userService.updateUser(currentUser.getId(), user);
            return Result.success("更新成功", updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/sign")
    @Operation(summary = "每日签到", description = "用户每日签到获取积分")
    public Result<Integer> dailySign(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            int points = userService.dailySign(user.getId());
            return Result.success("签到成功，获得" + points + "积分", points);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/sign/status")
    @Operation(summary = "获取签到状态", description = "获取用户今日签到状态和连续签到天数")
    public Result<Map<String, Object>> getSignStatus(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        return Result.success(userService.getSignStatus(user.getId()));
    }

}
