package com.inventory.controller;

import com.inventory.common.Result;
import com.inventory.entity.SeedBatch;
import com.inventory.service.SeedBatchService;
import com.inventory.service.VarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 种子批次管理Controller
 * 处理种子批次的增删改查，以及近效期、过期预警
 */
@Controller
@RequestMapping("/batch")
public class SeedBatchController {

    @Autowired
    private SeedBatchService seedBatchService;

    @Autowired
    private VarietyService varietyService;

    /**
     * 批次列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<SeedBatch> batches = seedBatchService.findAll();
        model.addAttribute("batches", batches);
        model.addAttribute("varieties", varietyService.findAll());
        return "batch/list";
    }

    /**
     * 新增批次页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("batch", new SeedBatch());
        model.addAttribute("varieties", varietyService.findAll());
        return "batch/form";
    }

    /**
     * 编辑批次页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        SeedBatch batch = seedBatchService.findById(id);
        model.addAttribute("batch", batch);
        model.addAttribute("varieties", varietyService.findAll());
        return "batch/form";
    }

    /**
     * 保存批次
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<SeedBatch> save(@RequestBody SeedBatch batch) {
        try {
            if (batch.getId() == null && seedBatchService.existsByBatchNo(batch.getBatchNo())) {
                return Result.error("批次号已存在");
            }
            SeedBatch saved = seedBatchService.save(batch);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除批次
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Long id) {
        try {
            seedBatchService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 近效期预警页面
     */
    @GetMapping("/near-expiry")
    public String nearExpiry(Model model) {
        List<SeedBatch> nearExpiry = seedBatchService.findNearExpiryBatches(30);
        List<SeedBatch> expired = seedBatchService.findExpiredBatches();
        model.addAttribute("nearExpiryList", nearExpiry);
        model.addAttribute("expiredList", expired);
        return "batch/expiry-warning";
    }

    /**
     * 更新批次状态（检查近效期和过期）
     */
    @PostMapping("/update-status")
    @ResponseBody
    public Result<Void> updateStatus() {
        try {
            seedBatchService.updateBatchStatus(30);
            return Result.success();
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有批次（API）
     */
    @GetMapping("/api/list")
    @ResponseBody
    public Result<List<SeedBatch>> getList() {
        List<SeedBatch> batches = seedBatchService.findAll();
        return Result.success(batches);
    }

    /**
     * 根据品种ID获取批次（API）
     */
    @GetMapping("/api/variety/{varietyId}")
    @ResponseBody
    public Result<List<SeedBatch>> getByVariety(@PathVariable Long varietyId) {
        List<SeedBatch> batches = seedBatchService.findByVarietyId(varietyId);
        return Result.success(batches);
    }

    /**
     * 获取预警数据（API）
     */
    @GetMapping("/api/warning")
    @ResponseBody
    public Result<Map<String, List<SeedBatch>>> getWarningData() {
        Map<String, List<SeedBatch>> data = new HashMap<>();
        data.put("nearExpiry", seedBatchService.findNearExpiryBatches(30));
        data.put("expired", seedBatchService.findExpiredBatches());
        return Result.success(data);
    }
}
