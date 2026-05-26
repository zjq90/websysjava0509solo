package com.personal.accounting.controller;

import com.personal.accounting.dto.*;
import com.personal.accounting.service.DashboardService;
import com.personal.accounting.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 仪表盘控制器
 * 提供仪表盘概览和数据分析API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "仪表盘管理", description = "仪表盘概览与数据分析API")
public class DashboardController {

    private final DashboardService dashboardService;
    private final ReportService reportService;

    @GetMapping("/overview")
    @Operation(summary = "获取仪表盘概览数据", description = "获取今日/本周/本月收支总额、支出占比、账户余额趋势")
    public ResponseEntity<DashboardOverviewDTO> getOverview() {
        return ResponseEntity.ok(dashboardService.getDashboardOverview());
    }

    @GetMapping("/monthly-trend")
    @Operation(summary = "获取月度消费趋势", description = "获取最近N个月的收支趋势对比")
    public ResponseEntity<List<MonthlyTrendDTO>> getMonthlyTrend(
            @RequestParam(defaultValue = "12") int months) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrend(months));
    }

    @GetMapping("/abnormal-transactions")
    @Operation(summary = "获取异常交易", description = "获取所有被标记为异常的交易记录")
    public ResponseEntity<?> getAbnormalTransactions() {
        return ResponseEntity.ok(dashboardService.getAbnormalTransactions());
    }

    @PostMapping("/detect-abnormal")
    @Operation(summary = "检测异常交易", description = "自动检测并标记异常交易（单笔>月均3倍）")
    public ResponseEntity<Void> detectAbnormalTransactions() {
        dashboardService.detectAbnormalTransactions();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/heatmap")
    @Operation(summary = "获取支出热力图", description = "获取按小时/星期分布的支出热力图数据")
    public ResponseEntity<List<HeatmapDataDTO>> getExpenseHeatmap(
            @RequestParam(defaultValue = "90") int days) {
        return ResponseEntity.ok(reportService.getExpenseHeatmap(days));
    }

    @GetMapping("/category-comparison")
    @Operation(summary = "获取分类对比表", description = "获取最近N个月的分类支出对比")
    public ResponseEntity<Map<String, Object>> getCategoryComparison(
            @RequestParam(defaultValue = "3") int months) {
        return ResponseEntity.ok(reportService.getCategoryComparison(months));
    }

    @GetMapping("/asset-trend")
    @Operation(summary = "获取资产趋势图", description = "获取总资产、净资产趋势数据")
    public ResponseEntity<Map<String, Object>> getAssetTrend(
            @RequestParam(defaultValue = "12") int months) {
        return ResponseEntity.ok(reportService.getAssetTrend(months));
    }

    @GetMapping("/top-spending")
    @Operation(summary = "获取最烧钱分类排名", description = "获取最近N个月支出最高的分类排名")
    public ResponseEntity<List<Map<String, Object>>> getTopSpendingCategories(
            @RequestParam(defaultValue = "3") int months) {
        return ResponseEntity.ok(reportService.getTopSpendingCategories(months));
    }
}
