package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Order;
import com.photostudio.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理", description = "订单的增删改查及状态管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @Operation(summary = "查询所有订单")
    public Result<List<Order>> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询订单")
    public Result<Page<Order>> findPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return orderService.findPage(page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询订单")
    public Result<Order> findById(@Parameter(description = "订单ID") @PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/no/{orderNo}")
    @Operation(summary = "根据订单编号查询订单")
    public Result<Order> findByOrderNo(@Parameter(description = "订单编号") @PathVariable String orderNo) {
        return orderService.findByOrderNo(orderNo);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "根据客户ID查询订单")
    public Result<List<Order>> findByCustomerId(@Parameter(description = "客户ID") @PathVariable Long customerId) {
        return orderService.findByCustomerId(customerId);
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "根据手机号查询客户订单")
    public Result<List<Order>> findByCustomerPhone(@Parameter(description = "手机号") @PathVariable String phone) {
        return orderService.findByCustomerPhone(phone);
    }

    @GetMapping("/stage/{stage}")
    @Operation(summary = "根据阶段查询订单")
    public Result<List<Order>> findByStage(@Parameter(description = "阶段(0-6)") @PathVariable Integer stage) {
        return orderService.findByStage(stage);
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取订单统计数据")
    public Result<Map<String, Object>> getStatistics() {
        return orderService.getStatistics();
    }

    @PostMapping
    @Operation(summary = "创建订单")
    public Result<Order> createOrder(
            @Parameter(description = "客户ID") @RequestParam Long customerId,
            @Parameter(description = "套餐ID") @RequestParam Long packageId,
            @Parameter(description = "加购项ID列表") @RequestParam(required = false) List<Long> addOnItemIds,
            @Parameter(description = "订单来源") @RequestParam(required = false) String channelSource,
            @Parameter(description = "拍摄地点") @RequestParam(required = false) String shootingLocation,
            @Parameter(description = "拍摄日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime shootingDate,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        return orderService.createOrder(customerId, packageId, addOnItemIds, channelSource, shootingLocation, shootingDate, remark);
    }

    @PutMapping("/{id}/stage/{stage}")
    @Operation(summary = "更新订单阶段")
    public Result<Order> updateStage(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "目标阶段(0-6)") @PathVariable Integer stage,
            @Parameter(description = "操作人") @RequestParam(defaultValue = "管理员") String operator) {
        return orderService.updateStage(id, stage, operator);
    }

    @PutMapping("/{id}/pay-deposit")
    @Operation(summary = "支付定金")
    public Result<Order> payDeposit(@Parameter(description = "订单ID") @PathVariable Long id) {
        return orderService.payDeposit(id);
    }

    @PutMapping("/{id}/sign-contract")
    @Operation(summary = "签署电子合同")
    public Result<Order> signContract(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "签署IP") @RequestParam(defaultValue = "127.0.0.1") String signIp) {
        return orderService.signContract(id, signIp);
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "分配工作人员")
    public Result<Order> assignEmployee(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "摄影师") @RequestParam(required = false) String photographer,
            @Parameter(description = "化妆师") @RequestParam(required = false) String makeupArtist,
            @Parameter(description = "修图师") @RequestParam(required = false) String retoucher) {
        return orderService.assignEmployee(id, photographer, makeupArtist, retoucher);
    }

    @PutMapping
    @Operation(summary = "更新订单")
    public Result<Order> update(@RequestBody Order order) {
        return orderService.update(order);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单")
    public Result<Void> delete(@Parameter(description = "订单ID") @PathVariable Long id) {
        return orderService.delete(id);
    }
}
