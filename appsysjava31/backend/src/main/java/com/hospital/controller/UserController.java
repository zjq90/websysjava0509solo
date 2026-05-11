package com.hospital.controller;

import com.hospital.annotation.OperationLog;
import com.hospital.common.Result;
import com.hospital.entity.NotificationSetting;
import com.hospital.entity.User;
import com.hospital.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 处理用户相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/patient/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户", description = "获取当前登录用户信息")
    public Result<User> getCurrentUser(@AuthenticationPrincipal Long userId) {
        return Result.success(userService.getUserById(userId));
    }

    /**
     * 获取通知设置
     */
    @GetMapping("/notification-setting")
    @Operation(summary = "获取通知设置", description = "获取当前用户的通知偏好设置")
    public Result<NotificationSetting> getNotificationSetting(@AuthenticationPrincipal Long userId) {
        return Result.success(userService.getNotificationSetting(userId));
    }

    /**
     * 更新通知设置
     */
    @PutMapping("/notification-setting")
    @Operation(summary = "更新通知设置", description = "更新用户的通知偏好设置")
    @OperationLog(value = "更新通知设置", operationType = "USER_UPDATE", module = "USER")
    public Result<NotificationSetting> updateNotificationSetting(
            @AuthenticationPrincipal Long userId,
            @RequestBody NotificationSetting setting) {
        return Result.success(userService.updateNotificationSetting(userId, setting));
    }
}
