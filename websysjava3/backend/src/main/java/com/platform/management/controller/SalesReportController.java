package com.platform.management.controller;

import com.platform.management.common.Result;
import com.platform.management.dto.SalesReportDTO;
import com.platform.management.service.SalesReportService;
import com.platform.management.vo.DashboardVO;
import com.platform.management.vo.SalesReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 销售报表Controller
 * 
 * @author platform
 * @version 1.0.0
 */
@RestController
@RequestMapping("/sales-report")
@Tag(name = "销售报表", description = "销售报表相关接口")
public class SalesReportController {

    @Autowired
    private SalesReportService salesReportService;

    @GetMapping
    @Operation(summary = "获取销售报表数据")
    public Result<List<SalesReportVO>> getSalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "DAY") String dimension,
            @RequestParam(required = false) String payChannel) {
        SalesReportDTO dto = new SalesReportDTO();
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setDimension(dimension);
        dto.setPayChannel(payChannel);
        
        List<SalesReportVO> result = salesReportService.getSalesReport(dto);
        return Result.success(result);
    }

    @GetMapping("/dashboard")
    @Operation(summary = "获取仪表盘统计数据")
    public Result<DashboardVO> getDashboardStats() {
        DashboardVO result = salesReportService.getDashboardStats();
        return Result.success(result);
    }
}
