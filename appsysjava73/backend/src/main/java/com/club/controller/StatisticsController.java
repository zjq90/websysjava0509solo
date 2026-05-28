package com.club.controller;

import com.club.common.Result;
import com.club.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据统计控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/clubs/{clubId}/statistics")
@Tag(name = "数据统计", description = "社团数据统计、报告生成相关接口")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取社团基本统计数据
     */
    @GetMapping
    @Operation(summary = "获取基本统计", description = "获取社团基本统计数据（成员数、活动数等）")
    public Result<Map<String, Object>> getClubStatistics(
            @PathVariable Long clubId,
            @RequestAttribute("userId") Long userId) {
        Map<String, Object> stats = statisticsService.getClubStatistics(clubId, userId);
        return Result.success(stats);
    }

    /**
     * 获取成员增长趋势
     */
    @GetMapping("/member-growth")
    @Operation(summary = "获取成员增长趋势", description = "获取最近12个月的成员增长趋势数据")
    public Result<Map<String, Object>> getMemberGrowthTrend(
            @PathVariable Long clubId,
            @RequestAttribute("userId") Long userId) {
        Map<String, Object> trend = statisticsService.getMemberGrowthTrend(clubId, userId);
        return Result.success(trend);
    }

    /**
     * 获取活动参与率统计
     */
    @GetMapping("/activity-participation")
    @Operation(summary = "获取活动参与率", description = "获取最近6个月的活动参与率统计")
    public Result<Map<String, Object>> getActivityParticipationRate(
            @PathVariable Long clubId,
            @RequestAttribute("userId") Long userId) {
        Map<String, Object> rate = statisticsService.getActivityParticipationRate(clubId, userId);
        return Result.success(rate);
    }

    /**
     * 获取成员部门分布统计
     */
    @GetMapping("/department-distribution")
    @Operation(summary = "获取部门分布", description = "获取成员部门分布统计")
    public Result<Map<String, Object>> getMemberDepartmentDistribution(
            @PathVariable Long clubId,
            @RequestAttribute("userId") Long userId) {
        Map<String, Object> distribution = statisticsService.getMemberDepartmentDistribution(clubId, userId);
        return Result.success(distribution);
    }

    /**
     * 生成月度活动报告
     */
    @GetMapping("/monthly-report")
    @Operation(summary = "生成月度报告", description = "生成指定月份的社团活动报告")
    public Result<Map<String, Object>> generateMonthlyReport(
            @PathVariable Long clubId,
            @RequestParam int year,
            @RequestParam int month,
            @RequestAttribute("userId") Long userId) {
        Map<String, Object> report = statisticsService.generateMonthlyReport(clubId, year, month, userId);
        return Result.success(report);
    }
}
