package com.appsys.report.controller;

import com.appsys.common.result.Result;
import com.appsys.report.dto.ReportDTO;
import com.appsys.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报表控制器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Tag(name = "报表管理", description = "报表查询接口")
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取综合报表数据
     */
    @Operation(summary = "获取综合报表", description = "获取库存、订单、田间记录的综合统计数据")
    @GetMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_KEEPER', 'SALESMAN', 'AGRICULTURAL_TECHNICIAN', 'MANAGER', 'ADMIN')")
    public Result<ReportDTO> getReport() {
        ReportDTO report = reportService.getReport();
        return Result.success(report);
    }
}
