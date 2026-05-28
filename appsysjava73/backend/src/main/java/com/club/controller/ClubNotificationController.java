package com.club.controller;

import com.club.common.PageResult;
import com.club.common.Result;
import com.club.entity.ClubNotification;
import com.club.service.ClubNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 社团通知控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "通知管理", description = "用户通知相关接口")
public class ClubNotificationController {

    @Autowired
    private ClubNotificationService clubNotificationService;

    /**
     * 获取我的通知列表
     */
    @GetMapping
    @Operation(summary = "获取通知列表", description = "获取当前用户的通知列表")
    public Result<PageResult<ClubNotification>> getMyNotifications(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String type,
            @RequestAttribute("userId") Long userId) {
        PageResult<ClubNotification> page = clubNotificationService.getMyNotifications(userId, type, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 标记通知已读
     */
    @PutMapping("/{notificationId}/read")
    @Operation(summary = "标记已读", description = "标记指定通知为已读")
    public Result<Void> markAsRead(
            @PathVariable Long notificationId,
            @RequestAttribute("userId") Long userId) {
        clubNotificationService.markAsRead(notificationId, userId);
        return Result.success();
    }

    /**
     * 标记所有通知已读
     */
    @PutMapping("/read-all")
    @Operation(summary = "标记全部已读", description = "标记所有通知为已读")
    public Result<Void> markAllAsRead(@RequestAttribute("userId") Long userId) {
        clubNotificationService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "删除通知", description = "删除指定通知")
    public Result<Void> deleteNotification(
            @PathVariable Long notificationId,
            @RequestAttribute("userId") Long userId) {
        clubNotificationService.deleteNotification(notificationId, userId);
        return Result.success();
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count")
    @Operation(summary = "获取未读数量", description = "获取当前用户的未读通知数量")
    public Result<Long> getUnreadCount(@RequestAttribute("userId") Long userId) {
        long count = clubNotificationService.getUnreadCount(userId);
        return Result.success(count);
    }
}
