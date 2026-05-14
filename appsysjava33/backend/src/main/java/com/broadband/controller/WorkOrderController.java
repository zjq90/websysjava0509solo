package com.broadband.controller;

import com.broadband.common.Result;
import com.broadband.entity.WorkOrder;
import com.broadband.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单控制器
 * 处理新装、移机、销户等工单接口
 * 
 * @author broadband
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/order")
@Tag(name = "工单管理", description = "新装、移机、销户等工单接口")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @Operation(summary = "获取用户工单列表", description = "获取指定用户的所有工单")
    @GetMapping("/user/{userId}")
    public Result<List<WorkOrder>> getUserOrders(@PathVariable Long userId) {
        try {
            List<WorkOrder> orders = workOrderService.getUserOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "按状态获取用户工单", description = "0-待审核 1-待处理 2-处理中 3-已完成 4-已取消")
    @GetMapping("/user/{userId}/status/{status}")
    public Result<List<WorkOrder>> getUserOrdersByStatus(@PathVariable Long userId, @PathVariable Integer status) {
        try {
            List<WorkOrder> orders = workOrderService.getUserOrdersByStatus(userId, status);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取工单详情", description = "根据工单ID获取详细信息")
    @GetMapping("/{orderId}")
    public Result<WorkOrder> getOrderById(@PathVariable Long orderId) {
        try {
            WorkOrder order = workOrderService.getOrderById(orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "创建新装工单", description = "创建宽带新装办理工单")
    @PostMapping("/install")
    public Result<WorkOrder> createInstallOrder(@RequestParam Long userId,
                                                  @RequestParam Long packageId,
                                                  @RequestParam String broadbandNumber,
                                                  @RequestParam String address,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime appointmentTime) {
        try {
            WorkOrder order = workOrderService.createInstallOrder(userId, packageId, broadbandNumber, address, appointmentTime);
            return Result.success("工单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "创建移机工单", description = "创建宽带移机办理工单")
    @PostMapping("/move")
    public Result<WorkOrder> createMoveOrder(@RequestParam Long userId,
                                               @RequestParam String oldAddress,
                                               @RequestParam String newAddress,
                                               @RequestParam(required = false) String addressProofs,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime appointmentTime) {
        try {
            WorkOrder order = workOrderService.createMoveOrder(userId, oldAddress, newAddress, addressProofs, appointmentTime);
            return Result.success("工单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "创建销户工单", description = "创建宽带销户办理工单")
    @PostMapping("/cancel")
    public Result<WorkOrder> createCancelOrder(@RequestParam Long userId,
                                                @RequestParam String broadbandNumber,
                                                @RequestParam(required = false) String description) {
        try {
            WorkOrder order = workOrderService.createCancelOrder(userId, broadbandNumber, description);
            return Result.success("工单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "签署电子合同", description = "完成电子合同签署")
    @PostMapping("/{orderId}/sign")
    public Result<WorkOrder> signContract(@PathVariable Long orderId, @RequestParam String contractPath) {
        try {
            WorkOrder order = workOrderService.signContract(orderId, contractPath);
            return Result.success("签署成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "取消工单", description = "取消未完成的工单")
    @PostMapping("/{orderId}/cancel")
    public Result<WorkOrder> cancelOrder(@PathVariable Long orderId) {
        try {
            WorkOrder order = workOrderService.cancelOrder(orderId);
            return Result.success("工单已取消", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
