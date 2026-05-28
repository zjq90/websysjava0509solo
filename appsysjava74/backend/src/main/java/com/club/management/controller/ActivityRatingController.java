package com.club.management.controller;

import com.club.management.common.ApiResponse;
import com.club.management.common.PageQuery;
import com.club.management.dto.ActivityRatingDTO;
import com.club.management.entity.ActivityRating;
import com.club.management.service.ActivityRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 活动评分控制器
 * 
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "活动评分", description = "活动评分、评价、反馈等接口")
@RestController
@RequestMapping("/activity-ratings")
@RequiredArgsConstructor
public class ActivityRatingController {

    private final ActivityRatingService activityRatingService;

    /**
     * 提交活动评分
     */
    @Operation(summary = "提交活动评分", description = "参与活动的学生对活动进行评分评价")
    @PostMapping
    public ApiResponse<ActivityRating> submitRating(
            @Valid @RequestBody ActivityRatingDTO dto,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("评分提交成功", activityRatingService.submitRating(dto, userId));
    }

    /**
     * 分页查询活动评分列表
     */
    @Operation(summary = "查询活动评分列表", description = "分页查询指定活动的评分评价")
    @GetMapping("/activity/{activityId}")
    public ApiResponse<Page<ActivityRating>> getRatingList(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @ModelAttribute PageQuery pageQuery) {
        return ApiResponse.success(activityRatingService.getRatingList(activityId, pageQuery));
    }

    /**
     * 获取用户对活动的评分
     */
    @Operation(summary = "获取用户评分", description = "获取当前用户对指定活动的评分")
    @GetMapping("/activity/{activityId}/my")
    public ApiResponse<ActivityRating> getMyRating(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(activityRatingService.getUserRating(activityId, userId));
    }

    /**
     * 删除评分
     */
    @Operation(summary = "删除评分", description = "用户删除自己的评分")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRating(
            @Parameter(description = "评分ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        activityRatingService.deleteRating(id, userId);
        return ApiResponse.success("删除成功", null);
    }
}
