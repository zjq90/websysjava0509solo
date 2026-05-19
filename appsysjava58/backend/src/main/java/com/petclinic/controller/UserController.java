package com.petclinic.controller;

import com.petclinic.dto.Result;
import com.petclinic.entity.User;
import com.petclinic.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息管理相关接口")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取详细信息")
    public Result<User> getUserInfo(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(Result::success)
                .orElse(Result.error("用户不存在"));
    }

    @PutMapping
    @Operation(summary = "更新用户信息", description = "更新用户基本信息")
    public Result<User> updateUser(@RequestBody User user) {
        if (user.getId() == null) {
            return Result.error("用户ID不能为空");
        }
        return userRepository.findById(user.getId())
                .map(existing -> {
                    if (user.getUsername() != null) {
                        existing.setUsername(user.getUsername());
                    }
                    if (user.getNickname() != null) {
                        existing.setNickname(user.getNickname());
                    }
                    if (user.getEmail() != null) {
                        existing.setEmail(user.getEmail());
                    }
                    if (user.getPhone() != null) {
                        existing.setPhone(user.getPhone());
                    }
                    if (user.getAvatar() != null) {
                        existing.setAvatar(user.getAvatar());
                    }
                    if (user.getSignature() != null) {
                        existing.setSignature(user.getSignature());
                    }
                    User saved = userRepository.save(existing);
                    return Result.success("更新成功", saved);
                })
                .orElse(Result.error("用户不存在"));
    }

    @GetMapping("/elder-mode")
    @Operation(summary = "获取长辈模式状态", description = "获取当前用户的长辈模式开关状态")
    public Result<Boolean> getElderMode() {
        // 简化实现，实际应从用户配置中获取
        return Result.success(false);
    }

    @PostMapping("/elder-mode")
    @Operation(summary = "切换长辈模式", description = "开启或关闭长辈模式")
    public Result<Boolean> toggleElderMode(
            @Parameter(description = "是否开启") @RequestParam Boolean enabled) {
        // 简化实现
        return Result.success(enabled ? "已开启长辈模式" : "已关闭长辈模式", enabled);
    }
}