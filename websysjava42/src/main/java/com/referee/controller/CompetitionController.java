package com.referee.controller;

import com.referee.common.Result;
import com.referee.entity.Competition;
import com.referee.entity.CompetitionAthlete;
import com.referee.service.CompetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 比赛控制器
 *
 * @author Referee System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/competition")
@Tag(name = "比赛管理", description = "比赛信息的增删改查及运动员报名管理")
@CrossOrigin(origins = "*")
public class CompetitionController {

    @Autowired
    private CompetitionService competitionService;

    /**
     * 获取所有比赛列表
     */
    @GetMapping
    @Operation(summary = "获取所有比赛", description = "获取系统中所有比赛的列表")
    public Result<List<Competition>> getAllCompetitions() {
        return competitionService.getAllCompetitions();
    }

    /**
     * 根据ID获取比赛信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取比赛信息", description = "根据ID获取比赛详细信息")
    public Result<Competition> getCompetitionById(
            @Parameter(description = "比赛ID") @PathVariable Long id) {
        return competitionService.getCompetitionById(id);
    }

    /**
     * 新增比赛
     */
    @PostMapping
    @Operation(summary = "新增比赛", description = "添加新的比赛信息")
    public Result<Competition> addCompetition(@RequestBody Competition competition) {
        return competitionService.addCompetition(competition);
    }

    /**
     * 更新比赛信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新比赛", description = "更新比赛信息")
    public Result<Competition> updateCompetition(
            @Parameter(description = "比赛ID") @PathVariable Long id,
            @RequestBody Competition competition) {
        return competitionService.updateCompetition(id, competition);
    }

    /**
     * 删除比赛
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除比赛", description = "删除比赛信息（逻辑删除）")
    public Result<String> deleteCompetition(
            @Parameter(description = "比赛ID") @PathVariable Long id) {
        return competitionService.deleteCompetition(id);
    }

    /**
     * 添加运动员到比赛
     */
    @PostMapping("/{competitionId}/athlete/{athleteId}")
    @Operation(summary = "添加运动员到比赛", description = "将运动员报名参加指定比赛")
    public Result<String> addAthleteToCompetition(
            @Parameter(description = "比赛ID") @PathVariable Long competitionId,
            @Parameter(description = "运动员ID") @PathVariable Long athleteId) {
        return competitionService.addAthleteToCompetition(competitionId, athleteId);
    }

    /**
     * 从比赛中移除运动员
     */
    @DeleteMapping("/{competitionId}/athlete/{athleteId}")
    @Operation(summary = "移除比赛中的运动员", description = "取消运动员的比赛报名")
    public Result<String> removeAthleteFromCompetition(
            @Parameter(description = "比赛ID") @PathVariable Long competitionId,
            @Parameter(description = "运动员ID") @PathVariable Long athleteId) {
        return competitionService.removeAthleteFromCompetition(competitionId, athleteId);
    }

    /**
     * 获取比赛的参赛运动员列表
     */
    @GetMapping("/{competitionId}/athletes")
    @Operation(summary = "获取比赛参赛运动员", description = "获取指定比赛的所有参赛运动员")
    public Result<List<CompetitionAthlete>> getCompetitionAthletes(
            @Parameter(description = "比赛ID") @PathVariable Long competitionId) {
        return competitionService.getCompetitionAthletes(competitionId);
    }

    /**
     * 获取运动员的参赛比赛列表
     */
    @GetMapping("/athlete/{athleteId}/competitions")
    @Operation(summary = "获取运动员参赛比赛", description = "获取指定运动员参加的所有比赛")
    public Result<List<CompetitionAthlete>> getAthleteCompetitions(
            @Parameter(description = "运动员ID") @PathVariable Long athleteId) {
        return competitionService.getAthleteCompetitions(athleteId);
    }

    /**
     * 运动员签到
     */
    @PostMapping("/{competitionId}/athlete/{athleteId}/checkin")
    @Operation(summary = "运动员签到", description = "比赛运动员进行签到")
    public Result<String> checkIn(
            @Parameter(description = "比赛ID") @PathVariable Long competitionId,
            @Parameter(description = "运动员ID") @PathVariable Long athleteId) {
        return competitionService.checkIn(competitionId, athleteId);
    }

    /**
     * 搜索比赛
     */
    @GetMapping("/search")
    @Operation(summary = "搜索比赛", description = "根据关键词搜索比赛信息")
    public Result<List<Competition>> searchCompetitions(
            @Parameter(description = "搜索关键词") @RequestParam String keyword) {
        return competitionService.searchCompetitions(keyword);
    }
}
