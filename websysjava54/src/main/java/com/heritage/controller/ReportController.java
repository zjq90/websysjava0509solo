package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.Report;
import com.heritage.enums.AuditStatus;
import com.heritage.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@Tag(name = "举报管理", description = "举报处理相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    @Operation(summary = "获取所有举报")
    public Result<List<Report>> findAll() {
        return Result.success(reportService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取举报")
    public Result<Report> findById(@PathVariable Long id) {
        return reportService.findById(id)
                .map(Result::success)
                .orElse(Result.error("举报不存在"));
    }

    @PostMapping
    @Operation(summary = "创建举报")
    public Result<Report> create(@RequestBody Report report) {
        return Result.success(reportService.createReport(report));
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待处理举报列表")
    public Result<List<Report>> getPendingReports() {
        return Result.success(reportService.findPendingReports());
    }

    @GetMapping("/type/{reportType}")
    @Operation(summary = "按类型获取举报")
    public Result<List<Report>> getByType(@PathVariable String reportType) {
        return Result.success(reportService.findByType(reportType));
    }

    @PostMapping("/{id}/handle")
    @Operation(summary = "处理举报")
    public Result<Report> handleReport(@PathVariable Long id,
                                        @RequestParam AuditStatus status,
                                        @RequestParam(required = false) String remark,
                                        @RequestParam(required = false) Long handlerId) {
        return Result.success(reportService.handleReport(id, status, remark, handlerId));
    }
}
