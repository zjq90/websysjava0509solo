package com.appsys.controller;

import com.appsys.entity.LogisticsStatus;
import com.appsys.entity.LogisticsTracking;
import com.appsys.service.LogisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 物流管理控制器
 * 提供物流跟踪的API接口
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/logistics")
@Tag(name = "物流管理", description = "物流跟踪、物流状态更新等")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    /**
     * 根据ID获取物流信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取物流信息", description = "根据物流记录ID获取物流详情")
    public ResponseEntity<Map<String, Object>> getLogisticsById(
            @Parameter(description = "物流记录ID") @PathVariable Long id) {
        LogisticsTracking logistics = logisticsService.getLogisticsById(id);
        return successResponse(logistics);
    }

    /**
     * 根据订单ID获取物流信息
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "根据订单ID获取物流", description = "根据订单ID获取对应的物流信息")
    public ResponseEntity<Map<String, Object>> getLogisticsByOrderId(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        LogisticsTracking logistics = logisticsService.getLogisticsByOrderId(orderId);
        return successResponse(logistics);
    }

    /**
     * 根据快递单号获取物流信息
     */
    @GetMapping("/tracking/{trackingNo}")
    @Operation(summary = "根据快递单号查询", description = "根据快递单号获取物流信息")
    public ResponseEntity<Map<String, Object>> getLogisticsByTrackingNo(
            @Parameter(description = "快递单号") @PathVariable String trackingNo) {
        LogisticsTracking logistics = logisticsService.getLogisticsByTrackingNo(trackingNo);
        return successResponse(logistics);
    }

    /**
     * 创建物流记录
     */
    @PostMapping
    @Operation(summary = "创建物流记录", description = "创建新的物流跟踪记录")
    public ResponseEntity<Map<String, Object>> createLogistics(
            @RequestBody LogisticsTracking logistics) {
        LogisticsTracking created = logisticsService.createLogistics(logistics);
        return successResponse(created);
    }

    /**
     * 更新物流信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新物流信息", description = "根据ID更新物流信息")
    public ResponseEntity<Map<String, Object>> updateLogistics(
            @Parameter(description = "物流记录ID") @PathVariable Long id,
            @RequestBody LogisticsTracking logisticsDetails) {
        LogisticsTracking updated = logisticsService.updateLogistics(id, logisticsDetails);
        return successResponse(updated);
    }

    /**
     * 更新物流状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新物流状态", description = "更新物流状态和当前位置")
    public ResponseEntity<Map<String, Object>> updateLogisticsStatus(
            @Parameter(description = "物流记录ID") @PathVariable Long id,
            @Parameter(description = "物流状态") @RequestParam LogisticsStatus status,
            @Parameter(description = "当前位置") @RequestParam(required = false) String location) {
        LogisticsTracking updated = logisticsService.updateLogisticsStatus(id, status, location);
        return successResponse(updated);
    }

    /**
     * 模拟从快递API获取物流信息
     */
    @PostMapping("/{trackingNo}/fetch")
    @Operation(summary = "获取快递物流", description = "模拟从快递API获取实时物流信息")
    public ResponseEntity<Map<String, Object>> simulateFetchFromApi(
            @Parameter(description = "快递单号") @PathVariable String trackingNo) {
        LogisticsTracking logistics = logisticsService.simulateFetchFromApi(trackingNo);
        return successResponse(logistics);
    }

    /**
     * 删除物流记录
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除物流记录", description = "根据ID删除物流记录")
    public ResponseEntity<Map<String, Object>> deleteLogistics(
            @Parameter(description = "物流记录ID") @PathVariable Long id) {
        logisticsService.deleteLogistics(id);
        return successResponse("物流记录删除成功");
    }

    /**
     * 构建成功响应
     */
    private ResponseEntity<Map<String, Object>> successResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}
