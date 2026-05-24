package com.vehicle.controller;

import com.vehicle.common.Result;
import com.vehicle.dto.EnforcementRecordDTO;
import com.vehicle.entity.EnforcementRecord;
import com.vehicle.service.EnforcementRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enforcement")
@Tag(name = "执法记录", description = "现场执法记录管理接口")
public class EnforcementRecordController {

    private final EnforcementRecordService recordService;

    public EnforcementRecordController(EnforcementRecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    @Operation(summary = "创建执法记录", description = "提交现场执法记录，包括照片、签名等信息")
    public Result<EnforcementRecord> createRecord(
            @Validated @RequestBody EnforcementRecordDTO dto) {
        EnforcementRecord record = recordService.createRecord(dto);
        return Result.success("执法记录创建成功", record);
    }

    @GetMapping("/plate/{plateNumber}")
    @Operation(summary = "查询车辆执法记录", description = "根据车牌号查询历史执法记录")
    public Result<List<EnforcementRecord>> getRecordsByPlate(
            @Parameter(description = "车牌号", required = true)
            @PathVariable String plateNumber) {
        List<EnforcementRecord> records = recordService.getRecordsByPlateNumber(plateNumber);
        return Result.success(records);
    }

    @GetMapping("/officer/{officerName}")
    @Operation(summary = "查询执法人记录", description = "根据执法人姓名查询执法记录")
    public Result<List<EnforcementRecord>> getRecordsByOfficer(
            @Parameter(description = "执法人姓名", required = true)
            @PathVariable String officerName) {
        List<EnforcementRecord> records = recordService.getRecordsByOfficer(officerName);
        return Result.success(records);
    }

    @GetMapping("/unsynced")
    @Operation(summary = "获取未同步记录", description = "获取所有未同步到服务器的执法记录")
    public Result<List<EnforcementRecord>> getUnsyncedRecords() {
        List<EnforcementRecord> records = recordService.getUnsyncedRecords();
        return Result.success(records);
    }

    @PutMapping("/sync/{id}")
    @Operation(summary = "标记记录已同步", description = "网络恢复后标记离线记录为已同步")
    public Result<EnforcementRecord> syncRecord(
            @Parameter(description = "记录ID", required = true)
            @PathVariable Long id) {
        EnforcementRecord record = recordService.syncRecord(id);
        if (record != null) {
            return Result.success("记录同步成功", record);
        }
        return Result.error("记录不存在");
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询单个执法记录", description = "根据ID查询执法记录详情")
    public Result<EnforcementRecord> getRecord(
            @Parameter(description = "记录ID", required = true)
            @PathVariable Long id) {
        Optional<EnforcementRecord> record = recordService.getRecordById(id);
        return record.map(Result::success)
                .orElse(Result.error("记录不存在"));
    }

    @GetMapping
    @Operation(summary = "获取所有执法记录", description = "获取全部执法记录列表")
    public Result<List<EnforcementRecord>> getAllRecords() {
        List<EnforcementRecord> records = recordService.getAllRecords();
        return Result.success(records);
    }
}
