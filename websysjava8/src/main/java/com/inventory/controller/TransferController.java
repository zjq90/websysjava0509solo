package com.inventory.controller;

import com.inventory.common.Result;
import com.inventory.entity.TransferOrder;
import com.inventory.entity.TransferOrderItem;
import com.inventory.service.TransferService;
import com.inventory.service.WarehouseService;
import com.inventory.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调拨管理Controller
 * 处理库存调拨的全流程
 */
@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private StoreService storeService;

    /**
     * 调拨单列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<TransferOrder> orders = transferService.findAll();
        model.addAttribute("orders", orders);
        return "transfer/list";
    }

    /**
     * 新增调拨单页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("order", new TransferOrder());
        model.addAttribute("warehouses", warehouseService.findByStatus(1));
        model.addAttribute("stores", storeService.findByStatus(1));
        return "transfer/form";
    }

    /**
     * 调拨单详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        TransferOrder order = transferService.findById(id);
        List<TransferOrderItem> items = transferService.findItemsByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "transfer/detail";
    }

    /**
     * 审核调拨单
     */
    @PostMapping("/audit/{id}")
    @ResponseBody
    public Result<TransferOrder> audit(@PathVariable Long id, 
                                        @RequestParam String auditor,
                                        @RequestParam boolean approved) {
        try {
            TransferOrder order = transferService.auditOrder(id, auditor, approved);
            return Result.success(approved ? "审核通过" : "审核拒绝", order);
        } catch (Exception e) {
            return Result.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 审核调拨单 (API版本)
     */
    @PostMapping("/api/approve/{id}")
    @ResponseBody
    public Result<TransferOrder> approveApi(@PathVariable Long id) {
        try {
            TransferOrder order = transferService.auditOrder(id, "admin", true);
            return Result.success("审核通过", order);
        } catch (Exception e) {
            return Result.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 执行出库
     */
    @PostMapping("/outbound/{id}")
    @ResponseBody
    public Result<TransferOrder> outbound(@PathVariable Long id, @RequestParam String person) {
        try {
            TransferOrder order = transferService.executeOutbound(id, person);
            return Result.success("出库成功", order);
        } catch (Exception e) {
            return Result.error("出库失败：" + e.getMessage());
        }
    }

    /**
     * 执行出库 (API版本)
     */
    @PostMapping("/api/outbound/{id}")
    @ResponseBody
    public Result<TransferOrder> outboundApi(@PathVariable Long id) {
        try {
            TransferOrder order = transferService.executeOutbound(id, "admin");
            return Result.success("出库成功", order);
        } catch (Exception e) {
            return Result.error("出库失败：" + e.getMessage());
        }
    }

    /**
     * 执行入库
     */
    @PostMapping("/inbound/{id}")
    @ResponseBody
    public Result<TransferOrder> inbound(@PathVariable Long id, @RequestParam String person) {
        try {
            TransferOrder order = transferService.executeInbound(id, person);
            return Result.success("入库成功", order);
        } catch (Exception e) {
            return Result.error("入库失败：" + e.getMessage());
        }
    }

    /**
     * 执行入库 (API版本)
     */
    @PostMapping("/api/inbound/{id}")
    @ResponseBody
    public Result<TransferOrder> inboundApi(@PathVariable Long id) {
        try {
            TransferOrder order = transferService.executeInbound(id, "admin");
            return Result.success("入库成功", order);
        } catch (Exception e) {
            return Result.error("入库失败：" + e.getMessage());
        }
    }

    /**
     * 取消调拨单
     */
    @PostMapping("/cancel/{id}")
    @ResponseBody
    public Result<TransferOrder> cancel(@PathVariable Long id) {
        try {
            TransferOrder order = transferService.cancelOrder(id);
            return Result.success("取消成功", order);
        } catch (Exception e) {
            return Result.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 取消调拨单 (API版本)
     */
    @PostMapping("/api/cancel/{id}")
    @ResponseBody
    public Result<TransferOrder> cancelApi(@PathVariable Long id) {
        try {
            TransferOrder order = transferService.cancelOrder(id);
            return Result.success("取消成功", order);
        } catch (Exception e) {
            return Result.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 获取调拨单列表（API）
     */
    @GetMapping("/api/list")
    @ResponseBody
    public Result<List<TransferOrder>> getList() {
        List<TransferOrder> orders = transferService.findAll();
        return Result.success(orders);
    }

    /**
     * 获取调拨单详情（API）
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Result<TransferOrder> getById(@PathVariable Long id) {
        TransferOrder order = transferService.findById(id);
        if (order == null) {
            return Result.error("调拨单不存在");
        }
        return Result.success(order);
    }
}
