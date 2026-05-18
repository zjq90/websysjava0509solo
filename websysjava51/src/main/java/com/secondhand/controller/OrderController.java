package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.Order;
import com.secondhand.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Tag(name = "订单管理", description = "订单的增删改查、状态更新、物流管理等接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @Operation(summary = "分页查询订单列表")
    public Result<Page<Order>> list(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isAbnormal,
            @RequestParam(required = false) Long buyerId,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return Result.success(orderService.findAll(orderNo, status, isAbnormal, buyerId, sellerId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询订单")
    public Result<Order> getById(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);
        return order.map(Result::success).orElse(Result.error("订单不存在"));
    }

    @PostMapping
    @Operation(summary = "新增订单")
    public Result<Order> create(@RequestBody Order order) {
        return Result.success(orderService.save(order));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新订单")
    public Result<Order> update(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        return Result.success(orderService.save(order));
    }

    @PostMapping("/batch/ship")
    @Operation(summary = "批量发货")
    public Result<Integer> batchShip(@RequestBody List<Long> ids) {
        return Result.success("成功发货" + orderService.batchShip(ids) + "个订单", orderService.batchShip(ids));
    }

    @PostMapping("/batch/refund")
    @Operation(summary = "批量退款")
    public Result<Integer> batchRefund(@RequestBody List<Long> ids) {
        return Result.success("成功退款" + orderService.batchRefund(ids) + "个订单", orderService.batchRefund(ids));
    }

    @PostMapping("/{id}/mark-abnormal")
    @Operation(summary = "标记异常订单")
    public Result<Integer> markAsAbnormal(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String reason = params.get("reason");
        return Result.success(orderService.markAsAbnormal(id, reason));
    }

    @PutMapping("/{id}/logistics")
    @Operation(summary = "更新物流信息")
    public Result<Order> updateLogistics(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String logisticsCompany = params.get("logisticsCompany");
        String trackingNumber = params.get("trackingNumber");
        return Result.success(orderService.updateLogistics(id, logisticsCompany, trackingNumber));
    }

}