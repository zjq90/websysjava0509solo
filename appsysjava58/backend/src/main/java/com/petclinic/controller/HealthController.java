package com.petclinic.controller;

import com.petclinic.dto.Result;
import com.petclinic.entity.DewormingRecord;
import com.petclinic.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 健康管理控制器
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Tag(name = "健康管理", description = "宠物健康管理相关接口")
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/cards/{petId}")
    @Operation(summary = "获取健康提醒卡片", description = "获取宠物的驱虫、体检等提醒卡片数据")
    public Result<Map<String, Object>> getHealthCards(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return healthService.getHealthCards(petId);
    }

    @PostMapping("/deworming")
    @Operation(summary = "添加驱虫记录", description = "添加新的驱虫记录")
    public Result<DewormingRecord> addDewormingRecord(@RequestBody DewormingRecord record) {
        return healthService.addDewormingRecord(record);
    }

    @GetMapping("/deworming/history/{petId}")
    @Operation(summary = "获取驱虫历史记录", description = "获取宠物的所有驱虫历史记录")
    public Result<List<DewormingRecord>> getDewormingHistory(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return healthService.getDewormingHistory(petId);
    }
}