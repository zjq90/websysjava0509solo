package com.club.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.Result;
import com.club.management.entity.NotificationSetting;
import com.club.management.entity.SysMessage;
import com.club.management.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息控制器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Api(tags = "消息管理")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 分页查询消息列表
     */
    @ApiOperation("分页查询消息列表")
    @GetMapping("/page")
    public Result<PageResult<SysMessage>> getMessagePage(
            @ApiParam(value = "页码", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("消息类型 system/activity/club/chat")
            @RequestParam(required = false) String type) {
        Page<SysMessage> page = new Page<>(pageNum, pageSize);
        PageResult<SysMessage> pageResult = messageService.getMessagePage(page, type);
        return Result.success(pageResult);
    }

    /**
     * 获取消息详情
     */
    @ApiOperation("获取消息详情")
    @GetMapping("/{id}")
    public Result<SysMessage> getMessageDetail(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id) {
        SysMessage message = messageService.getMessageDetail(id);
        return Result.success(message);
    }

    /**
     * 标记消息已读
     */
    @ApiOperation("标记消息已读")
    @PostMapping("/read/{id}")
    public Result<Void> markAsRead(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id) {
        messageService.markAsRead(id);
        return Result.success("操作成功", null);
    }

    /**
     * 标记所有消息已读
     */
    @ApiOperation("标记所有消息已读")
    @PostMapping("/read-all")
    public Result<Void> markAllAsRead(
            @ApiParam("消息类型")
            @RequestParam(required = false) String type) {
        messageService.markAllAsRead(type);
        return Result.success("操作成功", null);
    }

    /**
     * 删除消息
     */
    @ApiOperation("删除消息")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMessage(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id) {
        messageService.deleteMessage(id);
        return Result.success("删除成功", null);
    }

    /**
     * 获取未读消息数
     */
    @ApiOperation("获取未读消息数")
    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount(
            @ApiParam("消息类型")
            @RequestParam(required = false) String type) {
        Integer count = messageService.getUnreadCount(type);
        return Result.success(count);
    }

    /**
     * 获取通知设置
     */
    @ApiOperation("获取通知设置")
    @GetMapping("/notification-setting")
    public Result<NotificationSetting> getNotificationSetting() {
        NotificationSetting setting = messageService.getNotificationSetting();
        return Result.success(setting);
    }

    /**
     * 更新通知设置
     */
    @ApiOperation("更新通知设置")
    @PutMapping("/notification-setting")
    public Result<Void> updateNotificationSetting(@RequestBody NotificationSetting setting) {
        messageService.updateNotificationSetting(setting);
        return Result.success("更新成功", null);
    }
}
