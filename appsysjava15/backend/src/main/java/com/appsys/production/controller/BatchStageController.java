package com.appsys.production.controller;

import com.appsys.production.common.Result;
import com.appsys.production.dto.StageOperationDTO;
import com.appsys.production.entity.BatchStage;
import com.appsys.production.service.BatchStageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stages")
@Tag(name = "环节管理", description = "生产环节操作接口")
@CrossOrigin(origins = "*")
public class BatchStageController {

    @Autowired
    private BatchStageService stageService;

    @GetMapping("/batch/{batchId}")
    @Operation(summary = "查询批次所有环节", description = "获取指定批次的所有生产环节进度")
    public Result<List<BatchStage>> getByBatchId(@Parameter(description = "批次ID") @PathVariable Long batchId) {
        return Result.success(stageService.getByBatchId(batchId));
    }

    @GetMapping("/batch/{batchId}/stage/{stageCode}")
    @Operation(summary = "查询指定环节详情", description = "获取批次指定环节的详细信息")
    public Result<BatchStage> getByBatchIdAndStageCode(
            @Parameter(description = "批次ID") @PathVariable Long batchId,
            @Parameter(description = "环节编码") @PathVariable String stageCode) {
        return Result.success(stageService.getByBatchIdAndStageCode(batchId, stageCode));
    }

    @PostMapping("/start")
    @Operation(summary = "开始环节", description = "开始生产环节，记录开始时间和操作人")
    public Result<BatchStage> startStage(@RequestBody StageOperationDTO dto) {
        return Result.success(stageService.startStage(dto));
    }

    @PostMapping("/complete")
    @Operation(summary = "完成环节", description = "完成生产环节，记录结束时间")
    public Result<BatchStage> completeStage(@RequestBody StageOperationDTO dto) {
        return Result.success(stageService.completeStage(dto));
    }

    @PostMapping("/check-timeout")
    @Operation(summary = "检查超时并预警", description = "检查进行中的环节是否超时，超时则自动发送预警通知")
    public Result<Void> checkTimeoutAndAlert() {
        stageService.checkTimeoutAndAlert();
        return Result.success();
    }
}
