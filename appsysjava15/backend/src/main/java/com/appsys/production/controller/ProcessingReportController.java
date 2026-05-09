package com.appsys.production.controller;

import com.appsys.production.common.Result;
import com.appsys.production.entity.ProcessingReport;
import com.appsys.production.service.ProcessingReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "报告管理", description = "加工报告生成接口")
@CrossOrigin(origins = "*")
public class ProcessingReportController {

    @Autowired
    private ProcessingReportService reportService;

    @GetMapping("/batch/{batchId}")
    @Operation(summary = "查询批次报告", description = "根据批次ID获取加工报告")
    public Result<ProcessingReport> getByBatchId(@Parameter(description = "批次ID") @PathVariable Long batchId) {
        return Result.success(reportService.getByBatchId(batchId));
    }

    @GetMapping("/batchNo/{batchNo}")
    @Operation(summary = "根据批次号查询报告", description = "扫码查询：根据批次号获取加工报告")
    public Result<ProcessingReport> getByBatchNo(@Parameter(description = "批次编号") @PathVariable String batchNo) {
        return Result.success(reportService.getByBatchNo(batchNo));
    }

    @PostMapping("/generate/{batchId}")
    @Operation(summary = "生成加工报告", description = "系统自动生成加工报告，汇总所有环节和质检数据")
    public Result<ProcessingReport> generateReport(
            @Parameter(description = "批次ID") @PathVariable Long batchId,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long userId) {
        if (userId == null) {
            userId = 1L;
        }
        return Result.success(reportService.generateReport(batchId, userId));
    }
}
