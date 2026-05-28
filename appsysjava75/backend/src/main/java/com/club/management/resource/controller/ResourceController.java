package com.club.management.resource.controller;

import com.club.management.common.result.PageResult;
import com.club.management.common.result.Result;
import com.club.management.resource.entity.CooperationApply;
import com.club.management.resource.entity.EnterpriseCooperation;
import com.club.management.resource.entity.ResourceShare;
import com.club.management.resource.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 资源共享Controller
 *
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "资源共享", description = "资料分享、校企对接等接口")
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @Operation(summary = "获取公开资源列表")
    @GetMapping("/share/list")
    public Result<PageResult<ResourceShare>> getResourceList(
            @Parameter(description = "分类 0-学习资料 1-比赛经验 2-技能教程 3-工具软件 4-其他") @RequestParam(required = false) Integer category,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(resourceService.getResourceList(category, pageNum, pageSize));
    }

    @Operation(summary = "获取社团资源列表")
    @GetMapping("/share/club/{clubId}")
    public Result<PageResult<ResourceShare>> getClubResourceList(
            @Parameter(description = "社团ID") @PathVariable Long clubId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(resourceService.getClubResourceList(clubId, pageNum, pageSize));
    }

    @Operation(summary = "获取我的资源列表")
    @GetMapping("/share/my/{uploaderId}")
    public Result<PageResult<ResourceShare>> getMyResourceList(
            @Parameter(description = "上传者ID") @PathVariable Long uploaderId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(resourceService.getMyResourceList(uploaderId, pageNum, pageSize));
    }

    @Operation(summary = "上传资源")
    @PostMapping("/share/upload")
    public Result<ResourceShare> uploadResource(@RequestBody ResourceShare resource) {
        return Result.success(resourceService.uploadResource(resource));
    }

    @Operation(summary = "获取资源详情")
    @GetMapping("/share/{resourceId}")
    public Result<ResourceShare> getResourceDetail(
            @Parameter(description = "资源ID") @PathVariable Long resourceId) {
        resourceService.increaseResourceView(resourceId);
        return Result.success(resourceService.getResourceDetail(resourceId));
    }

    @Operation(summary = "下载资源")
    @PostMapping("/share/download/{resourceId}")
    public Result<Void> downloadResource(
            @Parameter(description = "资源ID") @PathVariable Long resourceId) {
        resourceService.increaseResourceDownload(resourceId);
        return Result.success();
    }

    @Operation(summary = "删除资源")
    @DeleteMapping("/share/{resourceId}")
    public Result<Void> deleteResource(
            @Parameter(description = "资源ID") @PathVariable Long resourceId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        resourceService.deleteResource(resourceId, userId);
        return Result.success();
    }

    @Operation(summary = "获取校企对接列表")
    @GetMapping("/cooperation/list")
    public Result<PageResult<EnterpriseCooperation>> getCooperationList(
            @Parameter(description = "类型 0-实习信息 1-赞助信息 2-合作项目 3-招聘信息") @RequestParam(required = false) Integer type,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(resourceService.getCooperationList(type, pageNum, pageSize));
    }

    @Operation(summary = "发布校企对接信息")
    @PostMapping("/cooperation/publish")
    public Result<EnterpriseCooperation> publishCooperation(@RequestBody EnterpriseCooperation cooperation) {
        return Result.success(resourceService.publishCooperation(cooperation));
    }

    @Operation(summary = "获取校企对接详情")
    @GetMapping("/cooperation/{cooperationId}")
    public Result<EnterpriseCooperation> getCooperationDetail(
            @Parameter(description = "对接ID") @PathVariable Long cooperationId) {
        resourceService.increaseCooperationView(cooperationId);
        return Result.success(resourceService.getCooperationDetail(cooperationId));
    }

    @Operation(summary = "获取校企对接申请列表")
    @GetMapping("/cooperation/apply/list")
    public Result<PageResult<CooperationApply>> getCooperationApplyList(
            @Parameter(description = "对接ID") @RequestParam Long cooperationId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(resourceService.getCooperationApplyList(cooperationId, pageNum, pageSize));
    }

    @Operation(summary = "获取我的申请列表")
    @GetMapping("/cooperation/apply/my/{applicantId}")
    public Result<PageResult<CooperationApply>> getMyApplyList(
            @Parameter(description = "申请人ID") @PathVariable Long applicantId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(resourceService.getMyApplyList(applicantId, pageNum, pageSize));
    }

    @Operation(summary = "提交校企对接申请")
    @PostMapping("/cooperation/apply/submit")
    public Result<CooperationApply> submitCooperationApply(@RequestBody CooperationApply apply) {
        return Result.success(resourceService.submitCooperationApply(apply));
    }

    @Operation(summary = "审批校企对接申请")
    @PostMapping("/cooperation/apply/audit")
    public Result<CooperationApply> auditCooperationApply(@RequestBody Map<String, Object> params) {
        Long applyId = Long.valueOf(params.get("applyId").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        Long auditorId = Long.valueOf(params.get("auditorId").toString());
        String auditorName = (String) params.get("auditorName");
        String auditOpinion = (String) params.get("auditOpinion");
        return Result.success(resourceService.auditCooperationApply(applyId, status, auditorId, auditorName, auditOpinion));
    }

    @Operation(summary = "获取校企对接申请详情")
    @GetMapping("/cooperation/apply/{applyId}")
    public Result<CooperationApply> getCooperationApplyDetail(
            @Parameter(description = "申请ID") @PathVariable Long applyId) {
        return Result.success(resourceService.getCooperationApplyDetail(applyId));
    }
}
