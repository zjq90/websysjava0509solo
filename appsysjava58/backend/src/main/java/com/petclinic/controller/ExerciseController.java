package com.petclinic.controller;

import com.petclinic.dto.Result;
import com.petclinic.entity.ExerciseData;
import com.petclinic.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 运动监测控制器
 * 支持第三方智能项圈数据接入
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/exercise")
@RequiredArgsConstructor
@Tag(name = "运动监测", description = "宠物运动监测相关接口，支持第三方设备接入")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping("/sync")
    @Operation(summary = "同步第三方设备数据", description = "从智能项圈同步运动数据，支持FitBark、Whistle等设备")
    public Result<ExerciseData> syncThirdPartyData(
            @Parameter(description = "设备ID") @RequestParam String deviceId,
            @Parameter(description = "设备类型") @RequestParam String deviceType,
            @Parameter(description = "数据源") @RequestParam String dataSource,
            @Parameter(description = "步数") @RequestParam(required = false) Integer steps,
            @Parameter(description = "距离(公里)") @RequestParam(required = false) BigDecimal distance,
            @Parameter(description = "消耗卡路里") @RequestParam(required = false) BigDecimal caloriesBurned,
            @Parameter(description = "活动分钟数") @RequestParam(required = false) Integer activeMinutes) {
        return exerciseService.syncThirdPartyData(deviceId, deviceType, dataSource,
                steps, distance, caloriesBurned, activeMinutes);
    }

    @GetMapping("/stats/{petId}")
    @Operation(summary = "获取今日运动统计", description = "获取宠物今日运动数据统计")
    public Result<Map<String, Object>> getTodayStats(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return exerciseService.getTodayStats(petId);
    }

    @GetMapping("/history/{petId}")
    @Operation(summary = "获取运动历史", description = "获取宠物的运动历史记录")
    public Result<List<ExerciseData>> getExerciseHistory(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return exerciseService.getExerciseHistory(petId);
    }

    @GetMapping("/devices")
    @Operation(summary = "获取支持的设备列表", description = "获取系统支持的第三方智能项圈设备列表")
    public Result<String[]> getSupportedDevices() {
        return exerciseService.getSupportedDevices();
    }

    @PostMapping("/manual")
    @Operation(summary = "手动添加运动记录", description = "手动录入宠物运动数据")
    public Result<ExerciseData> addManualRecord(@RequestBody ExerciseData data) {
        return exerciseService.addManualRecord(data);
    }
}