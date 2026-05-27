package com.club.controller;

import com.club.common.Result;
import com.club.dto.PageQuery;
import com.club.entity.Activity;
import com.club.entity.ContentReview;
import com.club.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 活动管理控制器
 * 提供活动CRUD、活动审批、内容审查等API接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/activity")
@Tag(name = "活动管理", description = "活动监管相关接口")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询活动列表")
    public Result<Page<Activity>> getActivityList(PageQuery query) {
        return Result.success(activityService.getActivityList(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询活动详情")
    public Result<Activity> getActivityById(@PathVariable Long id) {
        return Result.success(activityService.getActivityById(id));
    }

    @PostMapping
    @Operation(summary = "新增活动")
    public Result<Activity> createActivity(@RequestBody Activity activity) {
        return Result.success(activityService.createActivity(activity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新活动信息")
    public Result<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        try {
            return Result.success(activityService.updateActivity(id, activity));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除活动")
    public Result<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return Result.success();
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "审核活动")
    public Result<Activity> approveActivity(
            @PathVariable Long id,
            @Parameter(description = "是否通过") @RequestParam boolean approved,
            @Parameter(description = "审核意见") @RequestParam String opinion,
            @Parameter(description = "审核人ID") @RequestParam(required = false) Long approverId,
            @Parameter(description = "审核人姓名") @RequestParam(required = false) String approverName) {
        try {
            return Result.success(activityService.approveActivity(id, approved, opinion, approverId, approverName));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取活动统计数据")
    public Result<Map<String, Object>> getActivityStatistics() {
        return Result.success(activityService.getActivityStatistics());
    }

    @GetMapping("/non-compliant")
    @Operation(summary = "获取内容不合规的活动")
    public Result<List<Activity>> getNonCompliantActivities() {
        return Result.success(activityService.getNonCompliantActivities());
    }

    @GetMapping("/review/list")
    @Operation(summary = "获取活动内容审查列表")
    public Result<List<ContentReview>> getReviewList() {
        return Result.success(activityService.getPendingReviewList());
    }

    @GetMapping("/review/statistics")
    @Operation(summary = "获取内容审查统计")
    public Result<Map<String, Object>> getReviewStatistics() {
        return Result.success(activityService.getReviewStatistics());
    }

    @PostMapping("/review/{id}/approve")
    @Operation(summary = "审查通过")
    public Result<ContentReview> approveReview(
            @PathVariable Long id,
            @Parameter(description = "审核人ID") @RequestParam(required = false) Long reviewerId,
            @Parameter(description = "审核人姓名") @RequestParam(required = false) String reviewerName,
            @Parameter(description = "审核意见") @RequestParam(required = false) String opinion) {
        try {
            return Result.success(activityService.approveReview(id, reviewerId, reviewerName, opinion));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/review/{id}/block")
    @Operation(summary = "屏蔽内容")
    public Result<ContentReview> blockReview(
            @PathVariable Long id,
            @Parameter(description = "审核人ID") @RequestParam(required = false) Long reviewerId,
            @Parameter(description = "审核人姓名") @RequestParam(required = false) String reviewerName,
            @Parameter(description = "审核意见") @RequestParam(required = false) String opinion) {
        try {
            return Result.success(activityService.blockReview(id, reviewerId, reviewerName, opinion));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/approval/pending")
    @Operation(summary = "获取待审批活动列表")
    public Result<Page<Activity>> getPendingApprovalList(
            PageQuery query,
            @Parameter(description = "审批阶段（1-社联，2-团委，3-校级领导）") @RequestParam(required = false) Integer approvalStage) {
        return Result.success(activityService.getPendingApprovalList(query, approvalStage));
    }

    @GetMapping("/approval/statistics")
    @Operation(summary = "获取活动审批统计")
    public Result<Map<String, Object>> getApprovalStatistics() {
        return Result.success(activityService.getApprovalStatistics());
    }
}
