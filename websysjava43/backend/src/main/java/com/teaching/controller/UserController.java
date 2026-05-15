package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.dto.LoginDTO;
import com.teaching.dto.RegisterDTO;
import com.teaching.entity.User;
import com.teaching.service.UserService;
import com.teaching.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户登录注册和个人信息管理")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    private Long getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtils.getUserIdFromToken(token);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = userService.login(loginDTO);
        return Result.success("登录成功", result);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<User> register(@Valid @RequestBody RegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        return Result.success("注册成功", user);
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public Result<User> getInfo(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        User user = userService.getById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新个人资料")
    public Result<User> updateProfile(@RequestHeader("Authorization") String token, @RequestBody User user) {
        Long userId = getUserIdFromToken(token);
        User updatedUser = userService.updateProfile(userId, user);
        updatedUser.setPassword(null);
        return Result.success("更新成功", updatedUser);
    }

    @PostMapping("/change-password")
    @Operation(summary = "修改密码")
    public Result<Void> changePassword(@RequestHeader("Authorization") String token,
                                      @RequestBody Map<String, String> params) {
        Long userId = getUserIdFromToken(token);
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success("密码修改成功");
    }

    @PostMapping("/bind-email")
    @Operation(summary = "绑定邮箱")
    public Result<User> bindEmail(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> params) {
        Long userId = getUserIdFromToken(token);
        String email = params.get("email");
        User user = userService.bindEmail(userId, email);
        user.setPassword(null);
        return Result.success("邮箱绑定成功", user);
    }

    @PostMapping("/unbind-email")
    @Operation(summary = "解绑邮箱")
    public Result<User> unbindEmail(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        User user = userService.unbindEmail(userId);
        user.setPassword(null);
        return Result.success("邮箱解绑成功", user);
    }
}
