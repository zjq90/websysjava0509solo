package com.photostudio.controller;

import com.photostudio.entity.Express;
import com.photostudio.entity.Express.ExpressStatus;
import com.photostudio.service.ExpressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 快递管理控制器
 * 提供快递管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/express")
@Tag(name = "快递管理", description = "快递单创建与物流信息更新")
public class ExpressController {

    private final ExpressService expressService;

    @Autowired
    public ExpressController(ExpressService expressService) {
        this.expressService = expressService;
    }

    @GetMapping
    @Operation(summary = "获取所有快递信息", description = "获取系统中所有快递信息")
    public ResponseEntity<List<Express>> getAllExpress() {
        return ResponseEntity.ok(expressService.getAllExpress());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取快递信息", description = "根据快递ID获取详细信息")
    public ResponseEntity<Express> getExpressById(
            @Parameter(description = "快递ID") @PathVariable Long id) {
        return expressService.getExpressById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "根据订单获取快递信息", description = "获取指定订单的快递信息")
    public ResponseEntity<Express> getExpressByOrder(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        return expressService.getExpressByOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tracking/{trackingNo}")
    @Operation(summary = "根据快递单号查询", description = "根据快递单号查询快递信息")
    public ResponseEntity<Express> getExpressByTrackingNo(
            @Parameter(description = "快递单号") @PathVariable String trackingNo) {
        return expressService.getExpressByTrackingNo(trackingNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取快递", description = "获取指定状态的快递")
    public ResponseEntity<List<Express>> getExpressByStatus(
            @Parameter(description = "物流状态") @PathVariable ExpressStatus status) {
        return ResponseEntity.ok(expressService.getExpressByStatus(status));
    }

    @GetMapping("/un-signed")
    @Operation(summary = "获取未签收的快递", description = "获取所有未签收的快递")
    public ResponseEntity<List<Express>> getUnSignedExpress() {
        return ResponseEntity.ok(expressService.getUnSignedExpress());
    }

    @PostMapping
    @Operation(summary = "创建快递信息", description = "创建新的快递信息")
    public ResponseEntity<Express> createExpress(@RequestBody Express express) {
        return ResponseEntity.ok(expressService.createExpress(express));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新快递信息", description = "更新快递信息")
    public ResponseEntity<Express> updateExpress(
            @Parameter(description = "快递ID") @PathVariable Long id,
            @RequestBody Express express) {
        return ResponseEntity.ok(expressService.updateExpress(id, express));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除快递信息", description = "删除快递信息")
    public ResponseEntity<Void> deleteExpress(
            @Parameter(description = "快递ID") @PathVariable Long id) {
        expressService.deleteExpress(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "更新物流状态", description = "更新快递的物流状态")
    public ResponseEntity<Express> updateStatus(
            @Parameter(description = "快递ID") @PathVariable Long id,
            @Parameter(description = "物流状态") @PathVariable ExpressStatus status,
            @RequestParam(required = false) String trackingInfo) {
        return ResponseEntity.ok(expressService.updateStatus(id, status, trackingInfo));
    }

    @PutMapping("/{id}/simulate-tracking")
    @Operation(summary = "模拟更新物流", description = "模拟更新物流状态（用于测试）")
    public ResponseEntity<Express> simulateTrackingUpdate(
            @Parameter(description = "快递ID") @PathVariable Long id) {
        return ResponseEntity.ok(expressService.simulateTrackingUpdate(id));
    }

    @PutMapping("/{id}/sign")
    @Operation(summary = "标记已签收", description = "标记快递已签收")
    public ResponseEntity<Express> markAsSigned(
            @Parameter(description = "快递ID") @PathVariable Long id) {
        return ResponseEntity.ok(expressService.markAsSigned(id));
    }
}
