package com.broadband.controller;

import com.broadband.common.Result;
import com.broadband.entity.User;
import com.broadband.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器
 * 处理用户信息管理、实名认证等接口
 * 
 * @author broadband
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户信息管理、实名认证等接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{userId}")
    public Result<User> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "更新用户信息", description = "更新用户基本信息")
    @PutMapping
    public Result<User> updateUser(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
            return Result.success("更新成功", updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "提交实名认证", description = "上传身份证信息和照片进行实名认证")
    @PostMapping("/realName")
    public Result<User> submitRealName(@RequestParam Long userId,
                                         @RequestParam String name,
                                         @RequestParam String idCard,
                                         @RequestParam(required = false) MultipartFile frontImg,
                                         @RequestParam(required = false) MultipartFile backImg) {
        try {
            User user = userService.submitRealName(userId, name, idCard, frontImg, backImg);
            return Result.success("提交成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "活体检测认证", description = "完成活体检测，完成实名认证")
    @PostMapping("/liveness")
    public Result<User> verifyLiveness(@RequestParam Long userId) {
        try {
            User user = userService.verifyLiveness(userId);
            return Result.success("认证成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "切换长辈模式", description = "开启或关闭长辈模式")
    @PostMapping("/elderMode")
    public Result<User> toggleElderMode(@RequestParam Long userId, @RequestParam Integer elderMode) {
        try {
            User user = userService.toggleElderMode(userId, elderMode);
            return Result.success(elderMode == 1 ? "已开启长辈模式" : "已关闭长辈模式", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "切换自动续费", description = "开启或关闭自动续费功能")
    @PostMapping("/autoRenewal")
    public Result<User> toggleAutoRenewal(@RequestParam Long userId, @RequestParam Integer autoRenewal) {
        try {
            User user = userService.toggleAutoRenewal(userId, autoRenewal);
            return Result.success(autoRenewal == 1 ? "已开启自动续费" : "已关闭自动续费", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
