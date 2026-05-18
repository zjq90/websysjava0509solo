package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.Order;
import com.flowerstore.backend.service.OrderService;
import com.flowerstore.backend.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
@Tag(name = "订单接口", description = "订单创建、查询、取消等")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单", description = "提交购物车商品创建订单")
    public Result<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request, 
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");
        Long addressId = request.get("addressId") != null ? Long.valueOf(request.get("addressId").toString()) : null;
        Long couponId = request.get("couponId") != null ? Long.valueOf(request.get("couponId").toString()) : null;
        String remark = request.get("remark") != null ? request.get("remark").toString() : null;

        return orderService.createOrder(userId, items, addressId, couponId, remark);
    }

    /**
     * 获取用户订单列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取订单列表", description = "获取当前用户的订单列表")
    public Result<List<Order>> getOrderList(
            @RequestParam(required = false) Integer status,
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return orderService.getUserOrders(userId, status);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "获取订单详情", description = "获取订单的详细信息")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long orderId, 
            HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return orderService.getOrderDetail(userId, orderId);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderId}")
    @Operation(summary = "取消订单", description = "取消未支付的订单")
    public Result<String> cancelOrder(@PathVariable Long orderId, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return orderService.cancelOrder(userId, orderId);
    }

    /**
     * 确认收货
     */
    @PostMapping("/confirm/{orderId}")
    @Operation(summary = "确认收货", description = "确认已收到商品")
    public Result<String> confirmReceipt(@PathVariable Long orderId, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return orderService.confirmReceipt(userId, orderId);
    }

    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
