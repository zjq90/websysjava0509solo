package com.seedinventory.controller;

import com.seedinventory.common.Result;
import com.seedinventory.entity.Notification;
import com.seedinventory.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 消息通知控制器
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@Tag(name = "消息通知", description = "预警消息的查询和处理")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    @Operation(summary = "查询所有通知")
    public Result<List<Notification>> list() {
        return Result.success(notificationService.findAll());
    }
    
    @GetMapping("/unread")
    @Operation(summary = "查询未读通知")
    public Result<List<Notification>> getUnread() {
        return Result.success(notificationService.findUnread());
    }
    
    @GetMapping("/count-unread")
    @Operation(summary = "统计未读消息数量")
    public Result<Map<String, Long>> countUnread() {
        Map<String, Long> result = new HashMap<>();
        result.put("count", notificationService.countUnread());
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "查询通知详情")
    public Result<Notification> getById(@PathVariable Long id) {
        Optional<Notification> opt = notificationService.findById(id);
        if (opt.isPresent()) {
            return Result.success(opt.get());
        }
        return Result.error(404, "通知不存在");
    }
    
    @PutMapping("/{id}/read")
    @Operation(summary = "标记为已读")
    public Result<Notification> markAsRead(@PathVariable Long id) {
        try {
            Notification result = notificationService.markAsRead(id);
            return Result.success("标记成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/handle")
    @Operation(summary = "标记为已处理")
    public Result<Notification> markAsHandled(
            @PathVariable Long id,
            @RequestParam String handler,
            @RequestParam(required = false) String remark) {
        try {
            Notification result = notificationService.markAsHandled(id, handler, remark);
            return Result.success("处理成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/read-all")
    @Operation(summary = "批量标记已读")
    public Result<Integer> markAllAsRead() {
        int count = notificationService.markAllAsRead();
        return Result.success("已标记 " + count + " 条消息为已读", count);
    }
}
