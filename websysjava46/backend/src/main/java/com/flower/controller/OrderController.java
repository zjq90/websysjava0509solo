package com.flower.controller;

import com.flower.common.Result;
import com.flower.entity.AbnormalOrder;
import com.flower.entity.Order;
import com.flower.entity.OrderItem;
import com.flower.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单管理控制器
 */
@RestController
@RequestMapping("/api/orders")
@Api(tags = "订单管理接口")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @ApiOperation("分页查询订单列表")
    public Result<Page<Order>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(orderService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询订单")
    public Result<Order> getById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    @GetMapping("/no/{orderNo}")
    @ApiOperation("根据订单编号查询")
    public Result<Order> getByOrderNo(@PathVariable String orderNo) {
        Order order = orderService.findByOrderNo(orderNo);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    @PostMapping
    @ApiOperation("创建订单")
    public Result<Order> create(@RequestBody Order order) {
        return Result.success(orderService.save(order));
    }

    @PutMapping("/{id}/accept")
    @ApiOperation("审核/接单")
    public Result<Order> accept(@PathVariable Long id, @RequestParam(required = false) String remark) {
        Order order = orderService.acceptOrder(id, remark);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    @PutMapping("/{id}/status")
    @ApiOperation("更新订单状态")
    public Result<Order> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Order order = orderService.updateStatus(id, status);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    @GetMapping("/{id}/items")
    @ApiOperation("获取订单明细")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long id) {
        return Result.success(orderService.getOrderItems(id));
    }

    // ========== 异常订单 ==========

    @PostMapping("/{id}/abnormal")
    @ApiOperation("创建异常订单")
    public Result<AbnormalOrder> createAbnormal(
            @PathVariable Long id,
            @RequestParam Integer type,
            @RequestParam String reason,
            @RequestParam(required = false) BigDecimal amount) {
        AbnormalOrder abnormalOrder = orderService.createAbnormalOrder(id, type, reason, amount);
        return abnormalOrder != null ? Result.success(abnormalOrder) : Result.error("订单不存在");
    }

    @PutMapping("/abnormal/{abnormalId}/handle")
    @ApiOperation("处理异常订单")
    public Result<AbnormalOrder> handleAbnormal(
            @PathVariable Long abnormalId,
            @RequestParam String result,
            @RequestParam String handler) {
        AbnormalOrder abnormalOrder = orderService.handleAbnormalOrder(abnormalId, result, handler);
        return abnormalOrder != null ? Result.success(abnormalOrder) : Result.error("异常订单不存在");
    }

    @GetMapping("/abnormal")
    @ApiOperation("获取异常订单列表")
    public Result<List<AbnormalOrder>> getAbnormalOrders(
            @RequestParam(required = false) Integer status) {
        return Result.success(orderService.getAbnormalOrders(status));
    }
}
