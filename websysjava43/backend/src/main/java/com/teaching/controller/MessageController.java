package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.Message;
import com.teaching.service.MessageService;
import com.teaching.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@Tag(name = "消息管理", description = "用户消息的发送和查看")
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtUtils jwtUtils;

    private Long getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtils.getUserIdFromToken(token);
    }

    @PostMapping("/send")
    @Operation(summary = "发送消息")
    public Result<Message> sendMessage(@RequestHeader(value = "Authorization", required = false) String token,
                                       @RequestBody Map<String, Object> params) {
        Long senderId = null;
        if (token != null && !token.isEmpty()) {
            senderId = getUserIdFromToken(token);
        }
        Long receiverId = Long.valueOf(params.get("receiverId").toString());
        String title = params.get("title").toString();
        String content = params.get("content") != null ? params.get("content").toString() : "";
        
        Message message = messageService.sendMessage(senderId, receiverId, title, content);
        return Result.success("发送成功", message);
    }

    @GetMapping("/list")
    @Operation(summary = "获取我的消息列表")
    public Result<List<Message>> getMyMessages(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        List<Message> messages = messageService.getUserMessages(userId);
        return Result.success(messages);
    }

    @GetMapping("/unread")
    @Operation(summary = "获取未读消息")
    public Result<List<Message>> getUnreadMessages(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        List<Message> messages = messageService.getUnreadMessages(userId);
        return Result.success(messages);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "获取未读消息数量")
    public Result<Integer> getUnreadCount(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        int count = messageService.getUnreadCount(userId);
        return Result.success(count);
    }

    @PostMapping("/{id}/read")
    @Operation(summary = "标记消息已读")
    public Result<Message> markAsRead(@PathVariable Long id) {
        Message message = messageService.markAsRead(id);
        return Result.success("已标记已读", message);
    }

    @PostMapping("/read-all")
    @Operation(summary = "标记所有消息已读")
    public Result<Void> markAllAsRead(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        messageService.markAllAsRead(userId);
        return Result.success("所有消息已标记为已读");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除消息")
    public Result<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return Result.success("删除成功");
    }
}
