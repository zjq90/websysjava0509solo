package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.Order;
import com.secondhand.entity.User;
import com.secondhand.service.OrderService;
import com.secondhand.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 *
 * @author secondhand
 * @version 1.0.0
 */
@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "订单创建、支付、状态管理等接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @Operation(summary = "创建订单", description = "创建新的订单")
    public Result<Order> createOrder(@RequestHeader("Authorization") String token, @RequestParam Long productId, @RequestParam(required = false) String pickupType, @RequestParam(required = false) Long pickupPointId, @RequestParam(required = false) String address, @RequestParam(required = false) String receiver, @RequestParam(required = false) String phone) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            Order order = orderService.createOrder(user.getId(), productId, pickupType, pickupPointId, address, receiver, phone);
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/pay/{orderNo}")
    @Operation(summary = "支付订单", description = "支付待支付的订单")
    public Result<Order> payOrder(@RequestHeader("Authorization") String token, @PathVariable String orderNo, @RequestParam(defaultValue = "WECHAT") String paymentMethod) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            Order order = orderService.payOrder(orderNo, paymentMethod);
            return Result.success("支付成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/cancel/{orderNo}")
    @Operation(summary = "取消订单", description = "取消待支付的订单")
    public Result<Order> cancelOrder(@RequestHeader("Authorization") String token, @PathVariable String orderNo) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            Order order = orderService.cancelOrder(orderNo);
            return Result.success("取消成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/confirm/{orderNo}")
    @Operation(summary = "确认收货", description = "确认收到商品")
    public Result<Order> confirmReceive(@RequestHeader("Authorization") String token, @PathVariable String orderNo) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            Order order = orderService.confirmReceive(orderNo);
            return Result.success("确认成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{orderNo}")
    @Operation(summary = "获取订单详情", description = "根据订单号获取订单详情")
    public Result<Order> getOrderByOrderNo(@RequestHeader("Authorization") String token, @PathVariable String orderNo) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        Order order = orderService.getOrderByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    @GetMapping("/my")
    @Operation(summary = "我的订单", description = "获取当前用户的订单列表")
    public Result<List<Order>> getUserOrders(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        List<Order> orders = orderService.getUserOrders(user.getId());
        return Result.success(orders);
    }

    @PostMapping("/refund/{orderNo}")
    @Operation(summary = "申请退款", description = "申请订单退款")
    public Result<Order> applyRefund(@RequestHeader("Authorization") String token, @PathVariable String orderNo) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            Order order = orderService.applyRefund(orderNo);
            return Result.success("退款申请已提交", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
