package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.dto.ConversionFunnelDTO;
import com.photostudio.dto.EmployeePerformanceDTO;
import com.photostudio.dto.FinanceReportDTO;
import com.photostudio.dto.SalesDashboardDTO;
import com.photostudio.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 看板控制器
 * 提供销售业绩、转化漏斗、员工绩效、财务报表等API接口
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "看板管理", description = "销售业绩、转化漏斗、员工绩效、财务报表API")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取销售业绩看板数据
     */
    @GetMapping("/sales")
    @Operation(summary = "获取销售业绩看板", description = "获取总营收、订单量、客单价、转化率等核心指标")
    public Result<SalesDashboardDTO> getSalesDashboard(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long salesId,
            @RequestParam(required = false) String packageType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(dashboardService.getSalesDashboard(storeId, salesId, packageType, startDate, endDate));
    }

    /**
     * 获取客户转化漏斗数据
     */
    @GetMapping("/conversion-funnel")
    @Operation(summary = "获取客户转化漏斗", description = "分析咨询→定单→拍摄→交付各环节流失率，以及老客户复购率和转介绍率")
    public Result<ConversionFunnelDTO> getConversionFunnel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(dashboardService.getConversionFunnel(startDate, endDate));
    }

    /**
     * 获取员工绩效排行
     */
    @GetMapping("/employee-performance")
    @Operation(summary = "获取员工绩效排行", description = "摄影师、化妆师、选片师按接单量、客户评分、成单金额排行；修图师按修图数量、返修率排行")
    public Result<List<EmployeePerformanceDTO>> getEmployeePerformance(
            @RequestParam(required = false) String position,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(dashboardService.getEmployeePerformance(position, startDate, endDate));
    }

    /**
     * 获取财务报表
     */
    @GetMapping("/finance-report")
    @Operation(summary = "获取财务报表", description = "生成每日/每周/每月财务报表，支持按门店维度统计")
    public Result<FinanceReportDTO> getFinanceReport(
            @RequestParam(required = false, defaultValue = "MONTHLY") String period,
            @RequestParam(required = false) Long storeId) {
        return Result.success(dashboardService.getFinanceReport(period, storeId));
    }
}
