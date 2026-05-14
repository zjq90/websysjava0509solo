package com.ops.controller;

import com.ops.common.Result;
import com.ops.service.AnalyticsService;
import com.ops.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据分析控制器
 * 提供数据分析和报表导出API
 * 
 * @author ops-admin
 */
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "数据分析", description = "服务效能监控、用户行为分析、报表导出")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/metrics")
    @Operation(summary = "获取服务效能统计")
    public Result<Map<String, Object>> getServiceMetrics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return Result.success(analyticsService.getServiceMetrics(start, end));
    }

    @GetMapping("/user-behavior")
    @Operation(summary = "获取用户行为分析")
    public Result<Map<String, Object>> getUserBehaviorAnalysis() {
        return Result.success(analyticsService.getUserBehaviorAnalysis());
    }

    @GetMapping("/order-trend")
    @Operation(summary = "获取工单趋势数据")
    public Result<List<Map<String, Object>>> getOrderTrend(@RequestParam(defaultValue = "7") int days) {
        return Result.success(analyticsService.getOrderTrend(days));
    }

    @GetMapping("/report/daily-pdf")
    @Operation(summary = "导出每日报表(PDF)")
    public ResponseEntity<byte[]> downloadDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            byte[] pdfContent = reportService.generateDailyReport(date);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "daily-report.pdf");
            return ResponseEntity.ok().headers(headers).body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/report/workorder-excel")
    @Operation(summary = "导出工单数据(Excel)")
    public ResponseEntity<byte[]> downloadWorkOrderExcel() {
        try {
            byte[] excelContent = reportService.generateWorkOrderExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "workorders.xlsx");
            return ResponseEntity.ok().headers(headers).body(excelContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
