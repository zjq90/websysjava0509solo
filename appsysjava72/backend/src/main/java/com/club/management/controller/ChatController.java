package com.club.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.Result;
import com.club.management.entity.ChatMessage;
import com.club.management.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天控制器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Api(tags = "聊天管理")
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 发送消息
     */
    @ApiOperation("发送消息")
    @PostMapping("/send")
    public Result<ChatMessage> sendMessage(
            @ApiParam(value = "社团ID", required = true)
            @RequestParam Long clubId,
            @ApiParam(value = "消息类型 text/image/voice", required = true)
            @RequestParam String messageType,
            @ApiParam("消息内容")
            @RequestParam(required = false) String content,
            @ApiParam("媒体文件地址")
            @RequestParam(required = false) String mediaUrl,
            @ApiParam("语音时长(秒)")
            @RequestParam(required = false) Integer duration) {
        ChatMessage message = chatService.sendMessage(clubId, messageType, content, mediaUrl, duration);
        return Result.success("发送成功", message);
    }

    /**
     * 分页获取聊天消息
     */
    @ApiOperation("分页获取聊天消息")
    @GetMapping("/messages")
    public Result<PageResult<ChatMessage>> getChatMessages(
            @ApiParam(value = "页码", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", defaultValue = "20")
            @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam(value = "社团ID", required = true)
            @RequestParam Long clubId) {
        Page<ChatMessage> page = new Page<>(pageNum, pageSize);
        PageResult<ChatMessage> pageResult = chatService.getChatMessages(page, clubId);
        return Result.success(pageResult);
    }

    /**
     * 标记消息已读
     */
    @ApiOperation("标记消息已读")
    @PostMapping("/read/{clubId}")
    public Result<Void> markMessagesAsRead(
            @ApiParam(value = "社团ID", required = true)
            @PathVariable Long clubId) {
        chatService.markMessagesAsRead(clubId);
        return Result.success("操作成功", null);
    }

    /**
     * 获取未读消息数
     */
    @ApiOperation("获取未读消息数")
    @GetMapping("/unread-count/{clubId}")
    public Result<Integer> getUnreadCount(
            @ApiParam(value = "社团ID", required = true)
            @PathVariable Long clubId) {
        Integer count = chatService.getUnreadCount(clubId);
        return Result.success(count);
    }
}
