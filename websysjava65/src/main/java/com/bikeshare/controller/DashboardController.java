package com.bikeshare.controller;

import com.bikeshare.common.Result;
import com.bikeshare.dto.DashboardDTO;
import com.bikeshare.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据看板控制器
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "数据看板", description = "数据看板相关接口")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "获取看板数据", description = "获取运营数据、车辆状态分布、区域使用率对比")
    public Result<DashboardDTO> getDashboard() {
        return Result.success(dashboardService.getDashboardData());
    }
}
