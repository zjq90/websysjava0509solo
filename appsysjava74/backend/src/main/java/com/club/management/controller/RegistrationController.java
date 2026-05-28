package com.club.management.controller;

import com.club.management.common.ApiResponse;
import com.club.management.common.PageQuery;
import com.club.management.dto.AuditDTO;
import com.club.management.dto.RegistrationDTO;
import com.club.management.entity.Registration;
import com.club.management.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
 * 报名管理控制器
 * 
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "报名管理", description = "活动报名、审核、取消、导出等接口")
@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * 用户报名活动
     */
    @Operation(summary = "用户报名活动", description = "学生在线报名活动")
    @PostMapping
    public ApiResponse<Registration> register(
            @Valid @RequestBody RegistrationDTO dto,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("报名成功", registrationService.register(dto, userId));
    }

    /**
     * 审核报名
     */
    @Operation(summary = "审核报名", description = "社团管理员审核报名申请")
    @PostMapping("/audit")
    public ApiResponse<Registration> auditRegistration(
            @Valid @RequestBody AuditDTO dto,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("审核完成", registrationService.auditRegistration(dto, userId));
    }

    /**
     * 批量审核报名
     */
    @Operation(summary = "批量审核报名", description = "批量审核报名申请")
    @PostMapping("/audit/batch")
    public ApiResponse<Void> batchAudit(
            @Valid @RequestBody List<AuditDTO> dtoList,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        registrationService.batchAudit(dtoList, userId);
        return ApiResponse.success("批量审核完成", null);
    }

    /**
     * 取消报名
     */
    @Operation(summary = "取消报名", description = "用户取消已报名的活动")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> cancelRegistration(
            @Parameter(description = "报名记录ID") @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        registrationService.cancelRegistration(id, userId);
        return ApiResponse.success("取消报名成功", null);
    }

    /**
     * 获取报名详情
     */
    @Operation(summary = "获取报名详情", description = "根据ID获取报名详情")
    @GetMapping("/{id}")
    public ApiResponse<Registration> getRegistrationById(
            @Parameter(description = "报名记录ID") @PathVariable Long id) {
        return ApiResponse.success(registrationService.getRegistrationById(id));
    }

    /**
     * 分页查询活动报名列表
     */
    @Operation(summary = "查询活动报名列表", description = "分页查询指定活动的报名记录")
    @GetMapping("/activity/{activityId}")
    public ApiResponse<Page<Registration>> getRegistrationListByActivity(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @ModelAttribute PageQuery pageQuery) {
        return ApiResponse.success(registrationService.getRegistrationList(activityId, pageQuery));
    }

    /**
     * 分页查询用户报名列表
     */
    @Operation(summary = "查询用户报名列表", description = "分页查询当前用户的报名记录")
    @GetMapping("/my")
    public ApiResponse<Page<Registration>> getMyRegistrationList(
            @ModelAttribute PageQuery pageQuery,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(registrationService.getUserRegistrationList(userId, pageQuery));
    }

    /**
     * 导出报名名单
     */
    @Operation(summary = "导出报名名单", description = "导出活动报名名单为Excel")
    @GetMapping("/activity/{activityId}/export")
    public ResponseEntity<byte[]> exportRegistrationList(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Parameter(description = "院系筛选") @RequestParam(required = false) String department,
            @Parameter(description = "学号筛选") @RequestParam(required = false) String studentNo) {
        byte[] excelBytes = registrationService.exportRegistrationList(activityId, department, studentNo);
        String fileName = "报名名单_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
