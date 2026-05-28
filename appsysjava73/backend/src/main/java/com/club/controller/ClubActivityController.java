package com.club.controller;

import com.club.common.PageResult;
import com.club.common.Result;
import com.club.entity.ActivityParticipant;
import com.club.entity.ClubActivity;
import com.club.service.ClubActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 社团活动控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/activities")
@Tag(name = "社团活动", description = "社团活动管理相关接口")
public class ClubActivityController {

    @Autowired
    private ClubActivityService clubActivityService;

    /**
     * 创建活动
     */
    @PostMapping
    @Operation(summary = "创建活动", description = "社团管理员创建新活动")
    public Result<ClubActivity> createActivity(
            @RequestBody ClubActivity activity,
            @RequestAttribute("userId") Long userId) {
        ClubActivity saved = clubActivityService.createActivity(activity, userId);
        return Result.success(saved);
    }

    /**
     * 更新活动
     */
    @PutMapping("/{activityId}")
    @Operation(summary = "更新活动", description = "更新活动信息")
    public Result<ClubActivity> updateActivity(
            @PathVariable Long activityId,
            @RequestBody ClubActivity activity,
            @RequestAttribute("userId") Long userId) {
        ClubActivity updated = clubActivityService.updateActivity(activityId, activity, userId);
        return Result.success(updated);
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{activityId}")
    @Operation(summary = "删除活动", description = "删除活动")
    public Result<Void> deleteActivity(
            @PathVariable Long activityId,
            @RequestAttribute("userId") Long userId) {
        clubActivityService.deleteActivity(activityId, userId);
        return Result.success();
    }

    /**
     * 获取活动列表
     */
    @GetMapping
    @Operation(summary = "获取活动列表", description = "分页获取活动列表")
    public Result<PageResult<ClubActivity>> getActivityList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) Integer status) {
        PageResult<ClubActivity> page = clubActivityService.getActivityList(pageNum, pageSize, clubId, status);
        return Result.success(page);
    }

    /**
     * 获取活动详情
     */
    @GetMapping("/{activityId}")
    @Operation(summary = "获取活动详情", description = "获取活动详细信息")
    public Result<ClubActivity> getActivityDetail(@PathVariable Long activityId) {
        ClubActivity activity = clubActivityService.getActivityDetail(activityId);
        return Result.success(activity);
    }

    /**
     * 报名活动
     */
    @PostMapping("/{activityId}/signup")
    @Operation(summary = "报名活动", description = "用户报名参加活动")
    public Result<ActivityParticipant> signupActivity(
            @PathVariable Long activityId,
            @RequestAttribute("userId") Long userId) {
        ActivityParticipant participant = clubActivityService.signupActivity(activityId, userId);
        return Result.success(participant);
    }

    /**
     * 取消报名
     */
    @DeleteMapping("/{activityId}/signup")
    @Operation(summary = "取消报名", description = "取消活动报名")
    public Result<Void> cancelSignup(
            @PathVariable Long activityId,
            @RequestAttribute("userId") Long userId) {
        clubActivityService.cancelSignup(activityId, userId);
        return Result.success();
    }

    /**
     * 活动签到
     */
    @PostMapping("/{activityId}/checkin")
    @Operation(summary = "活动签到", description = "活动现场签到")
    public Result<Void> checkinActivity(
            @PathVariable Long activityId,
            @RequestAttribute("userId") Long userId) {
        clubActivityService.checkinActivity(activityId, userId);
        return Result.success();
    }

    /**
     * 获取活动参与者列表
     */
    @GetMapping("/{activityId}/participants")
    @Operation(summary = "获取参与者列表", description = "获取活动的报名参与者列表")
    public Result<PageResult<ActivityParticipant>> getActivityParticipants(
            @PathVariable Long activityId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<ActivityParticipant> page = clubActivityService.getActivityParticipants(activityId, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 获取我报名的活动列表
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的活动", description = "获取当前用户报名的活动列表")
    public Result<PageResult<ActivityParticipant>> getMyActivities(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestAttribute("userId") Long userId) {
        PageResult<ActivityParticipant> page = clubActivityService.getMyActivities(userId, pageNum, pageSize);
        return Result.success(page);
    }
}
