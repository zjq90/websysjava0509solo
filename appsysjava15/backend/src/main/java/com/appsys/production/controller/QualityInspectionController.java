package com.appsys.production.controller;

import com.appsys.production.common.Result;
import com.appsys.production.dto.QualityInspectionDTO;
import com.appsys.production.entity.QualityInspection;
import com.appsys.production.service.QualityInspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspections")
@Tag(name = "质检管理", description = "质检记录管理接口")
@CrossOrigin(origins = "*")
public class QualityInspectionController {

    @Autowired
    private QualityInspectionService inspectionService;

    @GetMapping("/batch/{batchId}")
    @Operation(summary = "查询批次质检记录", description = "获取指定批次的所有质检记录")
    public Result<List<QualityInspection>> getByBatchId(@Parameter(description = "批次ID") @PathVariable Long batchId) {
        return Result.success(inspectionService.getByBatchId(batchId));
    }

    @GetMapping("/batch/{batchId}/stage/{stageCode}")
    @Operation(summary = "查询指定环节质检记录", description = "获取批次指定环节的质检记录")
    public Result<QualityInspection> getByBatchIdAndStageCode(
            @Parameter(description = "批次ID") @PathVariable Long batchId,
            @Parameter(description = "环节编码") @PathVariable String stageCode) {
        return Result.success(inspectionService.getByBatchIdAndStageCode(batchId, stageCode));
    }

    @PostMapping
    @Operation(summary = "提交质检结果", description = "上传质检数据（水分、净度、发芽率），系统自动判定结果")
    public Result<QualityInspection> submit(@Valid @RequestBody QualityInspectionDTO dto) {
        return Result.success(inspectionService.submit(dto));
    }
}
