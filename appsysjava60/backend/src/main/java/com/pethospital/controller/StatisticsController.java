package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.dto.StatisticsDTO;
import com.pethospital.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统计控制器
 * 提供接诊量统计、疾病排行、药品使用分析等接口
 * 
 * @author Pet Hospital Team
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
@Tag(name = "统计管理", description = "接诊量统计、疾病排行、药品使用分析相关接口")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    /**
     * 获取统计数据
     * 
     * @param days 统计天数，默认30天
     * @return 统计数据
     */
    @GetMapping
    @Operation(summary = "获取统计数据", description = "获取接诊量统计、疾病排行、药品使用分析等统计数据")
    public Result<StatisticsDTO> getStatistics(
            @Parameter(description = "统计天数，默认30天") @RequestParam(required = false, defaultValue = "30") Integer days) {
        log.info("获取统计数据，天数：{}", days);
        StatisticsDTO statistics = statisticsService.getStatistics(days);
        return Result.success(statistics);
    }
    
    /**
     * 导出统计数据到Excel
     * 
     * @param days 统计天数，默认30天
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    @Operation(summary = "导出统计数据", description = "导出统计数据到Excel文件")
    public void exportStatistics(
            @Parameter(description = "统计天数，默认30天") @RequestParam(required = false, defaultValue = "30") Integer days,
            HttpServletResponse response) throws IOException {
        log.info("导出统计数据，天数：{}", days);
        statisticsService.exportStatistics(response, days);
    }
}
