package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.entity.Notification;
import com.personal.accounting.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "通知管理", description = "通知提醒相关接口")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "获取用户所有通知")
    @GetMapping("/user/{userId}")
    public Result<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return Result.success(notificationService.getUserNotifications(userId));
    }

    @Operation(summary = "获取用户未读通知")
    @GetMapping("/user/{userId}/unread")
    public Result<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        return Result.success(notificationService.getUnreadNotifications(userId));
    }

    @Operation(summary = "获取未读通知数量")
    @GetMapping("/user/{userId}/unread-count")
    public Result<Long> getUnreadCount(@PathVariable Long userId) {
        return Result.success(notificationService.getUnreadCount(userId));
    }

    @Operation(summary = "标记通知为已读")
    @PutMapping("/{id}/read")
    public Result<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return Result.success();
    }

    @Operation(summary = "标记所有通知为已读")
    @PutMapping("/user/{userId}/read-all")
    public Result<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return Result.success();
    }

    @Operation(summary = "删除通知")
    @DeleteMapping("/{id}")
    public Result<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return Result.success();
    }
}
