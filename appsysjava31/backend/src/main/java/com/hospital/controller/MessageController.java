package com.hospital.controller;

import com.hospital.annotation.OperationLog;
import com.hospital.common.Result;
import com.hospital.dto.MessageVO;
import com.hospital.entity.Message;
import com.hospital.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息控制器
 * 处理消息通知相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/patient/messages")
@Tag(name = "消息管理", description = "消息通知相关接口")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取消息列表
     */
    @GetMapping
    @Operation(summary = "获取消息列表", description = "获取当前用户的所有消息")
    public Result<List<MessageVO>> getMyMessages(@AuthenticationPrincipal Long userId) {
        List<Message> messages = messageService.getUserMessages(userId);
        List<MessageVO> voList = messages.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 统计未读消息
     */
    @GetMapping("/unread/count")
    @Operation(summary = "统计未读消息", description = "获取当前用户未读消息数量")
    public Result<Long> countUnread(@AuthenticationPrincipal Long userId) {
        return Result.success(messageService.countUnread(userId));
    }

    /**
     * 获取消息详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取消息详情", description = "根据ID获取消息详细信息")
    public Result<MessageVO> getMessageById(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        Message message = messageService.getMessageById(id, userId);
        messageService.markAsRead(id, userId);
        return Result.success(convertToVO(message));
    }

    /**
     * 标记消息已读
     */
    @PostMapping("/{id}/read")
    @Operation(summary = "标记消息已读", description = "将指定消息标记为已读")
    public Result<?> markAsRead(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        messageService.markAsRead(id, userId);
        return Result.success();
    }

    /**
     * 标记消息已读（PUT）
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "标记消息已读", description = "将指定消息标记为已读")
    public Result<?> markAsReadPut(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        messageService.markAsRead(id, userId);
        return Result.success();
    }

    /**
     * 标记所有消息已读
     */
    @PostMapping("/read-all")
    @Operation(summary = "标记全部已读", description = "将所有消息标记为已读")
    @OperationLog(value = "标记全部已读", operationType = "MESSAGE_READ", module = "USER")
    public Result<?> markAllAsRead(@AuthenticationPrincipal Long userId) {
        messageService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 标记所有消息已读（PUT）
     */
    @PutMapping("/read-all")
    @Operation(summary = "标记全部已读", description = "将所有消息标记为已读")
    @OperationLog(value = "标记全部已读", operationType = "MESSAGE_READ", module = "USER")
    public Result<?> markAllAsReadPut(@AuthenticationPrincipal Long userId) {
        messageService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 将Message转换为MessageVO
     */
    private MessageVO convertToVO(Message message) {
        MessageVO vo = new MessageVO();
        vo.setId(message.getId());
        vo.setMessageType(message.getMessageType());
        vo.setBusinessType(message.getBusinessType());
        vo.setUserId(message.getUserId());
        vo.setTitle(message.getTitle());
        vo.setContent(message.getContent());
        vo.setBusinessId(message.getBusinessId());
        vo.setRedirectUrl(message.getRedirectUrl());
        vo.setIsRead(message.getIsRead() != null && message.getIsRead() == 1);
        vo.setReadStatus(message.getIsRead());
        vo.setReadTime(message.getReadTime());
        vo.setCreateTime(message.getCreateTime());
        vo.setSendTime(message.getSendTime());
        
        String type = "SYSTEM";
        String msgType = message.getMessageType();
        if (msgType != null) {
            if (msgType.contains("REMINDER")) {
                type = "REMINDER";
            } else if (msgType.startsWith("APPOINTMENT")) {
                type = "APPOINTMENT";
            } else if (msgType.startsWith("REPORT")) {
                type = "REPORT";
            }
        }
        vo.setType(type);
        
        return vo;
    }
}
