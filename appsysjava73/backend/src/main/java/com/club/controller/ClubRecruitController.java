package com.club.controller;

import com.club.common.PageResult;
import com.club.common.Result;
import com.club.entity.ClubRecruit;
import com.club.entity.ClubRecruitApply;
import com.club.entity.enums.ApplyStatusEnum;
import com.club.service.ClubRecruitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/clubs/{clubId}/recruit")
@Tag(name = "社团招新", description = "社团招新管理相关接口")
public class ClubRecruitController {

    @Autowired
    private ClubRecruitService clubRecruitService;

    @PostMapping
    @Operation(summary = "创建招新通道")
    public Result<ClubRecruit> createRecruit(
            @PathVariable Long clubId,
            @RequestBody ClubRecruit recruit,
            @RequestAttribute("userId") Long userId) {
        recruit.setClubId(clubId);
        ClubRecruit saved = clubRecruitService.createRecruit(recruit, userId);
        return Result.success(saved);
    }

    @PutMapping("/{recruitId}")
    @Operation(summary = "更新招新信息")
    public Result<ClubRecruit> updateRecruit(
            @PathVariable Long clubId,
            @PathVariable Long recruitId,
            @RequestBody ClubRecruit recruit,
            @RequestAttribute("userId") Long userId) {
        recruit.setClubId(clubId);
        ClubRecruit updated = clubRecruitService.updateRecruit(recruitId, recruit, userId);
        return Result.success(updated);
    }

    @PutMapping("/{recruitId}/close")
    @Operation(summary = "关闭招新通道")
    public Result<Void> closeRecruit(
            @PathVariable Long clubId,
            @PathVariable Long recruitId,
            @RequestAttribute("userId") Long userId) {
        clubRecruitService.closeRecruit(recruitId, userId);
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "获取招新列表")
    public Result<PageResult<ClubRecruit>> getRecruitList(
            @PathVariable Long clubId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        PageResult<ClubRecruit> page = clubRecruitService.getRecruitList(pageNum, pageSize, clubId, status);
        return Result.success(page);
    }

    @GetMapping("/{recruitId}")
    @Operation(summary = "获取招新详情")
    public Result<Map<String, Object>> getRecruitDetail(@PathVariable Long clubId, @PathVariable Long recruitId) {
        Map<String, Object> detail = clubRecruitService.getRecruitDetail(recruitId);
        return Result.success(detail);
    }

    @PostMapping("/{recruitId}/apply")
    @Operation(summary = "提交入团申请")
    public Result<ClubRecruitApply> submitApply(
            @PathVariable Long clubId,
            @PathVariable Long recruitId,
            @RequestBody ClubRecruitApply apply,
            @RequestAttribute("userId") Long userId) {
        apply.setRecruitId(recruitId);
        apply.setClubId(clubId);
        ClubRecruitApply saved = clubRecruitService.submitApply(apply, userId);
        return Result.success(saved);
    }

    @PutMapping("/apply/{applyId}/review")
    @Operation(summary = "审核入团申请")
    public Result<Void> reviewApply(
            @PathVariable Long clubId,
            @PathVariable Long applyId,
            @RequestBody Map<String, Object> reviewData,
            @RequestAttribute("userId") Long userId) {
        String statusStr = (String) reviewData.get("status");
        ApplyStatusEnum status = ApplyStatusEnum.valueOf(statusStr);
        String reviewMessage = (String) reviewData.get("reviewMessage");
        clubRecruitService.reviewApply(applyId, status, reviewMessage, userId);
        return Result.success();
    }

    @GetMapping("/apply/list")
    @Operation(summary = "获取申请列表")
    public Result<PageResult<ClubRecruitApply>> getApplyList(
            @PathVariable Long clubId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestAttribute("userId") Long userId) {
        ApplyStatusEnum statusEnum = status != null ? ApplyStatusEnum.valueOf(status) : null;
        PageResult<ClubRecruitApply> page = clubRecruitService.getApplyList(pageNum, pageSize, clubId, statusEnum, userId);
        return Result.success(page);
    }

    @GetMapping("/apply/my")
    @Operation(summary = "获取我的申请")
    public Result<PageResult<ClubRecruitApply>> getMyApplyList(
            @PathVariable Long clubId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestAttribute("userId") Long userId) {
        PageResult<ClubRecruitApply> page = clubRecruitService.getMyApplyList(pageNum, pageSize, userId);
        return Result.success(page);
    }

    @GetMapping("/apply/export")
    @Operation(summary = "导出申请名单")
    public Result<String> exportApplyList(
            @PathVariable Long clubId,
            @RequestParam(required = false) Integer status,
            @RequestAttribute("userId") Long userId) {
        String data = clubRecruitService.exportApplyList(clubId, status, userId);
        return Result.success(data);
    }
}
