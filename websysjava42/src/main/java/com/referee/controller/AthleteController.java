package com.referee.controller;

import com.referee.common.Result;
import com.referee.entity.Athlete;
import com.referee.service.AthleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运动员控制器
 *
 * @author Referee System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/athlete")
@Tag(name = "运动员管理", description = "运动员信息的增删改查接口")
@CrossOrigin(origins = "*")
public class AthleteController {

    @Autowired
    private AthleteService athleteService;

    /**
     * 获取所有运动员列表
     */
    @GetMapping
    @Operation(summary = "获取所有运动员", description = "获取系统中所有运动员的列表")
    public Result<List<Athlete>> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    /**
     * 根据ID获取运动员信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取运动员信息", description = "根据ID获取运动员详细信息")
    public Result<Athlete> getAthleteById(
            @Parameter(description = "运动员ID") @PathVariable Long id) {
        return athleteService.getAthleteById(id);
    }

    /**
     * 新增运动员
     */
    @PostMapping
    @Operation(summary = "新增运动员", description = "添加新的运动员信息，并自动创建登录账号")
    public Result<Athlete> addAthlete(@RequestBody Athlete athlete) {
        return athleteService.addAthlete(athlete);
    }

    /**
     * 更新运动员信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新运动员", description = "更新运动员信息")
    public Result<Athlete> updateAthlete(
            @Parameter(description = "运动员ID") @PathVariable Long id,
            @RequestBody Athlete athlete) {
        return athleteService.updateAthlete(id, athlete);
    }

    /**
     * 删除运动员
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除运动员", description = "删除运动员信息（逻辑删除）")
    public Result<String> deleteAthlete(
            @Parameter(description = "运动员ID") @PathVariable Long id) {
        return athleteService.deleteAthlete(id);
    }

    /**
     * 对运动员进行评价
     */
    @PostMapping("/{id}/evaluate")
    @Operation(summary = "评价运动员", description = "对运动员进行评价")
    public Result<String> evaluateAthlete(
            @Parameter(description = "运动员ID") @PathVariable Long id,
            @Parameter(description = "评价内容") @RequestParam String evaluation) {
        return athleteService.evaluateAthlete(id, evaluation);
    }

    /**
     * 搜索运动员
     */
    @GetMapping("/search")
    @Operation(summary = "搜索运动员", description = "根据关键词搜索运动员信息")
    public Result<List<Athlete>> searchAthletes(
            @Parameter(description = "搜索关键词") @RequestParam String keyword) {
        return athleteService.searchAthletes(keyword);
    }
}
