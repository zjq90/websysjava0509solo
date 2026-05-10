package com.management.platform.controller;

import com.management.platform.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 大屏控制器
 * 提供数据大屏所需的综合统计数据API
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "数据大屏", description = "数据大屏综合统计接口")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取大屏综合数据
     * @return 大屏综合统计数据
     */
    @GetMapping
    @Operation(summary = "大屏综合数据", description = "获取数据大屏所需的综合统计数据")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
}
