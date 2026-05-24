package com.bikeshare.controller;

import com.bikeshare.common.Result;
import com.bikeshare.entity.CustomerTicket;
import com.bikeshare.entity.User;
import com.bikeshare.entity.UserBlacklist;
import com.bikeshare.entity.UserMembership;
import com.bikeshare.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "获取用户列表", description = "获取所有用户列表")
    public Result<List<User>> getUsers() {
        return Result.success(userService.getAllUsers());
    }

    @GetMapping("/blacklist")
    @Operation(summary = "获取黑名单", description = "获取用户黑名单列表")
    public Result<List<UserBlacklist>> getBlacklist() {
        return Result.success(userService.getBlacklist());
    }

    @PostMapping("/blacklist")
    @Operation(summary = "添加黑名单", description = "将用户加入黑名单")
    public Result<UserBlacklist> addToBlacklist(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String reason = params.get("reason").toString();
        String operator = params.getOrDefault("operator", "admin").toString();
        return Result.success(userService.addToBlacklist(userId, reason, operator));
    }

    @DeleteMapping("/blacklist/{userId}")
    @Operation(summary = "移除黑名单", description = "将用户从黑名单移除")
    public Result<Void> removeFromBlacklist(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        userService.removeFromBlacklist(userId);
        return Result.success();
    }

    @GetMapping("/tickets")
    @Operation(summary = "获取客服工单", description = "获取客服工单列表")
    public Result<List<CustomerTicket>> getTickets(
            @Parameter(description = "工单状态") @RequestParam(required = false) String status) {
        return Result.success(userService.getTickets(status));
    }

    @GetMapping("/tickets/{id}")
    @Operation(summary = "获取工单详情", description = "获取工单详细信息")
    public Result<CustomerTicket> getTicket(
            @Parameter(description = "工单ID") @PathVariable Long id) {
        return Result.success(userService.getTicket(id));
    }

    @PutMapping("/tickets/{id}")
    @Operation(summary = "处理工单", description = "回复并处理工单")
    public Result<CustomerTicket> handleTicket(
            @Parameter(description = "工单ID") @PathVariable Long id,
            @RequestBody Map<String, Object> params) {
        String reply = params.get("reply").toString();
        String handler = params.getOrDefault("handler", "admin").toString();
        String status = params.get("status").toString();
        return Result.success(userService.handleTicket(id, reply, handler, status));
    }

    @GetMapping("/high-frequency")
    @Operation(summary = "获取高频用户", description = "获取高频用户列表用于月卡赠送")
    public Result<List<User>> getHighFrequencyUsers(
            @Parameter(description = "最小骑行次数") @RequestParam(defaultValue = "30") Integer minRides) {
        return Result.success(userService.getHighFrequencyUsers(minRides));
    }

    @PostMapping("/gift-membership")
    @Operation(summary = "赠送月卡", description = "给用户赠送月卡会员")
    public Result<UserMembership> giftMembership(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String operator = params.getOrDefault("operator", "admin").toString();
        return Result.success(userService.giftMonthlyCard(userId, operator));
    }

    @GetMapping("/{userId}/memberships")
    @Operation(summary = "获取会员记录", description = "获取用户会员历史记录")
    public Result<List<UserMembership>> getMembershipHistory(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(userService.getMembershipHistory(userId));
    }
}
