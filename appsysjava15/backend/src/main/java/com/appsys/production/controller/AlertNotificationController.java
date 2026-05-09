package com.appsys.production.controller;

import com.appsys.production.common.Result;
import com.appsys.production.entity.AlertNotification;
import com.appsys.production.service.AlertNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "预警管理", description = "异常预警通知接口")
@CrossOrigin(origins = "*")
public class AlertNotificationController {

    @Autowired
    private AlertNotificationService alertService;

    @GetMapping("/supervisor/{supervisorId}")
    @Operation(summary = "查询主管预警通知", description = "获取指定主管的所有预警通知")
    public Result<List<AlertNotification>> getBySupervisorId(@Parameter(description = "主管ID") @PathVariable Long supervisorId) {
        return Result.success(alertService.getBySupervisorId(supervisorId));
    }

    @GetMapping("/batch/{batchId}")
    @Operation(summary = "查询批次预警通知", description = "获取指定批次的所有预警通知")
    public Result<List<AlertNotification>> getByBatchId(@Parameter(description = "批次ID") @PathVariable Long batchId) {
        return Result.success(alertService.getByBatchId(batchId));
    }

    @PostMapping("/read/{id}")
    @Operation(summary = "标记已读", description = "将预警通知标记为已读")
    public Result<AlertNotification> markAsRead(@Parameter(description = "通知ID") @PathVariable Long id) {
        return Result.success(alertService.markAsRead(id));
    }
}
