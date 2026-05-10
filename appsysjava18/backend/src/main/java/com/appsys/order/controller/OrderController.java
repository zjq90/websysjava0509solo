package com.appsys.order.controller;

import com.appsys.common.result.PageResult;
import com.appsys.common.result.Result;
import com.appsys.order.dto.OrderDTO;
import com.appsys.order.entity.Order;
import com.appsys.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理控制器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Tag(name = "订单管理", description = "订单增删改查接口")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单列表
     */
    @Operation(summary = "分页查询订单列表", description = "根据关键字分页查询订单信息")
    @GetMapping
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<PageResult<Order>> list(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "搜索关键字（订单号或客户名称）") @RequestParam(required = false) String keyword) {
        PageResult<Order> result = orderService.list(page, size, keyword);
        return Result.success(result);
    }

    /**
     * 根据ID查询订单详情
     */
    @Operation(summary = "查询订单详情", description = "根据ID查询订单详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Order> getById(@Parameter(description = "订单ID") @PathVariable Long id) {
        Order order = orderService.getById(id);
        return Result.success(order);
    }

    /**
     * 创建订单
     */
    @Operation(summary = "创建订单", description = "创建新订单")
    @PostMapping
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Order> create(@Validated @RequestBody OrderDTO dto) {
        Order order = orderService.create(dto);
        return Result.success("订单创建成功", order);
    }

    /**
     * 更新订单状态
     */
    @Operation(summary = "更新订单状态", description = "更新订单状态：1-待发货，2-已发货，3-已完成，4-已取消")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Order> updateStatus(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "订单状态：1-待发货，2-已发货，3-已完成，4-已取消") @RequestParam Integer status) {
        Order order = orderService.updateStatus(id, status);
        return Result.success("订单状态更新成功", order);
    }

    /**
     * 删除订单
     */
    @Operation(summary = "删除订单", description = "删除订单（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALESMAN', 'MANAGER', 'ADMIN')")
    public Result<Void> delete(@Parameter(description = "订单ID") @PathVariable Long id) {
        orderService.delete(id);
        return Result.success("订单删除成功", null);
    }
}
