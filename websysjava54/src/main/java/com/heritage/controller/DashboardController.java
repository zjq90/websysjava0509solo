package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.Heritage;
import com.heritage.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "数据看板", description = "数据看板相关接口")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/statistics")
    @Operation(summary = "获取统计数据")
    public Result<Map<String, Object>> getStatistics() {
        return Result.success(dashboardService.getStatistics());
    }

    @GetMapping("/heritage/province")
    @Operation(summary = "获取文物按省份分布（热力图数据）")
    public Result<Map<String, Object>> getHeritageByProvince() {
        return Result.success(dashboardService.getHeritageDistributionByProvince());
    }

    @GetMapping("/heritage/dynasty")
    @Operation(summary = "获取文物按朝代分布")
    public Result<Map<String, Object>> getHeritageByDynasty() {
        return Result.success(dashboardService.getHeritageDistributionByDynasty());
    }

    @GetMapping("/heritage/material")
    @Operation(summary = "获取文物按材质分布")
    public Result<Map<String, Object>> getHeritageByMaterial() {
        return Result.success(dashboardService.getHeritageDistributionByMaterial());
    }

    @GetMapping("/heritage/province/{province}")
    @Operation(summary = "获取某省份的具体文物列表")
    public Result<List<Heritage>> getHeritageListByProvince(@PathVariable String province) {
        return Result.success(dashboardService.getHeritageByProvince(province));
    }

    @GetMapping("/user/province")
    @Operation(summary = "获取用户按省份分布")
    public Result<Map<String, Object>> getUserByProvince() {
        return Result.success(dashboardService.getUserDistributionByProvince());
    }

    @GetMapping("/user/credit-score")
    @Operation(summary = "获取用户信用分分布")
    public Result<Map<String, Object>> getCreditScoreDistribution() {
        return Result.success(dashboardService.getCreditScoreDistribution());
    }

    @GetMapping("/user/growth")
    @Operation(summary = "获取用户增长分析")
    public Result<Map<String, Object>> getUserGrowthAnalysis(@RequestParam(defaultValue = "7") int days) {
        return Result.success(dashboardService.getUserGrowthAnalysis(days));
    }

    @GetMapping("/user/monthly-growth")
    @Operation(summary = "获取用户月度增长")
    public Result<Map<String, Object>> getUserMonthlyGrowth() {
        return Result.success(dashboardService.getUserMonthlyGrowth());
    }
}
