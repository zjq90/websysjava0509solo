package com.inventory.controller;

import com.inventory.common.Result;
import com.inventory.entity.Inventory;
import com.inventory.service.InventoryService;
import com.inventory.service.SeedBatchService;
import com.inventory.service.VarietyService;
import com.inventory.service.WarehouseService;
import com.inventory.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存管理Controller
 * 处理库存的增删改查、入库、出库（先进先出）、预警等功能
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private SeedBatchService seedBatchService;

    @Autowired
    private VarietyService varietyService;

    /**
     * 库存列表页面
     */
    @GetMapping("/list")
    public String list(@RequestParam(required = false) Integer type, 
                       @RequestParam(required = false) Long locationId,
                       Model model) {
        model.addAttribute("warehouses", warehouseService.findByStatus(1));
        model.addAttribute("stores", storeService.findByStatus(1));
        model.addAttribute("batches", seedBatchService.findAll());
        model.addAttribute("varieties", varietyService.findAll());
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedLocationId", locationId);
        
        List<Inventory> inventories;
        if (type != null && locationId != null) {
            inventories = inventoryService.findByLocation(type, locationId);
            
            // 统计数据
            Integer totalQty = inventoryService.sumQuantityByLocation(type, locationId);
            Integer availableQty = inventoryService.sumAvailableQuantityByLocation(type, locationId);
            model.addAttribute("totalQty", totalQty);
            model.addAttribute("availableQty", availableQty);
        } else {
            // 默认显示所有库存
            inventories = inventoryService.findAll();
        }
        
        model.addAttribute("inventories", inventories);
        model.addAttribute("inventoryList", inventories);
        
        return "inventory/list";
    }

    /**
     * 入库页面
     */
    @GetMapping("/inbound")
    public String inboundForm(Model model) {
        model.addAttribute("warehouses", warehouseService.findByStatus(1));
        model.addAttribute("stores", storeService.findByStatus(1));
        model.addAttribute("batches", seedBatchService.findAll());
        return "inventory/inbound";
    }

    /**
     * 执行入库操作
     */
    @PostMapping("/inbound")
    @ResponseBody
    public Result<Inventory> inbound(@RequestBody Map<String, Object> params) {
        try {
            Integer inventoryType = Integer.valueOf(params.get("inventoryType").toString());
            Long locationId = Long.valueOf(params.get("locationId").toString());
            Long batchId = Long.valueOf(params.get("batchId").toString());
            Integer quantity = Integer.valueOf(params.get("quantity").toString());
            String inboundNo = params.get("inboundNo") != null ? params.get("inboundNo").toString() : null;
            
            Inventory inventory = inventoryService.inbound(inventoryType, locationId, batchId, quantity, inboundNo);
            return Result.success("入库成功", inventory);
        } catch (Exception e) {
            return Result.error("入库失败：" + e.getMessage());
        }
    }

    /**
     * 出库页面（先进先出）
     */
    @GetMapping("/outbound")
    public String outboundForm(Model model) {
        model.addAttribute("warehouses", warehouseService.findByStatus(1));
        model.addAttribute("stores", storeService.findByStatus(1));
        model.addAttribute("varieties", varietyService.findAll());
        return "inventory/outbound";
    }

    /**
     * 执行出库操作（先进先出）
     */
    @PostMapping("/outbound")
    @ResponseBody
    public Result<Integer> outbound(@RequestBody Map<String, Object> params) {
        try {
            Integer inventoryType = Integer.valueOf(params.get("inventoryType").toString());
            Long locationId = Long.valueOf(params.get("locationId").toString());
            Long varietyId = Long.valueOf(params.get("varietyId").toString());
            Integer quantity = Integer.valueOf(params.get("quantity").toString());
            
            int outboundQty = inventoryService.outboundFIFO(inventoryType, locationId, varietyId, quantity);
            return Result.success("出库成功，实际出库：" + outboundQty + " 单位", outboundQty);
        } catch (Exception e) {
            return Result.error("出库失败：" + e.getMessage());
        }
    }

    /**
     * 执行出库操作（先进先出）- FIFO别名
     */
    @PostMapping("/outbound-fifo")
    @ResponseBody
    public Result<List<Inventory>> outboundFIFO(@RequestBody Map<String, Object> params) {
        try {
            Integer inventoryType = Integer.valueOf(params.get("inventoryType").toString());
            Long locationId = Long.valueOf(params.get("locationId").toString());
            Long varietyId = Long.valueOf(params.get("varietyId").toString());
            Integer quantity = Integer.valueOf(params.get("quantity").toString());
            
            List<Inventory> result = inventoryService.outboundFIFODetail(inventoryType, locationId, varietyId, quantity);
            return Result.success("出库成功", result);
        } catch (Exception e) {
            return Result.error("出库失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取库存（API）
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Result<Inventory> getById(@PathVariable Long id) {
        Inventory inventory = inventoryService.findById(id);
        if (inventory == null) {
            return Result.error("库存记录不存在");
        }
        return Result.success(inventory);
    }

    /**
     * 获取先进先出的库存列表（API）
     */
    @GetMapping("/api/fifo")
    @ResponseBody
    public Result<List<Inventory>> getFIFOInventory(@RequestParam Integer type,
                                                     @RequestParam Long locationId,
                                                     @RequestParam Long varietyId) {
        List<Inventory> list = inventoryService.getFIFOInventory(type, locationId, varietyId);
        return Result.success(list);
    }

    /**
     * 预警页面
     */
    @GetMapping("/warning")
    public String warning(Model model) {
        List<Inventory> lowStock = inventoryService.findLowStockInventory();
        List<Inventory> highStock = inventoryService.findHighStockInventory();
        List<Inventory> expired = inventoryService.findExpiredInventory();
        List<Inventory> nearExpiry = inventoryService.findNearExpiryInventory();
        
        model.addAttribute("lowStockList", lowStock);
        model.addAttribute("highStockList", highStock);
        model.addAttribute("expiredList", expired);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("highStock", highStock);
        model.addAttribute("expired", expired);
        model.addAttribute("nearExpiry", nearExpiry);
        
        return "inventory/warning";
    }

    /**
     * 获取预警数据（API）
     */
    @GetMapping("/api/warning")
    @ResponseBody
    public Result<Map<String, List<Inventory>>> getWarningData() {
        Map<String, List<Inventory>> data = new HashMap<>();
        data.put("lowStock", inventoryService.findLowStockInventory());
        data.put("highStock", inventoryService.findHighStockInventory());
        data.put("expired", inventoryService.findExpiredInventory());
        return Result.success(data);
    }

    /**
     * 删除库存记录
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Long id) {
        try {
            inventoryService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据位置查询库存（API）
     */
    @GetMapping("/api/location")
    @ResponseBody
    public Result<List<Inventory>> getByLocation(@RequestParam Integer type, @RequestParam Long locationId) {
        List<Inventory> list = inventoryService.findByLocation(type, locationId);
        return Result.success(list);
    }
}
