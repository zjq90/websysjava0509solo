package com.bikesystem.controller;

import com.bikesystem.common.Result;
import com.bikesystem.dto.FaultReportRequest;
import com.bikesystem.entity.FaultReport;
import com.bikesystem.service.FaultReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 故障上报控制器
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/fault")
@Tag(name = "故障上报模块", description = "车辆故障上报相关接口")
@Validated
public class FaultReportController {

    @Resource
    private FaultReportService faultReportService;

    @PostMapping("/report")
    @Operation(summary = "提交故障上报", description = "上报车辆故障问题")
    public Result<FaultReport> submitReport(@Valid @RequestBody FaultReportRequest request) {
        return Result.success(faultReportService.submitReport(request));
    }

    @GetMapping("/my-reports")
    @Operation(summary = "我的上报记录", description = "获取我提交的故障上报列表")
    public Result<List<FaultReport>> getMyReports(
            @Parameter(description = "页码") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer pageSize) {
        return Result.success(faultReportService.getMyReports(pageNum, pageSize));
    }

    @GetMapping("/report/{reportId}")
    @Operation(summary = "上报详情", description = "获取故障上报详情")
    public Result<FaultReport> getReportDetail(
            @Parameter(description = "上报ID", required = true) @PathVariable Long reportId) {
        return Result.success(faultReportService.getReportDetail(reportId));
    }

    @PostMapping("/report/process")
    @Operation(summary = "处理上报(后台)", description = "运维人员处理故障上报")
    public Result<Void> processReport(
            @Parameter(description = "上报ID", required = true) @RequestParam Long reportId,
            @Parameter(description = "处理状态: RESOLVED/REJECTED", required = true) @RequestParam String status,
            @Parameter(description = "奖励金额") @RequestParam(required = false) BigDecimal rewardAmount,
            @Parameter(description = "处理备注") @RequestParam(required = false) String remark) {
        faultReportService.processReport(reportId, status, rewardAmount, remark);
        return Result.success();
    }
}
