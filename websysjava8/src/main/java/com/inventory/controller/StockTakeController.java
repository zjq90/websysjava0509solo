package com.inventory.controller;

import com.inventory.common.Result;
import com.inventory.entity.StockTake;
import com.inventory.entity.StockTakeItem;
import com.inventory.service.StockTakeService;
import com.inventory.service.WarehouseService;
import com.inventory.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 盘点管理Controller
 * 处理库存盘点的全流程
 */
@Controller
@RequestMapping("/stocktake")
public class StockTakeController {

    @Autowired
    private StockTakeService stockTakeService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private StoreService storeService;

    /**
     * 盘点单列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<StockTake> stockTakes = stockTakeService.findAll();
        model.addAttribute("stockTakes", stockTakes);
        return "stocktake/list";
    }

    /**
     * 新增盘点单页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("stockTake", new StockTake());
        model.addAttribute("warehouses", warehouseService.findByStatus(1));
        model.addAttribute("stores", storeService.findByStatus(1));
        return "stocktake/form";
    }

    /**
     * 盘点单详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        StockTake stockTake = stockTakeService.findById(id);
        List<StockTakeItem> items = stockTakeService.findItemsByTakeId(id);
        model.addAttribute("stockTake", stockTake);
        model.addAttribute("items", items);
        return "stocktake/detail";
    }

    /**
     * 创建盘点单
     */
    @PostMapping("/create")
    @ResponseBody
    public Result<StockTake> create(@RequestBody StockTake stockTake) {
        try {
            StockTake saved = stockTakeService.createStockTake(stockTake);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 开始盘点
     */
    @PostMapping("/start/{id}")
    @ResponseBody
    public Result<StockTake> start(@PathVariable Long id, @RequestParam String taker) {
        try {
            StockTake stockTake = stockTakeService.startStockTake(id, taker);
            return Result.success("开始盘点", stockTake);
        } catch (Exception e) {
            return Result.error("开始失败：" + e.getMessage());
        }
    }

    /**
     * 开始盘点 (API版本)
     */
    @PostMapping("/api/start/{id}")
    @ResponseBody
    public Result<StockTake> startApi(@PathVariable Long id) {
        try {
            StockTake stockTake = stockTakeService.startStockTake(id, "admin");
            return Result.success("开始盘点", stockTake);
        } catch (Exception e) {
            return Result.error("开始失败：" + e.getMessage());
        }
    }

    /**
     * 更新盘点明细
     */
    @PostMapping("/item/update/{id}")
    @ResponseBody
    public Result<StockTakeItem> updateItem(@PathVariable Long id,
                                             @RequestParam Integer actualQuantity,
                                             @RequestParam(required = false) String description) {
        try {
            StockTakeItem item = stockTakeService.updateItem(id, actualQuantity, description);
            return Result.success("更新成功", item);
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 完成盘点
     */
    @PostMapping("/complete/{id}")
    @ResponseBody
    public Result<StockTake> complete(@PathVariable Long id, @RequestParam String rechecker) {
        try {
            StockTake stockTake = stockTakeService.completeStockTake(id, rechecker);
            return Result.success("完成盘点", stockTake);
        } catch (Exception e) {
            return Result.error("完成失败：" + e.getMessage());
        }
    }

    /**
     * 完成盘点 (API版本)
     */
    @PostMapping("/api/complete/{id}")
    @ResponseBody
    public Result<StockTake> completeApi(@PathVariable Long id) {
        try {
            StockTake stockTake = stockTakeService.completeStockTake(id, "admin");
            return Result.success("完成盘点", stockTake);
        } catch (Exception e) {
            return Result.error("完成失败：" + e.getMessage());
        }
    }

    /**
     * 取消盘点
     */
    @PostMapping("/cancel/{id}")
    @ResponseBody
    public Result<StockTake> cancel(@PathVariable Long id) {
        try {
            StockTake stockTake = stockTakeService.cancelStockTake(id);
            return Result.success("取消成功", stockTake);
        } catch (Exception e) {
            return Result.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 取消盘点 (API版本)
     */
    @PostMapping("/api/cancel/{id}")
    @ResponseBody
    public Result<StockTake> cancelApi(@PathVariable Long id) {
        try {
            StockTake stockTake = stockTakeService.cancelStockTake(id);
            return Result.success("取消成功", stockTake);
        } catch (Exception e) {
            return Result.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 获取盘点单列表（API）
     */
    @GetMapping("/api/list")
    @ResponseBody
    public Result<List<StockTake>> getList() {
        List<StockTake> stockTakes = stockTakeService.findAll();
        return Result.success(stockTakes);
    }

    /**
     * 获取盘点单详情（API）
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Result<StockTake> getById(@PathVariable Long id) {
        StockTake stockTake = stockTakeService.findById(id);
        if (stockTake == null) {
            return Result.error("盘点单不存在");
        }
        return Result.success(stockTake);
    }

    /**
     * 获取盘点明细（API）
     */
    @GetMapping("/api/items/{takeId}")
    @ResponseBody
    public Result<List<StockTakeItem>> getItems(@PathVariable Long takeId) {
        List<StockTakeItem> items = stockTakeService.findItemsByTakeId(takeId);
        return Result.success(items);
    }
}
