package com.photostudio.controller;

import com.photostudio.entity.Order;
import com.photostudio.entity.Order.OrderStatus;
import com.photostudio.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单管理控制器
 * 提供订单管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理", description = "订单信息的管理与状态更新")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "获取所有订单", description = "获取系统中所有订单")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取订单", description = "根据订单ID获取详细信息")
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/no/{orderNo}")
    @Operation(summary = "根据订单编号获取订单", description = "根据订单编号获取订单信息")
    public ResponseEntity<Order> getOrderByNo(
            @Parameter(description = "订单编号") @PathVariable String orderNo) {
        return orderService.getOrderByNo(orderNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取订单", description = "获取指定状态的订单")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @Parameter(description = "订单状态") @PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索订单", description = "根据客户姓名搜索订单")
    public ResponseEntity<List<Order>> searchOrders(
            @Parameter(description = "客户姓名") @RequestParam String customerName) {
        return ResponseEntity.ok(orderService.searchOrdersByCustomerName(customerName));
    }

    @PostMapping
    @Operation(summary = "创建订单", description = "创建新的订单")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新订单", description = "更新订单信息")
    public ResponseEntity<Order> updateOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单", description = "删除订单")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "更新订单状态", description = "更新订单状态")
    public ResponseEntity<Order> updateStatus(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "订单状态") @PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成订单", description = "标记订单为已完成")
    public ResponseEntity<Order> completeOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        return ResponseEntity.ok(orderService.completeOrder(id));
    }

    @PutMapping("/{id}/delivering")
    @Operation(summary = "标记配送中", description = "标记订单为配送中")
    public ResponseEntity<Order> markAsDelivering(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        return ResponseEntity.ok(orderService.markAsDelivering(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消订单", description = "取消订单")
    public ResponseEntity<Order> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}
