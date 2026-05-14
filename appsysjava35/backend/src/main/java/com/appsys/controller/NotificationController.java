package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.Notification;
import com.appsys.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "通知管理", description = "消息通知相关接口")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户通知列表")
    public Result<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return Result.success(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/type/{type}")
    @Operation(summary = "根据类型获取用户通知")
    public Result<List<Notification>> getUserNotificationsByType(@PathVariable Long userId, @PathVariable Integer type) {
        return Result.success(notificationService.getUserNotificationsByType(userId, type));
    }

    @GetMapping("/unread/{userId}")
    @Operation(summary = "获取未读消息数量")
    public Result<Long> getUnreadCount(@PathVariable Long userId) {
        return Result.success(notificationService.getUnreadCount(userId));
    }

    @PostMapping("/read/{id}")
    @Operation(summary = "标记单条消息为已读")
    public Result<Boolean> markAsRead(@PathVariable Long id) {
        boolean success = notificationService.markAsRead(id);
        if (success) {
            return Result.success(true);
        }
        return Result.error("标记失败");
    }

    @PostMapping("/read-all/{userId}")
    @Operation(summary = "标记所有消息为已读")
    public Result<Boolean> markAllAsRead(@PathVariable Long userId) {
        boolean success = notificationService.markAllAsRead(userId);
        if (success) {
            return Result.success(true);
        }
        return Result.error("标记失败");
    }

    @PostMapping("/create")
    @Operation(summary = "创建通知")
    public Result<Notification> createNotification(@RequestBody Notification notification) {
        return Result.success("创建成功", notificationService.createNotification(notification));
    }

    @PostMapping("/broadcast")
    @Operation(summary = "发送广播通知")
    public Result<Void> sendBroadcastNotification(@RequestParam String title,
                                                    @RequestParam String content,
                                                    @RequestParam Integer type) {
        notificationService.sendBroadcastNotification(title, content, type);
        return Result.success("广播通知已发送");
    }
}
