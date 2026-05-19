package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.User;
import com.pethospital.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户登录、医生信息等接口")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<User> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        Optional<User> user = userService.login(username, password);
        if (user.isPresent()) {
            return Result.success(user.get());
        }
        return Result.error("用户名或密码错误");
    }

    @GetMapping("/doctors")
    @Operation(summary = "获取所有医生列表")
    public Result<List<User>> getDoctors() {
        return Result.success(userService.getDoctors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息")
    public Result<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(Result::success).orElseGet(() -> Result.error("用户不存在"));
    }

    @GetMapping
    @Operation(summary = "获取所有用户")
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @PostMapping
    @Operation(summary = "创建用户")
    public Result<User> createUser(@RequestBody User user) {
        return Result.success(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return Result.success(userService.saveUser(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
