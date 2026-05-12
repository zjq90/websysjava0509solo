package com.hospital.finance.controller;

import com.hospital.finance.entity.FinancialReport;
import com.hospital.finance.service.FinancialReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 财务报表控制器
 */
@RestController
@RequestMapping("/api/financial-reports")
@Tag(name = "财务报表管理", description = "财务日报、月报的生成、审核、结账等接口")
@CrossOrigin(origins = "*")
public class FinancialReportController {

    @Autowired
    private FinancialReportService reportService;

    @PostMapping("/daily")
    @Operation(summary = "生成日报表", description = "生成指定日期的财务日报表")
    public ResponseEntity<FinancialReport> generateDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
        return ResponseEntity.ok(reportService.generateDailyReport(reportDate));
    }

    @PostMapping("/monthly")
    @Operation(summary = "生成月报表", description = "生成指定月份的财务月报表")
    public ResponseEntity<FinancialReport> generateMonthlyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
        return ResponseEntity.ok(reportService.generateMonthlyReport(reportDate));
    }

    @PostMapping("/{id}/audit")
    @Operation(summary = "审核报表", description = "审核财务报表")
    public ResponseEntity<FinancialReport> auditReport(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String auditor = request.get("auditor");
        return ResponseEntity.ok(reportService.auditReport(id, auditor));
    }

    @PostMapping("/{id}/close")
    @Operation(summary = "结账", description = "完成财务报表结账")
    public ResponseEntity<FinancialReport> closeReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.closeReport(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询报表", description = "根据ID查询财务报表")
    public ResponseEntity<FinancialReport> findById(@PathVariable Long id) {
        return reportService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "查询所有报表", description = "获取所有财务报表列表")
    public ResponseEntity<List<FinancialReport>> findAll() {
        return ResponseEntity.ok(reportService.findAll());
    }

    @GetMapping("/type/{reportType}")
    @Operation(summary = "根据类型查询", description = "根据报表类型查询财务报表")
    public ResponseEntity<List<FinancialReport>> findByReportType(@PathVariable String reportType) {
        return ResponseEntity.ok(reportService.findByReportType(reportType));
    }
}