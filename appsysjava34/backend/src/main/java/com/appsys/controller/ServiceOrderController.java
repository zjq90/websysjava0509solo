package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.ServiceOrder;
import com.appsys.service.ServiceOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service-order")
@Tag(name = "服务工单管理", description = "服务工单相关接口")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户的工单列表")
    public Result<List<ServiceOrder>> listByUserId(@PathVariable Long userId) {
        return Result.success(serviceOrderService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取工单")
    public Result<ServiceOrder> getById(@PathVariable Long id) {
        Optional<ServiceOrder> order = serviceOrderService.findById(id);
        return order.map(Result::success).orElseGet(() -> Result.error("工单不存在"));
    }

    @GetMapping("/orderNo/{orderNo}")
    @Operation(summary = "根据工单号获取工单")
    public Result<ServiceOrder> getByOrderNo(@PathVariable String orderNo) {
        Optional<ServiceOrder> order = serviceOrderService.findByOrderNo(orderNo);
        return order.map(Result::success).orElseGet(() -> Result.error("工单不存在"));
    }

    @PostMapping("/create")
    @Operation(summary = "创建工单")
    public Result<ServiceOrder> create(@RequestBody ServiceOrder serviceOrder) {
        return Result.success(serviceOrderService.save(serviceOrder));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新工单状态")
    public Result<ServiceOrder> updateStatus(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) Integer step) {
        ServiceOrder order = serviceOrderService.updateStatus(id, status, step);
        if (order != null) {
            return Result.success(order);
        }
        return Result.error("工单不存在");
    }

    @PutMapping("/{id}/location")
    @Operation(summary = "更新装维人员位置")
    public Result<ServiceOrder> updateWorkerLocation(@PathVariable Long id, @RequestParam Double lat, @RequestParam Double lng) {
        ServiceOrder order = serviceOrderService.updateWorkerLocation(id, lat, lng);
        if (order != null) {
            return Result.success(order);
        }
        return Result.error("工单不存在");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除工单")
    public Result<Void> delete(@PathVariable Long id) {
        serviceOrderService.deleteById(id);
        return Result.success();
    }
}
