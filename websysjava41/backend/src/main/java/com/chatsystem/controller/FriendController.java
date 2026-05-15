package com.chatsystem.controller;

import com.chatsystem.common.Result;
import com.chatsystem.entity.Friend;
import com.chatsystem.entity.User;
import com.chatsystem.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友管理控制器
 */
@RestController
@RequestMapping("/api/friend")
@Tag(name = "好友管理", description = "好友添加、删除、查询等相关接口")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 发送好友请求
     */
    @PostMapping("/request")
    @Operation(summary = "发送好友请求", description = "向指定用户发送好友请求")
    public Result<Friend> addFriendRequest(@RequestParam Long userId, @RequestParam Long friendId) {
        try {
            Friend friend = friendService.addFriendRequest(userId, friendId);
            return Result.success("好友请求已发送", friend);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 接受好友请求
     */
    @PostMapping("/accept/{requestId}")
    @Operation(summary = "接受好友请求", description = "接受指定的好友请求")
    public Result<Friend> acceptFriendRequest(@PathVariable Long requestId) {
        try {
            Friend friend = friendService.acceptFriendRequest(requestId);
            return Result.success("已添加好友", friend);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 拒绝好友请求
     */
    @PostMapping("/reject/{requestId}")
    @Operation(summary = "拒绝好友请求", description = "拒绝指定的好友请求")
    public Result<Void> rejectFriendRequest(@PathVariable Long requestId) {
        try {
            friendService.rejectFriendRequest(requestId);
            return Result.success("已拒绝好友请求");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除好友
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除好友", description = "删除指定好友")
    public Result<Void> deleteFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        try {
            friendService.deleteFriend(userId, friendId);
            return Result.success("已删除好友");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取好友列表
     */
    @GetMapping("/list/{userId}")
    @Operation(summary = "获取好友列表", description = "获取用户的所有好友")
    public Result<List<User>> getFriendList(@PathVariable Long userId) {
        List<User> friends = friendService.getFriendList(userId);
        friends.forEach(user -> user.setPassword(null));
        return Result.success(friends);
    }

    /**
     * 获取好友请求列表
     */
    @GetMapping("/requests/{userId}")
    @Operation(summary = "获取好友请求列表", description = "获取收到的好友请求")
    public Result<List<Friend>> getFriendRequests(@PathVariable Long userId) {
        List<Friend> requests = friendService.getFriendRequests(userId);
        return Result.success(requests);
    }

    /**
     * 更新好友备注
     */
    @PutMapping("/remark")
    @Operation(summary = "更新好友备注", description = "更新好友的备注名称")
    public Result<Friend> updateRemark(@RequestParam Long userId, @RequestParam Long friendId, @RequestParam String remark) {
        try {
            Friend friend = friendService.updateRemark(userId, friendId, remark);
            return Result.success("备注已更新", friend);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查是否为好友
     */
    @GetMapping("/check")
    @Operation(summary = "检查是否为好友", description = "检查两个用户是否为好友")
    public Result<Boolean> isFriend(@RequestParam Long userId1, @RequestParam Long userId2) {
        boolean isFriend = friendService.isFriend(userId1, userId2);
        return Result.success(isFriend);
    }
}
