package com.plate.controller;

import com.plate.common.Result;
import com.plate.dto.DashboardStats;
import com.plate.entity.Camera;
import com.plate.service.CameraService;
import com.plate.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "仪表盘管理", description = "实时监控大屏相关接口")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private CameraService cameraService;

    @GetMapping("/stats")
    @Operation(summary = "获取统计数据")
    public Result<DashboardStats> getStats() {
        return Result.success(dashboardService.getStats());
    }

    @GetMapping("/cameras")
    @Operation(summary = "获取所有摄像头")
    public Result<List<Camera>> getAllCameras() {
        return Result.success(cameraService.getAllCameras());
    }

    @PostMapping("/night-mode")
    @Operation(summary = "一键切换所有摄像头夜间模式")
    public Result<Void> setAllNightMode(@RequestParam boolean enabled) {
        cameraService.setAllNightMode(enabled);
        return Result.success();
    }
}
