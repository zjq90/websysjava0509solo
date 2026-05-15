package com.chatsystem.controller;

import com.chatsystem.common.Result;
import com.chatsystem.entity.ChatMessage;
import com.chatsystem.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 聊天消息控制器
 */
@RestController
@RequestMapping("/api/chat")
@Tag(name = "聊天管理", description = "聊天消息、文件上传等相关接口")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * 获取聊天历史记录
     */
    @GetMapping("/history")
    @Operation(summary = "获取聊天历史记录", description = "获取两个用户之间的聊天历史记录")
    public Result<List<ChatMessage>> getChatHistory(@RequestParam Long userId1, @RequestParam Long userId2,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "50") int size) {
        List<ChatMessage> messages = chatMessageService.getChatHistory(userId1, userId2, page, size);
        return Result.success(messages);
    }

    /**
     * 标记消息为已读
     */
    @PostMapping("/mark-read")
    @Operation(summary = "标记消息为已读", description = "将指定用户发送的消息标记为已读")
    public Result<Integer> markAsRead(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        int count = chatMessageService.markAsRead(fromUserId, toUserId);
        return Result.success("已标记 " + count + " 条消息为已读", count);
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread-count")
    @Operation(summary = "获取未读消息数量", description = "获取指定用户发送的未读消息数量")
    public Result<Long> getUnreadCount(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        long count = chatMessageService.getUnreadCount(fromUserId, toUserId);
        return Result.success(count);
    }

    /**
     * 撤回消息
     */
    @PostMapping("/recall/{messageId}")
    @Operation(summary = "撤回消息", description = "撤回指定的消息")
    public Result<ChatMessage> recallMessage(@PathVariable Long messageId) {
        try {
            ChatMessage message = chatMessageService.recallMessage(messageId);
            return Result.success("消息已撤回", message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传聊天文件或图片")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = chatMessageService.uploadFile(file);
            return Result.success("上传成功", fileUrl);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 发送消息（REST方式）
     */
    @PostMapping("/send")
    @Operation(summary = "发送消息", description = "使用REST方式发送聊天消息")
    public Result<ChatMessage> sendMessage(@RequestParam Long fromUserId, @RequestParam Long toUserId,
                                           @RequestParam Integer type, @RequestParam(required = false) String content,
                                           @RequestParam(required = false) String fileName,
                                           @RequestParam(required = false) String fileUrl,
                                           @RequestParam(required = false) Long fileSize) {
        try {
            ChatMessage message = chatMessageService.saveMessage(fromUserId, toUserId, type, content,
                    fileName, fileUrl, fileSize);
            return Result.success("发送成功", message);
        } catch (Exception e) {
            return Result.error("发送失败: " + e.getMessage());
        }
    }

    /**
     * 批量发送消息
     */
    @PostMapping("/batch-send")
    @Operation(summary = "批量发送消息", description = "向多个用户发送相同的消息")
    public Result<Void> batchSendMessages(@RequestParam Long fromUserId, @RequestParam List<Long> toUserIds,
                                          @RequestParam Integer type, @RequestParam String content) {
        try {
            chatMessageService.batchSendMessages(fromUserId, toUserIds, type, content);
            return Result.success("批量发送成功");
        } catch (Exception e) {
            return Result.error("批量发送失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户所有消息
     */
    @GetMapping("/all/{userId}")
    @Operation(summary = "获取用户所有消息", description = "获取用户的所有聊天消息")
    public Result<List<ChatMessage>> getAllMessagesByUser(@PathVariable Long userId) {
        List<ChatMessage> messages = chatMessageService.getAllMessagesByUser(userId);
        return Result.success(messages);
    }
}
