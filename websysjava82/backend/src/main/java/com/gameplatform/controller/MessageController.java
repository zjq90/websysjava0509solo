package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.Message;
import com.gameplatform.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/user/{userId}")
    public Result<List<Message>> findByUserId(@PathVariable Long userId) {
        return Result.success(messageService.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public Result<List<Message>> findUnreadByUserId(@PathVariable Long userId) {
        return Result.success(messageService.findUnreadByUserId(userId));
    }

    @GetMapping("/task/{taskId}")
    public Result<List<Message>> findByTaskId(@PathVariable Long taskId) {
        return Result.success(messageService.findByTaskId(taskId));
    }

    @GetMapping("/{id}")
    public Result<Message> findById(@PathVariable Long id) {
        return messageService.findById(id)
                .map(Result::success)
                .orElse(Result.error("消息不存在"));
    }

    @PostMapping
    public Result<Message> save(@RequestBody Message message) {
        return Result.success(messageService.save(message));
    }

    @PutMapping("/{id}/read")
    public Result<Message> markAsRead(@PathVariable Long id) {
        Message message = messageService.markAsRead(id);
        if (message != null) {
            return Result.success(message);
        }
        return Result.error("消息不存在");
    }

    @PutMapping("/user/{userId}/read-all")
    public Result<Void> markAllAsRead(@PathVariable Long userId) {
        messageService.markAllAsRead(userId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        messageService.deleteById(id);
        return Result.success();
    }
}
