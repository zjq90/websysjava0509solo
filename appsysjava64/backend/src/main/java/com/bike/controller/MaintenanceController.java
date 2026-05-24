package com.bike.controller;

import com.bike.common.Result;
import com.bike.entity.FaultRecord;
import com.bike.entity.RepairOrder;
import com.bike.entity.SparePart;
import com.bike.entity.SparePartLog;
import com.bike.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆维护控制器
 * 
 * @author bike-sharing
 */
@Tag(name = "车辆维护", description = "故障记录、维修工单、备件库存管理等接口")
@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @Operation(summary = "获取所有故障记录")
    @GetMapping("/faults")
    public Result<List<FaultRecord>> getAllFaultRecords() {
        return Result.success(maintenanceService.getAllFaultRecords());
    }

    @Operation(summary = "获取车辆的故障记录")
    @GetMapping("/faults/bike/{bikeId}")
    public Result<List<FaultRecord>> getFaultRecordsByBikeId(
            @Parameter(description = "车辆ID") @PathVariable Long bikeId) {
        return Result.success(maintenanceService.getFaultRecordsByBikeId(bikeId));
    }

    @Operation(summary = "创建故障记录")
    @PostMapping("/faults")
    public Result<FaultRecord> createFaultRecord(@RequestBody FaultRecord record) {
        return Result.success(maintenanceService.createFaultRecord(record));
    }

    @Operation(summary = "更新故障记录")
    @PutMapping("/faults/{id}")
    public Result<FaultRecord> updateFaultRecord(
            @Parameter(description = "记录ID") @PathVariable Long id,
            @RequestBody FaultRecord record) {
        return Result.success(maintenanceService.updateFaultRecord(id, record));
    }

    @Operation(summary = "获取所有维修工单")
    @GetMapping("/orders")
    public Result<List<RepairOrder>> getAllRepairOrders() {
        return Result.success(maintenanceService.getAllRepairOrders());
    }

    @Operation(summary = "按优先级排序获取维修工单")
    @GetMapping("/orders/sorted")
    public Result<List<RepairOrder>> getSortedRepairOrdersByPriority() {
        return Result.success(maintenanceService.getSortedRepairOrdersByPriority());
    }

    @Operation(summary = "获取运维人员的维修工单")
    @GetMapping("/orders/staff/{staffId}")
    public Result<List<RepairOrder>> getRepairOrdersByStaffId(
            @Parameter(description = "运维人员ID") @PathVariable Long staffId) {
        return Result.success(maintenanceService.getRepairOrdersByStaffId(staffId));
    }

    @Operation(summary = "获取车辆的维修工单")
    @GetMapping("/orders/bike/{bikeId}")
    public Result<List<RepairOrder>> getRepairOrdersByBikeId(
            @Parameter(description = "车辆ID") @PathVariable Long bikeId) {
        return Result.success(maintenanceService.getRepairOrdersByBikeId(bikeId));
    }

    @Operation(summary = "获取维修工单详情")
    @GetMapping("/orders/{id}")
    public Result<RepairOrder> getRepairOrderById(@Parameter(description = "工单ID") @PathVariable Long id) {
        return Result.success(maintenanceService.getRepairOrderById(id));
    }

    @Operation(summary = "创建维修工单")
    @PostMapping("/orders")
    public Result<RepairOrder> createRepairOrder(@RequestBody RepairOrder order) {
        return Result.success(maintenanceService.createRepairOrder(order));
    }

    @Operation(summary = "从故障记录创建维修工单")
    @PostMapping("/orders/from-fault/{faultRecordId}")
    public Result<RepairOrder> createRepairOrderFromFault(
            @Parameter(description = "故障记录ID") @PathVariable Long faultRecordId) {
        return Result.success(maintenanceService.createRepairOrderFromFault(faultRecordId));
    }

    @Operation(summary = "接受维修工单")
    @PostMapping("/orders/{id}/accept")
    public Result<RepairOrder> acceptRepairOrder(
            @Parameter(description = "工单ID") @PathVariable Long id,
            @Parameter(description = "运维人员ID") @RequestParam Long staffId,
            @Parameter(description = "运维人员姓名") @RequestParam String staffName) {
        return Result.success(maintenanceService.acceptRepairOrder(id, staffId, staffName));
    }

    @Operation(summary = "开始维修")
    @PostMapping("/orders/{id}/start")
    public Result<RepairOrder> startRepair(@Parameter(description = "工单ID") @PathVariable Long id) {
        return Result.success(maintenanceService.startRepair(id));
    }

    @Operation(summary = "完成维修")
    @PostMapping("/orders/{id}/complete")
    public Result<RepairOrder> completeRepair(
            @Parameter(description = "工单ID") @PathVariable Long id,
            @Parameter(description = "维修描述") @RequestParam String description,
            @Parameter(description = "使用的备件") @RequestParam(required = false) String usedParts,
            @Parameter(description = "维修费用") @RequestParam(required = false) Float cost) {
        return Result.success(maintenanceService.completeRepair(id, description, usedParts, cost));
    }

    @Operation(summary = "删除维修工单")
    @DeleteMapping("/orders/{id}")
    public Result<Void> deleteRepairOrder(@Parameter(description = "工单ID") @PathVariable Long id) {
        maintenanceService.deleteRepairOrder(id);
        return Result.success();
    }

    @Operation(summary = "获取所有备件")
    @GetMapping("/parts")
    public Result<List<SparePart>> getAllSpareParts() {
        return Result.success(maintenanceService.getAllSpareParts());
    }

    @Operation(summary = "按分类获取备件")
    @GetMapping("/parts/category/{category}")
    public Result<List<SparePart>> getSparePartsByCategory(
            @Parameter(description = "分类") @PathVariable String category) {
        return Result.success(maintenanceService.getSparePartsByCategory(category));
    }

    @Operation(summary = "获取库存不足的备件")
    @GetMapping("/parts/low-stock")
    public Result<List<SparePart>> getLowStockParts() {
        return Result.success(maintenanceService.getLowStockParts());
    }

    @Operation(summary = "获取备件详情")
    @GetMapping("/parts/{id}")
    public Result<SparePart> getSparePartById(@Parameter(description = "备件ID") @PathVariable Long id) {
        return Result.success(maintenanceService.getSparePartById(id));
    }

    @Operation(summary = "创建备件")
    @PostMapping("/parts")
    public Result<SparePart> createSparePart(@RequestBody SparePart part) {
        return Result.success(maintenanceService.createSparePart(part));
    }

    @Operation(summary = "更新备件")
    @PutMapping("/parts/{id}")
    public Result<SparePart> updateSparePart(
            @Parameter(description = "备件ID") @PathVariable Long id,
            @RequestBody SparePart part) {
        return Result.success(maintenanceService.updateSparePart(id, part));
    }

    @Operation(summary = "更新备件库存")
    @PostMapping("/parts/{id}/stock")
    public Result<SparePart> updateStock(
            @Parameter(description = "备件ID") @PathVariable Long id,
            @Parameter(description = "数量") @RequestParam Integer quantity,
            @Parameter(description = "操作类型(IN/OUT)") @RequestParam String operationType,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "操作人姓名") @RequestParam(required = false) String operatorName,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        return Result.success(maintenanceService.updateStock(id, quantity, operationType, operatorId, operatorName, remark));
    }

    @Operation(summary = "获取备件出入库记录")
    @GetMapping("/parts/logs")
    public Result<List<SparePartLog>> getSparePartLogs(
            @Parameter(description = "备件ID") @RequestParam(required = false) Long partId) {
        return Result.success(maintenanceService.getSparePartLogs(partId));
    }
}
