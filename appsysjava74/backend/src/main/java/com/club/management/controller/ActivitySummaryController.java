package com.club.management.controller;

import com.club.management.common.ApiResponse;
import com.club.management.common.PageQuery;
import com.club.management.dto.ActivitySummaryDTO;
import com.club.management.entity.ActivitySummary;
import com.club.management.service.ActivitySummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 活动总结控制器
 * 
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "活动总结", description = "活动总结的创建、发布、查询等接口")
@RestController
@RequestMapping("/activity-summaries")
@RequiredArgsConstructor
public class ActivitySummaryController {

    private final ActivitySummaryService activitySummaryService;

    /**
     * 创建活动总结
     */
    @Operation(summary = "创建活动总结", description = "活动结束后创建总结")
    @PostMapping
    public ApiResponse<ActivitySummary> createSummary(
            @Valid @RequestBody ActivitySummaryDTO dto,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("总结创建成功", activitySummaryService.createSummary(dto, userId));
    }

    /**
     * 更新活动总结
     */
    @Operation(summary = "更新活动总结", description = "更新活动总结信息")
    @PutMapping("/{id}")
    public ApiResponse<ActivitySummary> updateSummary(
            @Parameter(description = "总结ID") @PathVariable Long id,
            @Valid @RequestBody ActivitySummaryDTO dto,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("总结更新成功", activitySummaryService.updateSummary(id, dto, userId));
    }

    /**
     * 发布活动总结
     */
    @Operation(summary = "发布活动总结", description = "发布活动总结，同步到社团主页")
    @PostMapping("/{id}/publish")
    public ApiResponse<ActivitySummary> publishSummary(
            @Parameter(description = "总结ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("总结发布成功", activitySummaryService.publishSummary(id, userId));
    }

    /**
     * 删除活动总结
     */
    @Operation(summary = "删除活动总结", description = "删除活动总结")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSummary(
            @Parameter(description = "总结ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        activitySummaryService.deleteSummary(id, userId);
        return ApiResponse.success("删除成功", null);
    }

    /**
     * 获取活动总结详情
     */
    @Operation(summary = "获取活动总结详情", description = "根据ID获取活动总结详情")
    @GetMapping("/{id}")
    public ApiResponse<ActivitySummary> getSummaryById(
            @Parameter(description = "总结ID") @PathVariable Long id) {
        return ApiResponse.success(activitySummaryService.getSummaryById(id));
    }

    /**
     * 根据活动ID获取总结
     */
    @Operation(summary = "根据活动ID获取总结", description = "获取指定活动的总结")
    @GetMapping("/activity/{activityId}")
    public ApiResponse<ActivitySummary> getSummaryByActivityId(
            @Parameter(description = "活动ID") @PathVariable Long activityId) {
        return ApiResponse.success(activitySummaryService.getSummaryByActivityId(activityId));
    }

    /**
     * 分页查询社团活动总结
     */
    @Operation(summary = "查询社团活动总结", description = "分页查询指定社团的活动总结")
    @GetMapping("/club/{clubId}")
    public ApiResponse<Page<ActivitySummary>> getSummaryListByClub(
            @Parameter(description = "社团ID") @PathVariable Long clubId,
            @Parameter(description = "是否包含未发布") @RequestParam(defaultValue = "false") boolean includeUnpublished,
            @ModelAttribute PageQuery pageQuery) {
        return ApiResponse.success(
                activitySummaryService.getSummaryListByClub(clubId, pageQuery, includeUnpublished));
    }
}
