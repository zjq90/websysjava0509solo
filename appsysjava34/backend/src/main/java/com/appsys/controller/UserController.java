package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.User;
import com.appsys.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @Operation(summary = "获取用户列表")
    public Result<List<User>> list() {
        return Result.success(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户")
    public Result<User> getById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(Result::success).orElseGet(() -> Result.error("用户不存在"));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<User> register(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return Result.error("用户名已存在");
        }
        return Result.success(userService.save(user));
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<User> login(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        User user = userService.login(username, password);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户名或密码错误");
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户信息")
    public Result<User> update(@RequestBody User user) {
        return Result.success(userService.update(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/{id}/elder-mode")
    @Operation(summary = "切换长辈模式")
    public Result<User> toggleElderMode(@PathVariable Long id) {
        User user = userService.toggleElderMode(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }
}
