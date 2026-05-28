package com.club.management.controller;

import com.club.management.common.ApiResponse;
import com.club.management.common.PageQuery;
import com.club.management.dto.ActivityDTO;
import com.club.management.entity.Activity;
import com.club.management.service.ActivityService;
import com.club.management.vo.SignInStatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 活动控制器
 * 
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "活动管理", description = "活动的CRUD、发布、取消、归档等接口")
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * 创建活动
     */
    @Operation(summary = "创建活动", description = "社团管理员创建新活动")
    @PostMapping
    public ApiResponse<Activity> createActivity(
            @Valid @RequestBody ActivityDTO dto,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("活动创建成功", activityService.createActivity(dto, userId));
    }

    /**
     * 更新活动
     */
    @Operation(summary = "更新活动", description = "更新活动信息")
    @PutMapping("/{id}")
    public ApiResponse<Activity> updateActivity(
            @Parameter(description = "活动ID") @PathVariable Long id,
            @Valid @RequestBody ActivityDTO dto,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("活动更新成功", activityService.updateActivity(id, dto, userId));
    }

    /**
     * 发布活动
     */
    @Operation(summary = "发布活动", description = "将草稿状态的活动发布")
    @PostMapping("/{id}/publish")
    public ApiResponse<Activity> publishActivity(
            @Parameter(description = "活动ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("活动发布成功", activityService.publishActivity(id, userId));
    }

    /**
     * 取消活动
     */
    @Operation(summary = "取消活动", description = "取消已发布的活动")
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelActivity(
            @Parameter(description = "活动ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        activityService.cancelActivity(id, userId);
        return ApiResponse.success("活动取消成功", null);
    }

    /**
     * 删除活动
     */
    @Operation(summary = "删除活动", description = "删除活动（逻辑删除）")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteActivity(
            @Parameter(description = "活动ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        activityService.deleteActivity(id, userId);
        return ApiResponse.success("活动删除成功", null);
    }

    /**
     * 获取活动详情
     */
    @Operation(summary = "获取活动详情", description = "根据ID获取活动详细信息")
    @GetMapping("/{id}")
    public ApiResponse<Activity> getActivityById(
            @Parameter(description = "活动ID") @PathVariable Long id) {
        return ApiResponse.success(activityService.getActivityById(id));
    }

    /**
     * 分页查询活动列表
     */
    @Operation(summary = "分页查询活动列表", description = "获取活动列表，支持搜索和分页")
    @GetMapping
    public ApiResponse<Page<Activity>> getActivityList(
            @Parameter(description = "是否归档") @RequestParam(required = false) Boolean archived,
            @ModelAttribute PageQuery pageQuery) {
        return ApiResponse.success(activityService.getActivityList(pageQuery, archived));
    }

    /**
     * 根据社团ID查询活动列表
     */
    @Operation(summary = "根据社团ID查询活动列表", description = "获取指定社团的所有活动")
    @GetMapping("/club/{clubId}")
    public ApiResponse<Page<Activity>> getActivityListByClub(
            @Parameter(description = "社团ID") @PathVariable Long clubId,
            @ModelAttribute PageQuery pageQuery) {
        return ApiResponse.success(activityService.getActivityListByClub(clubId, pageQuery));
    }

    /**
     * 生成签到二维码
     */
    @Operation(summary = "生成签到二维码", description = "生成活动签到用的二维码")
    @PostMapping("/{id}/qrcode")
    public ApiResponse<String> generateQrCode(
            @Parameter(description = "活动ID") @PathVariable Long id,
            @Parameter(description = "有效时间（分钟）") @RequestParam(required = false, defaultValue = "30") Integer validMinutes,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(activityService.generateSignInQrCode(id, userId, validMinutes));
    }

    /**
     * 刷新签到二维码
     */
    @Operation(summary = "刷新签到二维码", description = "刷新活动签到二维码，使之前的二维码失效")
    @PutMapping("/{id}/qrcode")
    public ApiResponse<String> refreshQrCode(
            @Parameter(description = "活动ID") @PathVariable Long id,
            @Parameter(description = "有效时间（分钟）") @RequestParam(required = false, defaultValue = "30") Integer validMinutes,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(activityService.refreshSignInQrCode(id, userId, validMinutes));
    }

    /**
     * 获取签到二维码
     */
    @Operation(summary = "获取签到二维码", description = "获取已生成的签到二维码")
    @GetMapping("/{id}/qrcode")
    public ApiResponse<String> getQrCode(
            @Parameter(description = "活动ID") @PathVariable Long id) {
        return ApiResponse.success(activityService.getQrCodeToken(id));
    }

    /**
     * 归档活动
     */
    @Operation(summary = "归档活动", description = "将已结束的活动归档")
    @PostMapping("/{id}/archive")
    public ApiResponse<Activity> archiveActivity(
            @Parameter(description = "活动ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("活动归档成功", activityService.archiveActivity(id, userId));
    }

    /**
     * 获取签到统计
     */
    @Operation(summary = "获取签到统计", description = "获取活动的签到统计数据")
    @GetMapping("/{id}/signin/statistics")
    public ApiResponse<Object> getSignInStatistics(
            @Parameter(description = "活动ID") @PathVariable Long id) {
        return ApiResponse.success(activityService.getSignInStatistics(id));
    }
}
