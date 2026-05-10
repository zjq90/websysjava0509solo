package com.vending.controller;

import com.vending.dto.ApiResponse;
import com.vending.entity.Order;
import com.vending.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单管理控制器
 * 提供订单的查询、创建、支付、取货等接口
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@Tag(name = "订单管理", description = "交易订单管理接口")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    @Operation(summary = "分页查询订单")
    public ApiResponse<Page<Order>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String pickupStatus) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return ApiResponse.success(orderService.findByConditions(paymentStatus, pickupStatus, pageable));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取订单")
    public ApiResponse<Order> getById(@PathVariable Long id) {
        return orderService.findById(id)
            .map(ApiResponse::success)
            .orElse(ApiResponse.error("订单不存在", 404));
    }
    
    @GetMapping("/orderNo/{orderNo}")
    @Operation(summary = "根据订单号查询")
    public ApiResponse<Order> getByOrderNo(@PathVariable String orderNo) {
        return orderService.findByOrderNo(orderNo)
            .map(ApiResponse::success)
            .orElse(ApiResponse.error("订单不存在", 404));
    }
    
    @PostMapping
    @Operation(summary = "创建订单")
    public ApiResponse<Order> create(@RequestBody Map<String, Object> request) {
        try {
            Long machineId = Long.valueOf(request.get("machineId").toString());
            Long productId = Long.valueOf(request.get("productId").toString());
            Long slotId = Long.valueOf(request.get("slotId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            String userIdentifier = (String) request.get("userIdentifier");
            String userName = (String) request.get("userName");
            String userPhone = (String) request.get("userPhone");
            
            return ApiResponse.success("订单创建成功", 
                orderService.createOrder(machineId, productId, slotId, quantity, 
                    userIdentifier, userName, userPhone));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/{orderId}/pay")
    @Operation(summary = "支付订单")
    public ApiResponse<Order> pay(@PathVariable Long orderId, 
                                  @RequestBody Map<String, String> request) {
        try {
            String paymentMethod = request.get("paymentMethod");
            return ApiResponse.success("支付成功", orderService.payOrder(orderId, paymentMethod));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/{orderId}/pickup")
    @Operation(summary = "确认取货")
    public ApiResponse<Order> pickup(@PathVariable Long orderId) {
        try {
            return ApiResponse.success("取货成功", orderService.confirmPickup(orderId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单")
    public ApiResponse<Order> cancel(@PathVariable Long orderId) {
        try {
            return ApiResponse.success("取消成功", orderService.cancelOrder(orderId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/machine/{machineId}")
    @Operation(summary = "根据设备ID查询订单")
    public ApiResponse<Page<Order>> getByMachineId(
            @PathVariable Long machineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return ApiResponse.success(orderService.findByMachineId(machineId, pageable));
    }
}
