package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.ChatMessage;
import com.appsys.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@Tag(name = "AI客服", description = "AI客服相关接口")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/history/user/{userId}")
    @Operation(summary = "获取用户聊天历史")
    public Result<List<ChatMessage>> getHistoryByUserId(@PathVariable Long userId) {
        return Result.success(chatService.getHistoryByUserId(userId));
    }

    @GetMapping("/history/session/{sessionId}")
    @Operation(summary = "获取会话聊天历史")
    public Result<List<ChatMessage>> getHistoryBySessionId(@PathVariable String sessionId) {
        return Result.success(chatService.getHistoryBySessionId(sessionId));
    }

    @PostMapping("/send")
    @Operation(summary = "发送消息")
    public Result<ChatMessage> sendMessage(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String sessionId = (String) request.get("sessionId");
        String content = (String) request.get("content");
        return Result.success(chatService.sendMessage(userId, sessionId, content));
    }

    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "删除会话")
    public Result<Void> deleteSession(@PathVariable String sessionId) {
        chatService.deleteBySessionId(sessionId);
        return Result.success();
    }
}
