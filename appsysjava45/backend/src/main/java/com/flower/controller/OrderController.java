package com.flower.controller;

import com.flower.common.Result;
import com.flower.dto.OrderCreateDTO;
import com.flower.entity.Order;
import com.flower.entity.OrderItem;
import com.flower.service.OrderService;
import com.flower.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<Map<String, Object>> createOrder(@Valid @RequestBody OrderCreateDTO dto,
                                                   HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            Map<String, Object> result = orderService.createOrder(userId, dto);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "获取订单列表")
    public Result<Page<Order>> getOrderList(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
            Page<Order> orders = orderService.getOrderList(userId, status, pageable);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long id,
                                                       HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            Order order = orderService.getOrderDetail(userId, id);
            List<OrderItem> orderItems = orderService.getOrderItems(id);
            Map<String, Object> result = new HashMap<>();
            result.put("order", order);
            result.put("items", orderItems);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/pay/{id}")
    @Operation(summary = "支付订单")
    public Result<Order> payOrder(@PathVariable Long id,
                                  @RequestBody Map<String, String> params,
                                  HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            String payMethod = params.get("payMethod");
            Order order = orderService.payOrder(userId, id, payMethod);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "取消订单")
    public Result<Order> cancelOrder(@PathVariable Long id,
                                      @RequestBody Map<String, String> params,
                                      HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            String reason = params.get("reason");
            Order order = orderService.cancelOrder(userId, id, reason);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/address/{id}")
    @Operation(summary = "修改订单地址")
    public Result<Order> updateOrderAddress(@PathVariable Long id,
                                             @RequestBody Map<String, String> params,
                                             HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            String receiverName = params.get("receiverName");
            String receiverPhone = params.get("receiverPhone");
            String receiverAddress = params.get("receiverAddress");
            Order order = orderService.updateOrderAddress(userId, id, receiverName, receiverPhone, receiverAddress);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/complete/{id}")
    @Operation(summary = "确认收货")
    public Result<Order> completeOrder(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            Order order = orderService.completeOrder(userId, id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}