package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@Tag(name = "数据统计", description = "交易额趋势、商品销量排行、用户增长等统计接口")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/transaction/daily")
    @Operation(summary = "日交易额趋势")
    public Result<Map<String, Object>> getDailyTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(statisticsService.getDailyTrend(startDate, endDate));
    }

    @GetMapping("/transaction/weekly")
    @Operation(summary = "周交易额趋势")
    public Result<Map<String, Object>> getWeeklyTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(statisticsService.getWeeklyTrend(startDate, endDate));
    }

    @GetMapping("/transaction/monthly")
    @Operation(summary = "月交易额趋势")
    public Result<Map<String, Object>> getMonthlyTrend(@RequestParam int year) {
        return Result.success(statisticsService.getMonthlyTrend(year));
    }

    @GetMapping("/products/ranking")
    @Operation(summary = "商品销量排行榜")
    public Result<List<Map<String, Object>>> getProductSalesRanking(@RequestParam(defaultValue = "10") int topN) {
        return Result.success(statisticsService.getProductSalesRanking(topN));
    }

    @GetMapping("/users/growth")
    @Operation(summary = "用户增长分析")
    public Result<Map<String, Object>> getUserGrowthAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(statisticsService.getUserGrowthAnalysis(startDate, endDate));
    }

    @PostMapping("/export")
    @Operation(summary = "导出统计数据")
    public ResponseEntity<byte[]> exportStatistics(@RequestBody Map<String, Object> statistics) throws IOException {
        byte[] excelBytes = statisticsService.exportStatisticsToExcel(statistics);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "statistics.xlsx");
        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }

}