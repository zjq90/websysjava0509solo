package com.gamesys.controller;

import com.gamesys.common.Result;
import com.gamesys.dto.DashboardStatsVO;
import com.gamesys.dto.TrendDataVO;
import com.gamesys.entity.DashboardConfig;
import com.gamesys.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "数据总览")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @ApiOperation("获取今日统计数据")
    @GetMapping("/stats")
    public Result<DashboardStatsVO> getTodayStats() {
        return Result.success(dashboardService.getTodayStats());
    }

    @ApiOperation("获取趋势数据")
    @GetMapping("/trend")
    public Result<TrendDataVO> getTrendData(@RequestParam(defaultValue = "7") Integer days) {
        return Result.success(dashboardService.getTrendData(days));
    }

    @ApiOperation("获取看板配置列表")
    @GetMapping("/configs")
    public Result<List<DashboardConfig>> getDashboardConfigs() {
        return Result.success(dashboardService.getDashboardConfigs());
    }

    @ApiOperation("保存看板配置")
    @PostMapping("/configs")
    public Result<DashboardConfig> saveDashboardConfig(@RequestBody DashboardConfig config) {
        return Result.success(dashboardService.saveDashboardConfig(config));
    }

    @ApiOperation("删除看板配置")
    @DeleteMapping("/configs/{id}")
    public Result<Void> deleteDashboardConfig(@PathVariable Long id) {
        dashboardService.deleteDashboardConfig(id);
        return Result.success();
    }

    @ApiOperation("设置默认看板")
    @PutMapping("/configs/{id}/default")
    public Result<Void> setDefaultDashboard(@PathVariable Long id) {
        dashboardService.setDefaultDashboard(id);
        return Result.success();
    }

    @ApiOperation("获取默认看板配置")
    @GetMapping("/configs/default")
    public Result<DashboardConfig> getDefaultDashboard() {
        return Result.success(dashboardService.getDefaultDashboard());
    }
}
