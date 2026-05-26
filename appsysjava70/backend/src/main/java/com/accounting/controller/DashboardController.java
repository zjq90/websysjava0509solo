package com.accounting.controller;

import com.accounting.dto.ApiResponse;
import com.accounting.dto.DashboardDTO;
import com.accounting.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "看板管理", description = "实时看板数据接口")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "获取看板数据", description = "获取首页看板的全部数据，包括总资产、今日收支、本月收支、预算警告、异常消费等")
    public ApiResponse<DashboardDTO> getDashboardData() {
        log.info("API: 获取看板数据");
        return ApiResponse.success(dashboardService.getDashboardData());
    }
}
