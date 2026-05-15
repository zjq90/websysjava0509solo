package com.referee.controller;

import com.referee.common.Result;
import com.referee.entity.Score;
import com.referee.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评分控制器
 *
 * @author Referee System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/score")
@Tag(name = "评分管理", description = "运动员评分的提交、审核等接口")
@CrossOrigin(origins = "*")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    /**
     * 获取所有评分列表
     */
    @GetMapping
    @Operation(summary = "获取所有评分", description = "获取系统中所有评分的列表")
    public Result<List<Score>> getAllScores() {
        return scoreService.getAllScores();
    }

    /**
     * 根据ID获取评分信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取评分信息", description = "根据ID获取评分详细信息")
    public Result<Score> getScoreById(
            @Parameter(description = "评分ID") @PathVariable Long id) {
        return scoreService.getScoreById(id);
    }

    /**
     * 提交评分
     */
    @PostMapping
    @Operation(summary = "提交评分", description = "裁判对运动员进行评分")
    public Result<Score> submitScore(@RequestBody Score score) {
        return scoreService.submitScore(score);
    }

    /**
     * 更新评分
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新评分", description = "修改已提交但未审核的评分")
    public Result<Score> updateScore(
            @Parameter(description = "评分ID") @PathVariable Long id,
            @RequestBody Score score) {
        return scoreService.updateScore(id, score);
    }

    /**
     * 审核评分
     */
    @PostMapping("/{id}/audit")
    @Operation(summary = "审核评分", description = "裁判长对评分进行审核（通过或驳回）")
    public Result<String> auditScore(
            @Parameter(description = "评分ID") @PathVariable Long id,
            @Parameter(description = "审核状态：1-通过，2-驳回") @RequestParam Integer auditStatus,
            @Parameter(description = "审核人ID") @RequestParam Long auditorId,
            @Parameter(description = "审核意见") @RequestParam(required = false) String auditComment) {
        return scoreService.auditScore(id, auditStatus, auditorId, auditComment);
    }

    /**
     * 根据比赛ID获取评分列表
     */
    @GetMapping("/competition/{competitionId}")
    @Operation(summary = "获取比赛评分", description = "获取指定比赛的所有评分")
    public Result<List<Score>> getScoresByCompetition(
            @Parameter(description = "比赛ID") @PathVariable Long competitionId) {
        return scoreService.getScoresByCompetition(competitionId);
    }

    /**
     * 根据运动员ID获取评分列表
     */
    @GetMapping("/athlete/{athleteId}")
    @Operation(summary = "获取运动员评分", description = "获取指定运动员的所有评分")
    public Result<List<Score>> getScoresByAthlete(
            @Parameter(description = "运动员ID") @PathVariable Long athleteId) {
        return scoreService.getScoresByAthlete(athleteId);
    }

    /**
     * 根据裁判ID获取评分列表
     */
    @GetMapping("/referee/{refereeId}")
    @Operation(summary = "获取裁判评分", description = "获取指定裁判提交的所有评分")
    public Result<List<Score>> getScoresByReferee(
            @Parameter(description = "裁判ID") @PathVariable Long refereeId) {
        return scoreService.getScoresByReferee(refereeId);
    }

    /**
     * 获取待审核评分列表
     */
    @GetMapping("/pending")
    @Operation(summary = "获取待审核评分", description = "获取所有待审核的评分列表")
    public Result<List<Score>> getPendingAuditScores() {
        return scoreService.getPendingAuditScores();
    }

    /**
     * 获取运动员最终得分
     */
    @GetMapping("/final-score")
    @Operation(summary = "获取最终得分", description = "获取运动员在某比赛中的最终平均分")
    public Result<Double> getFinalScore(
            @Parameter(description = "比赛ID") @RequestParam Long competitionId,
            @Parameter(description = "运动员ID") @RequestParam Long athleteId) {
        return scoreService.getFinalScore(competitionId, athleteId);
    }
}
