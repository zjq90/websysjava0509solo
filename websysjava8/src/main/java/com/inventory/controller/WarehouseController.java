package com.inventory.controller;

import com.inventory.common.Result;
import com.inventory.entity.EnvRecord;
import com.inventory.entity.Warehouse;
import com.inventory.service.EnvRecordService;
import com.inventory.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库管理Controller
 * 处理仓库和环境记录（温湿度）的管理
 */
@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private EnvRecordService envRecordService;

    /**
     * 仓库列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<Warehouse> warehouses = warehouseService.findAll();
        model.addAttribute("warehouses", warehouses);
        return "warehouse/list";
    }

    /**
     * 新增仓库页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("warehouse", new Warehouse());
        return "warehouse/form";
    }

    /**
     * 编辑仓库页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Warehouse warehouse = warehouseService.findById(id);
        model.addAttribute("warehouse", warehouse);
        return "warehouse/form";
    }

    /**
     * 保存仓库
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<Warehouse> save(@RequestBody Warehouse warehouse) {
        try {
            if (warehouse.getId() == null && warehouseService.existsByName(warehouse.getName())) {
                return Result.error("仓库名称已存在");
            }
            Warehouse saved = warehouseService.save(warehouse);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除仓库
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Long id) {
        try {
            warehouseService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 环境监控页面（温湿度记录）
     */
    @GetMapping("/env/{warehouseId}")
    public String envMonitor(@PathVariable Long warehouseId, Model model) {
        Warehouse warehouse = warehouseService.findById(warehouseId);
        List<EnvRecord> records = envRecordService.findByWarehouseId(warehouseId);
        EnvRecord latest = envRecordService.findLatestByWarehouseId(warehouseId);
        
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("records", records);
        model.addAttribute("latest", latest);
        
        return "warehouse/env-monitor";
    }

    /**
     * 添加环境记录（温湿度）
     */
    @PostMapping("/env/add")
    @ResponseBody
    public Result<EnvRecord> addEnvRecord(@RequestBody EnvRecord record) {
        try {
            EnvRecord saved = envRecordService.save(record);
            return Result.success("记录成功", saved);
        } catch (Exception e) {
            return Result.error("记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取仓库列表（API）
     */
    @GetMapping("/api/list")
    @ResponseBody
    public Result<List<Warehouse>> getList() {
        List<Warehouse> warehouses = warehouseService.findAll();
        return Result.success(warehouses);
    }

    /**
     * 获取环境记录（API）
     */
    @GetMapping("/api/env/{warehouseId}")
    @ResponseBody
    public Result<List<EnvRecord>> getEnvRecords(@PathVariable Long warehouseId) {
        List<EnvRecord> records = envRecordService.findByWarehouseId(warehouseId);
        return Result.success(records);
    }

    /**
     * 获取最新环境记录（API）
     */
    @GetMapping("/api/env/latest/{warehouseId}")
    @ResponseBody
    public Result<EnvRecord> getLatestEnvRecord(@PathVariable Long warehouseId) {
        EnvRecord latest = envRecordService.findLatestByWarehouseId(warehouseId);
        return Result.success(latest);
    }
}
