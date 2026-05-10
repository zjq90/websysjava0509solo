package com.appsys.controller;

import com.appsys.dto.OrderCreateRequest;
import com.appsys.entity.LogisticsTracking;
import com.appsys.entity.OrderStatus;
import com.appsys.entity.SalesOrder;
import com.appsys.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单管理控制器
 * 提供订单的增删改查API接口，支持完整的订单闭环流程
 * 
 * 订单流程：创�?>确认->签署->ERP同步->备货->发货->签收->完成
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/orders")
@Tag(name = "订单管理", description = "订单CRUD操作、订单状态流转、合同签署等")
public class SalesOrderController {

    @Autowired
    private SalesOrderService orderService;

    /**
     * 获取所有订单列�?     */
    @GetMapping
    @Operation(summary = "获取所有订�?, description = "返回所有订单列表，按创建时间倒序排列")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        List<SalesOrder> orders = orderService.getAllOrders();
        return successResponse(orders);
    }

    /**
     * 根据ID获取订单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单详细信息")
    public ResponseEntity<Map<String, Object>> getOrderById(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        SalesOrder order = orderService.getOrderById(id);
        return successResponse(order);
    }

    /**
     * 根据订单编号获取订单
     */
    @GetMapping("/no/{orderNo}")
    @Operation(summary = "根据编号获取订单", description = "根据订单编号获取订单详情")
    public ResponseEntity<Map<String, Object>> getOrderByOrderNo(
            @Parameter(description = "订单编号") @PathVariable String orderNo) {
        SalesOrder order = orderService.getOrderByOrderNo(orderNo);
        return successResponse(order);
    }

    /**
     * 创建新订�?     * 业务流程：业务员现场选品 -> 系统根据客户等级自动匹配价格策略
     */
    @PostMapping
    @Operation(summary = "创建订单", description = "创建新订单，系统会根据客户等级自动应用折�?)
    public ResponseEntity<Map<String, Object>> createOrder(
            @Valid @RequestBody OrderCreateRequest request) {
        SalesOrder order = orderService.createOrder(request);
        return successResponse(order);
    }

    /**
     * 确认订单（状态：待确�?-> 待签署）
     */
    @PostMapping("/{id}/confirm")
    @Operation(summary = "确认订单", description = "确认订单，状态变更为待签�?)
    public ResponseEntity<Map<String, Object>> confirmOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        SalesOrder order = orderService.confirmOrder(id);
        return successResponse(order);
    }

    /**
     * 签署合同（状态：待签�?-> 已签署）
     * 业务流程：客户扫码签署电子合�?     */
    @PostMapping("/{id}/sign")
    @Operation(summary = "签署合同", description = "客户签署电子合同，状态变更为已签署，生成合同编号")
    public ResponseEntity<Map<String, Object>> signContract(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        SalesOrder order = orderService.signContract(id);
        return successResponse(order);
    }

    /**
     * 同步到ERP（状态：已签�?-> 备货中）
     * 业务流程：同步至ERP系统 -> 触发仓库备货
     */
    @PostMapping("/{id}/sync-erp")
    @Operation(summary = "同步ERP", description = "同步订单到ERP系统，状态变更为备货中，扣减库存")
    public ResponseEntity<Map<String, Object>> syncToErp(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        SalesOrder order = orderService.syncToErp(id);
        return successResponse(order);
    }

    /**
     * 发货（状态：备货�?-> 已发货）
     */
    @PostMapping("/{id}/ship")
    @Operation(summary = "发货", description = "订单发货，状态变更为已发货，创建物流记录")
    public ResponseEntity<Map<String, Object>> shipOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @RequestBody LogisticsTracking logisticsInfo) {
        SalesOrder order = orderService.shipOrder(id, logisticsInfo);
        return successResponse(order);
    }

    /**
     * 签收（状态：已发�?-> 已签收）
     */
    @PostMapping("/{id}/deliver")
    @Operation(summary = "签收", description = "客户签收，状态变更为已签收，更新客户消费金额和等�?)
    public ResponseEntity<Map<String, Object>> deliverOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        SalesOrder order = orderService.deliverOrder(id);
        return successResponse(order);
    }

    /**
     * 完成订单（状态：已签�?-> 已完成）
     */
    @PostMapping("/{id}/complete")
    @Operation(summary = "完成订单", description = "订单完成，状态变更为已完�?)
    public ResponseEntity<Map<String, Object>> completeOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        SalesOrder order = orderService.completeOrder(id);
        return successResponse(order);
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消订单", description = "取消订单，状态变更为已取消，如果已扣减库存则回滚")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        SalesOrder order = orderService.cancelOrder(id);
        return successResponse(order);
    }

    /**
     * 根据状态获取订单列�?     */
    @GetMapping("/status/{status}")
    @Operation(summary = "按状态查询订�?, description = "根据订单状态获取订单列�?)
    public ResponseEntity<Map<String, Object>> getOrdersByStatus(
            @Parameter(description = "订单状�?) @PathVariable OrderStatus status) {
        List<SalesOrder> orders = orderService.getOrdersByStatus(status);
        return successResponse(orders);
    }

    /**
     * 获取客户历史订单
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "获取客户订单", description = "获取指定客户的所有订�?)
    public ResponseEntity<Map<String, Object>> getCustomerOrders(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<SalesOrder> orders = orderService.getCustomerOrders(customerId);
        return successResponse(orders);
    }

    /**
     * 根据业务员ID获取订单
     */
    @GetMapping("/salesperson/{salespersonId}")
    @Operation(summary = "获取业务员订�?, description = "获取指定业务员的所有订�?)
    public ResponseEntity<Map<String, Object>> getOrdersBySalesperson(
            @Parameter(description = "业务员ID") @PathVariable Long salespersonId) {
        List<SalesOrder> orders = orderService.getOrdersBySalesperson(salespersonId);
        return successResponse(orders);
    }

    /**
     * 获取各状态订单数量统�?     */
    @GetMapping("/statistics")
    @Operation(summary = "订单统计", description = "统计各状态订单的数量")
    public ResponseEntity<Map<String, Object>> getOrderStatusCounts() {
        Map<String, Long> counts = orderService.getOrderStatusCounts();
        return successResponse(counts);
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
