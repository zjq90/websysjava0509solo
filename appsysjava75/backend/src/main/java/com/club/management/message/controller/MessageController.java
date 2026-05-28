package com.club.management.message.controller;

import com.club.management.common.result.PageResult;
import com.club.management.common.result.Result;
import com.club.management.message.entity.*;
import com.club.management.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息通知Controller
 *
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "消息通知管理", description = "系统消息、群聊、私聊、已读状态等接口")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "获取系统消息列表")
    @GetMapping("/system/list")
    public Result<PageResult<SystemMessage>> getSystemMessageList(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(messageService.getSystemMessageList(userId, pageNum, pageSize));
    }

    @Operation(summary = "发送系统消息")
    @PostMapping("/system/send")
    public Result<SystemMessage> sendSystemMessage(@RequestBody SystemMessage message) {
        return Result.success(messageService.sendSystemMessage(message));
    }

    @Operation(summary = "标记系统消息为已读")
    @PostMapping("/system/read/{messageId}")
    public Result<Void> markSystemMessageAsRead(
            @Parameter(description = "消息ID") @PathVariable Long messageId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        messageService.markSystemMessageAsRead(messageId, userId);
        return Result.success();
    }

    @Operation(summary = "标记所有系统消息为已读")
    @PostMapping("/system/read-all")
    public Result<Void> markAllSystemMessageAsRead(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        messageService.markAllSystemMessageAsRead(userId);
        return Result.success();
    }

    @Operation(summary = "获取未读消息数量")
    @GetMapping("/system/unread-count")
    public Result<Long> getUnreadMessageCount(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return Result.success(messageService.getUnreadMessageCount(userId));
    }

    @Operation(summary = "获取群聊列表")
    @GetMapping("/group/list")
    public Result<PageResult<ChatGroup>> getChatGroupList(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(messageService.getChatGroupList(userId, pageNum, pageSize));
    }

    @Operation(summary = "创建群聊")
    @PostMapping("/group/create")
    public Result<ChatGroup> createChatGroup(@RequestBody ChatGroup group) {
        return Result.success(messageService.createChatGroup(group));
    }

    @Operation(summary = "获取群成员列表")
    @GetMapping("/group/members/{groupId}")
    public Result<List<ChatGroupMember>> getGroupMemberList(
            @Parameter(description = "群ID") @PathVariable Long groupId) {
        return Result.success(messageService.getGroupMemberList(groupId));
    }

    @Operation(summary = "添加群成员")
    @PostMapping("/group/add-member")
    public Result<Void> addGroupMember(@RequestBody Map<String, Object> params) {
        Long groupId = Long.valueOf(params.get("groupId").toString());
        Long userId = Long.valueOf(params.get("userId").toString());
        String username = (String) params.get("username");
        String realName = (String) params.get("realName");
        String avatar = (String) params.get("avatar");
        messageService.addGroupMember(groupId, userId, username, realName, avatar);
        return Result.success();
    }

    @Operation(summary = "发送群消息")
    @PostMapping("/group/send")
    public Result<GroupMessage> sendGroupMessage(@RequestBody GroupMessage message) {
        return Result.success(messageService.sendGroupMessage(message));
    }

    @Operation(summary = "获取群消息列表")
    @GetMapping("/group/messages/{groupId}")
    public Result<PageResult<GroupMessage>> getGroupMessageList(
            @Parameter(description = "群ID") @PathVariable Long groupId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(messageService.getGroupMessageList(groupId, pageNum, pageSize));
    }

    @Operation(summary = "标记群消息为已读")
    @PostMapping("/group/read/{groupId}")
    public Result<Void> markGroupMessageAsRead(
            @Parameter(description = "群ID") @PathVariable Long groupId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        messageService.markGroupMessageAsRead(groupId, userId);
        return Result.success();
    }

    @Operation(summary = "发送私聊消息")
    @PostMapping("/private/send")
    public Result<PrivateMessage> sendPrivateMessage(@RequestBody PrivateMessage message) {
        return Result.success(messageService.sendPrivateMessage(message));
    }

    @Operation(summary = "获取私聊消息列表")
    @GetMapping("/private/messages")
    public Result<PageResult<PrivateMessage>> getPrivateMessageList(
            @Parameter(description = "用户1ID") @RequestParam Long userId1,
            @Parameter(description = "用户2ID") @RequestParam Long userId2,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(messageService.getPrivateMessageList(userId1, userId2, pageNum, pageSize));
    }

    @Operation(summary = "标记私聊消息为已读")
    @PostMapping("/private/read")
    public Result<Void> markPrivateMessageAsRead(
            @Parameter(description = "发送者ID") @RequestParam Long senderId,
            @Parameter(description = "接收者ID") @RequestParam Long receiverId) {
        messageService.markPrivateMessageAsRead(senderId, receiverId);
        return Result.success();
    }

    @Operation(summary = "获取私聊未读消息数量")
    @GetMapping("/private/unread-count")
    public Result<Long> getPrivateUnreadCount(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return Result.success(messageService.getPrivateUnreadCount(userId));
    }

    @Operation(summary = "获取消息已读状态")
    @GetMapping("/read-status")
    public Result<List<MessageReadStatus>> getMessageReadStatus(
            @Parameter(description = "消息ID") @RequestParam Long messageId,
            @Parameter(description = "消息类型 0-系统消息 1-群消息") @RequestParam Integer messageType) {
        return Result.success(messageService.getMessageReadStatus(messageId, messageType));
    }

    @Operation(summary = "二次提醒未读成员")
    @PostMapping("/remind")
    public Result<Void> remindUnreadMember(
            @Parameter(description = "消息ID") @RequestParam Long messageId,
            @Parameter(description = "消息类型 0-系统消息 1-群消息") @RequestParam Integer messageType,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        messageService.remindUnreadMember(messageId, messageType, userId);
        return Result.success();
    }

    @Operation(summary = "批量创建消息已读状态记录")
    @PostMapping("/read-status/batch")
    public Result<Void> createMessageReadStatus(@RequestBody Map<String, Object> params) {
        Long messageId = Long.valueOf(params.get("messageId").toString());
        Integer messageType = Integer.valueOf(params.get("messageType").toString());
        Long groupId = params.get("groupId") != null ? Long.valueOf(params.get("groupId").toString()) : null;
        @SuppressWarnings("unchecked")
        List<Long> userIds = (List<Long>) params.get("userIds");
        messageService.createMessageReadStatus(messageId, messageType, groupId, userIds);
        return Result.success();
    }
}
