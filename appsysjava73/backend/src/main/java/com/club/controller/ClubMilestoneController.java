package com.club.controller;

import com.club.common.Result;
import com.club.entity.ClubMilestone;
import com.club.entity.PastPresident;
import com.club.service.ClubMilestoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社团大事记控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/clubs/{clubId}/milestones")
@Tag(name = "社团大事记", description = "社团发展历程、历任社长相关接口")
public class ClubMilestoneController {

    @Autowired
    private ClubMilestoneService clubMilestoneService;

    /**
     * 获取大事记列表
     */
    @GetMapping
    @Operation(summary = "获取大事记列表", description = "获取社团发展大事记列表")
    public Result<List<ClubMilestone>> getMilestoneList(@PathVariable Long clubId) {
        List<ClubMilestone> milestones = clubMilestoneService.getMilestoneList(clubId);
        return Result.success(milestones);
    }

    /**
     * 添加大事记
     */
    @PostMapping
    @Operation(summary = "添加大事记", description = "添加社团发展重要节点")
    public Result<ClubMilestone> createMilestone(
            @PathVariable Long clubId,
            @RequestBody ClubMilestone milestone,
            @RequestAttribute("userId") Long userId) {
        milestone.setClubId(clubId);
        ClubMilestone saved = clubMilestoneService.createMilestone(clubId, milestone, userId);
        return Result.success(saved);
    }

    /**
     * 更新大事记
     */
    @PutMapping("/{milestoneId}")
    @Operation(summary = "更新大事记", description = "更新大事记信息")
    public Result<ClubMilestone> updateMilestone(
            @PathVariable Long clubId,
            @PathVariable Long milestoneId,
            @RequestBody ClubMilestone milestone,
            @RequestAttribute("userId") Long userId) {
        milestone.setClubId(clubId);
        milestone.setId(milestoneId);
        ClubMilestone updated = clubMilestoneService.updateMilestone(clubId, milestoneId, milestone, userId);
        return Result.success(updated);
    }

    /**
     * 删除大事记
     */
    @DeleteMapping("/{milestoneId}")
    @Operation(summary = "删除大事记", description = "删除大事记")
    public Result<Void> deleteMilestone(
            @PathVariable Long clubId,
            @PathVariable Long milestoneId,
            @RequestAttribute("userId") Long userId) {
        clubMilestoneService.deleteMilestone(clubId, milestoneId, userId);
        return Result.success();
    }

    /**
     * 获取历任社长列表
     */
    @GetMapping("/presidents")
    @Operation(summary = "获取历任社长", description = "获取社团历任社长信息列表")
    public Result<List<PastPresident>> getPastPresidentList(@PathVariable Long clubId) {
        List<PastPresident> presidents = clubMilestoneService.getPastPresidentList(clubId);
        return Result.success(presidents);
    }

    /**
     * 添加历任社长
     */
    @PostMapping("/presidents")
    @Operation(summary = "添加历任社长", description = "添加历任社长信息")
    public Result<PastPresident> createPastPresident(
            @PathVariable Long clubId,
            @RequestBody PastPresident president,
            @RequestAttribute("userId") Long userId) {
        president.setClubId(clubId);
        PastPresident saved = clubMilestoneService.createPastPresident(clubId, president, userId);
        return Result.success(saved);
    }

    /**
     * 更新历任社长
     */
    @PutMapping("/presidents/{presidentId}")
    @Operation(summary = "更新历任社长", description = "更新历任社长信息")
    public Result<PastPresident> updatePastPresident(
            @PathVariable Long clubId,
            @PathVariable Long presidentId,
            @RequestBody PastPresident president,
            @RequestAttribute("userId") Long userId) {
        president.setClubId(clubId);
        president.setId(presidentId);
        PastPresident updated = clubMilestoneService.updatePastPresident(clubId, presidentId, president, userId);
        return Result.success(updated);
    }

    /**
     * 删除历任社长
     */
    @DeleteMapping("/presidents/{presidentId}")
    @Operation(summary = "删除历任社长", description = "删除历任社长信息")
    public Result<Void> deletePastPresident(
            @PathVariable Long clubId,
            @PathVariable Long presidentId,
            @RequestAttribute("userId") Long userId) {
        clubMilestoneService.deletePastPresident(clubId, presidentId, userId);
        return Result.success();
    }
}
