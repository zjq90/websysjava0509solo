package com.music.platform.controller;

import com.music.platform.common.Result;
import com.music.platform.entity.User;
import com.music.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Result<User> register(@RequestBody Map<String, String> body) {
        try {
            User user = userService.register(
                    body.get("username"),
                    body.get("password"),
                    body.get("email"),
                    body.get("nickname")
            );
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> body) {
        return userService.login(body.get("username"), body.get("password"))
                .map(user -> {
                    user.setPassword(null);
                    return Result.success(user);
                })
                .orElse(Result.error(401, "Invalid username or password"));
    }

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> {
                    user.setPassword(null);
                    return Result.success(user);
                })
                .orElse(Result.error(404, "User not found"));
    }

    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updated = userService.update(user);
        updated.setPassword(null);
        return Result.success(updated);
    }
}
