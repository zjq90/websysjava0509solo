package com.club.controller;

import com.club.common.PageResult;
import com.club.common.Result;
import com.club.entity.ClubDepartment;
import com.club.entity.ClubMember;
import com.club.service.ClubDepartmentService;
import com.club.service.ClubMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 社团成员控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/clubs/{clubId}/members")
@Tag(name = "社团成员管理", description = "社团成员管理、部门管理相关接口")
public class ClubMemberController {

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private ClubDepartmentService clubDepartmentService;

    /**
     * 获取成员列表
     */
    @GetMapping
    @Operation(summary = "获取成员列表", description = "获取社团成员列表，支持按部门筛选")
    public Result<PageResult<ClubMember>> getMemberList(
            @PathVariable Long clubId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String role) {
        PageResult<ClubMember> page = clubMemberService.getMemberList(pageNum, pageSize, clubId, departmentId, role);
        return Result.success(page);
    }

    /**
     * 添加成员
     */
    @PostMapping
    @Operation(summary = "添加成员", description = "社团管理员手动添加成员")
    public Result<ClubMember> addMember(
            @PathVariable Long clubId,
            @RequestBody Map<String, Object> memberData,
            @RequestAttribute("userId") Long userId) {
        Long targetUserId = Long.valueOf(memberData.get("userId").toString());
        Long departmentId = memberData.get("departmentId") != null ? Long.valueOf(memberData.get("departmentId").toString()) : null;
        String role = (String) memberData.get("role");
        ClubMember member = clubMemberService.addMember(clubId, targetUserId, departmentId, role, userId);
        return Result.success(member);
    }

    /**
     * 更新成员信息
     */
    @PutMapping("/{memberId}")
    @Operation(summary = "更新成员信息", description = "更新成员的部门、职位等信息")
    public Result<ClubMember> updateMember(
            @PathVariable Long clubId,
            @PathVariable Long memberId,
            @RequestBody ClubMember member,
            @RequestAttribute("userId") Long userId) {
        member.setClubId(clubId);
        member.setId(memberId);
        ClubMember updated = clubMemberService.updateMember(member, userId);
        return Result.success(updated);
    }

    /**
     * 移除成员
     */
    @DeleteMapping("/{memberId}")
    @Operation(summary = "移除成员", description = "将成员移出社团")
    public Result<Void> removeMember(
            @PathVariable Long clubId,
            @PathVariable Long memberId,
            @RequestAttribute("userId") Long userId) {
        clubMemberService.removeMember(clubId, memberId, userId);
        return Result.success();
    }

    /**
     * 转让社长权限
     */
    @PutMapping("/transfer-president")
    @Operation(summary = "转让社长权限", description = "社长将权限转让给其他成员")
    public Result<Void> transferPresident(
            @PathVariable Long clubId,
            @RequestBody Map<String, Long> data,
            @RequestAttribute("userId") Long userId) {
        Long newPresidentId = data.get("newPresidentId");
        clubMemberService.transferPresident(clubId, newPresidentId, userId);
        return Result.success();
    }

    /**
     * 退出社团
     */
    @PostMapping("/quit")
    @Operation(summary = "退出社团", description = "成员主动退出社团")
    public Result<Void> quitClub(@PathVariable Long clubId, @RequestAttribute("userId") Long userId) {
        clubMemberService.quitClub(clubId, userId);
        return Result.success();
    }

    /**
     * 获取成员活动参与记录
     */
    @GetMapping("/{memberId}/activities")
    @Operation(summary = "获取成员活动记录", description = "获取成员的活动参与记录")
    public Result<Map<String, Object>> getMemberActivityRecords(
            @PathVariable Long clubId,
            @PathVariable Long memberId) {
        Map<String, Object> records = clubMemberService.getMemberActivityRecords(memberId);
        return Result.success(records);
    }

    /**
     * 标记成员活跃状态
     */
    @PutMapping("/{memberId}/active")
    @Operation(summary = "标记成员活跃状态", description = "标记成员为活跃/不活跃")
    public Result<Void> markMemberActive(
            @PathVariable Long clubId,
            @PathVariable Long memberId,
            @RequestBody Map<String, Integer> data,
            @RequestAttribute("userId") Long userId) {
        Integer active = data.get("active");
        clubMemberService.markMemberActive(memberId, active, userId);
        return Result.success();
    }

    /**
     * 批量导出成员名单
     */
    @GetMapping("/export")
    @Operation(summary = "导出成员名单", description = "批量导出成员名单，包含学号、联系方式、院系等信息")
    public Result<String> exportMemberList(
            @PathVariable Long clubId,
            @RequestAttribute("userId") Long userId) {
        String filePath = clubMemberService.exportMemberList(clubId, userId);
        return Result.success(filePath);
    }

    /**
     * 获取部门列表
     */
    @GetMapping("/departments")
    @Operation(summary = "获取部门列表", description = "获取社团的部门列表")
    public Result<List<ClubDepartment>> getDepartmentList(@PathVariable Long clubId) {
        List<ClubDepartment> departments = clubDepartmentService.getDepartmentList(clubId);
        return Result.success(departments);
    }

    /**
     * 创建部门
     */
    @PostMapping("/departments")
    @Operation(summary = "创建部门", description = "创建新的社团部门")
    public Result<ClubDepartment> createDepartment(
            @PathVariable Long clubId,
            @RequestBody ClubDepartment department,
            @RequestAttribute("userId") Long userId) {
        ClubDepartment saved = clubDepartmentService.createDepartment(clubId, department, userId);
        return Result.success(saved);
    }

    /**
     * 更新部门
     */
    @PutMapping("/departments/{departmentId}")
    @Operation(summary = "更新部门", description = "更新部门信息")
    public Result<ClubDepartment> updateDepartment(
            @PathVariable Long clubId,
            @PathVariable Long departmentId,
            @RequestBody ClubDepartment department,
            @RequestAttribute("userId") Long userId) {
        ClubDepartment updated = clubDepartmentService.updateDepartment(clubId, departmentId, department, userId);
        return Result.success(updated);
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/departments/{departmentId}")
    @Operation(summary = "删除部门", description = "删除社团部门")
    public Result<Void> deleteDepartment(
            @PathVariable Long clubId,
            @PathVariable Long departmentId,
            @RequestAttribute("userId") Long userId) {
        clubDepartmentService.deleteDepartment(clubId, departmentId, userId);
        return Result.success();
    }
}
