package com.flower.controller;

import com.flower.common.Result;
import com.flower.service.AnalyticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据分析控制器
 */
@RestController
@RequestMapping("/api/analytics")
@Api(tags = "数据分析接口")
@CrossOrigin
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/sales-report")
    @ApiOperation("获取销售报表数据")
    public Result<Map<String, Object>> getSalesReport(@RequestParam(defaultValue = "month") String type) {
        return Result.success(analyticsService.getSalesReport(type));
    }

    @GetMapping("/hot-products")
    @ApiOperation("获取热销商品")
    public Result<List<Map<String, Object>>> getHotProducts(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(analyticsService.getHotProducts(limit));
    }

    @GetMapping("/sales-trend")
    @ApiOperation("获取销售趋势数据")
    public Result<Map<String, Object>> getSalesTrend(@RequestParam(defaultValue = "day") String type) {
        return Result.success(analyticsService.getSalesTrend(type));
    }
}
