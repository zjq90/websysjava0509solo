package com.club.management.controller;

import com.club.management.common.ApiResponse;
import com.club.management.dto.SignInDTO;
import com.club.management.entity.SignIn;
import com.club.management.service.SignInService;
import com.club.management.vo.SignInStatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 签到管理控制器
 * 
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "签到管理", description = "扫码签到、手动补签、签到统计、导出等接口")
@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class SignInController {

    private final SignInService signInService;

    /**
     * 用户扫码签到
     */
    @Operation(summary = "扫码签到", description = "学生扫码完成签到")
    @PostMapping
    public ApiResponse<SignIn> signIn(
            @Valid @RequestBody SignInDTO dto,
            Authentication authentication,
            HttpServletRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("签到成功", signInService.signIn(dto, userId, request));
    }

    /**
     * 手动补签
     */
    @Operation(summary = "手动补签", description = "管理员对未签到成员进行补签")
    @PostMapping("/makeup")
    public ApiResponse<SignIn> makeUpSignIn(
            @Parameter(description = "活动ID") @RequestParam Long activityId,
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "补签原因") @RequestParam(required = false) String reason,
            Authentication authentication) {
        Long operatorId = (Long) authentication.getPrincipal();
        return ApiResponse.success("补签成功", signInService.makeUpSignIn(activityId, userId, reason, operatorId));
    }

    /**
     * 获取活动签到列表
     */
    @Operation(summary = "获取签到列表", description = "获取活动的所有签到记录")
    @GetMapping("/activity/{activityId}")
    public ApiResponse<List<SignIn>> getSignInList(
            @Parameter(description = "活动ID") @PathVariable Long activityId) {
        return ApiResponse.success(signInService.getSignInList(activityId));
    }

    /**
     * 获取签到统计
     */
    @Operation(summary = "获取签到统计", description = "获取活动的签到统计数据")
    @GetMapping("/activity/{activityId}/statistics")
    public ApiResponse<SignInStatisticsVO> getSignInStatistics(
            @Parameter(description = "活动ID") @PathVariable Long activityId) {
        return ApiResponse.success(signInService.getSignInStatistics(activityId));
    }

    /**
     * 导出签到表
     */
    @Operation(summary = "导出签到表", description = "导出活动签到表为Excel")
    @GetMapping("/activity/{activityId}/export")
    public ResponseEntity<byte[]> exportSignInList(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Parameter(description = "院系筛选") @RequestParam(required = false) String department,
            @Parameter(description = "学号筛选") @RequestParam(required = false) String studentNo) {
        byte[] excelBytes = signInService.exportSignInList(activityId, department, studentNo);
        String fileName = "签到表_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    /**
     * 批量标记缺席
     */
    @Operation(summary = "批量标记缺席", description = "活动结束后批量标记未签到人员为缺席")
    @PostMapping("/activity/{activityId}/mark-absent")
    public ApiResponse<Void> markAbsent(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            Authentication authentication) {
        Long operatorId = (Long) authentication.getPrincipal();
        signInService.markAbsent(activityId, operatorId);
        return ApiResponse.success("标记完成", null);
    }
}
