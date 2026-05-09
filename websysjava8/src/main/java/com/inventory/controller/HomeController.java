package com.inventory.controller;

import com.inventory.common.Result;
import com.inventory.entity.*;
import com.inventory.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页Controller
 * 处理系统首页、仪表盘等页面
 */
@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VarietyService varietyService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private SeedBatchService seedBatchService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private EnvRecordService envRecordService;

    /**
     * 系统首页
     */
    @GetMapping("/")
    public String index(Model model) {
        // 统计数据
        model.addAttribute("categoryCount", categoryService.findAll().size());
        model.addAttribute("varietyCount", varietyService.findAll().size());
        model.addAttribute("warehouseCount", warehouseService.findByStatus(1).size());
        model.addAttribute("storeCount", storeService.findByStatus(1).size());
        
        // 预警数据
        List<SeedBatch> nearExpiry = seedBatchService.findNearExpiryBatches(30);
        List<SeedBatch> expired = seedBatchService.findExpiredBatches();
        List<Inventory> lowStock = inventoryService.findLowStockInventory();
        List<EnvRecord> abnormalEnv = envRecordService.findAbnormalRecords();
        
        model.addAttribute("nearExpiryCount", nearExpiry.size());
        model.addAttribute("expiredCount", expired.size());
        model.addAttribute("lowStockCount", lowStock.size());
        model.addAttribute("abnormalEnvCount", abnormalEnv.size());
        
        return "index";
    }

    /**
     * 获取仪表盘统计数据（API）
     */
    @GetMapping("/api/dashboard")
    @ResponseBody
    public Result<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        
        // 基础统计
        data.put("categoryCount", categoryService.findAll().size());
        data.put("varietyCount", varietyService.findAll().size());
        data.put("warehouseCount", warehouseService.findByStatus(1).size());
        data.put("storeCount", storeService.findByStatus(1).size());
        
        // 预警数据
        List<SeedBatch> nearExpiry = seedBatchService.findNearExpiryBatches(30);
        List<SeedBatch> expired = seedBatchService.findExpiredBatches();
        List<Inventory> lowStock = inventoryService.findLowStockInventory();
        List<EnvRecord> abnormalEnv = envRecordService.findAbnormalRecords();
        
        data.put("nearExpiryCount", nearExpiry.size());
        data.put("nearExpiryList", nearExpiry);
        data.put("expiredCount", expired.size());
        data.put("expiredList", expired);
        data.put("lowStockCount", lowStock.size());
        data.put("lowStockList", lowStock);
        data.put("abnormalEnvCount", abnormalEnv.size());
        data.put("abnormalEnvList", abnormalEnv);
        
        return Result.success(data);
    }
}
