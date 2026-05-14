package com.ops.controller;

import com.ops.common.Result;
import com.ops.entity.WorkOrder;
import com.ops.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工单控制器
 * 提供工单相关的API接口
 * 
 * @author ops-admin
 */
@RestController
@RequestMapping("/api/workorders")
@Tag(name = "工单管理", description = "工单的增删改查、派单、接单、完成等操作")
@CrossOrigin(origins = "*")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping
    @Operation(summary = "获取所有工单")
    public Result<List<WorkOrder>> getAllOrders() {
        return Result.success(workOrderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取工单")
    public Result<WorkOrder> getOrderById(@PathVariable Long id) {
        return workOrderService.getOrderById(id)
                .map(Result::success)
                .orElse(Result.error("工单不存在"));
    }

    @PostMapping
    @Operation(summary = "创建工单")
    public Result<WorkOrder> createOrder(@RequestBody WorkOrder workOrder) {
        try {
            return Result.success(workOrderService.createWorkOrder(workOrder));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新工单")
    public Result<WorkOrder> updateOrder(@PathVariable Long id, @RequestBody WorkOrder workOrder) {
        workOrder.setId(id);
        return Result.success(workOrderService.updateOrder(workOrder));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除工单")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        workOrderService.deleteOrder(id);
        return Result.success();
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "智能派单")
    public Result<WorkOrder> autoAssign(@PathVariable Long id) {
        try {
            return Result.success(workOrderService.autoAssign(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "接单")
    public Result<WorkOrder> acceptOrder(@PathVariable Long id) {
        try {
            return Result.success(workOrderService.acceptOrder(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "完成工单")
    public Result<WorkOrder> completeOrder(@PathVariable Long id, @RequestBody Map<String, String> params) {
        try {
            String photoUrls = params.get("photoUrls");
            String signatureUrl = params.get("signatureUrl");
            return Result.success(workOrderService.completeOrder(id, photoUrls, signatureUrl));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/evaluate")
    @Operation(summary = "提交满意度评价")
    public Result<WorkOrder> submitEvaluation(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        try {
            Integer score = (Integer) params.get("score");
            String evaluation = (String) params.get("evaluation");
            return Result.success(workOrderService.submitEvaluation(id, score, evaluation));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
