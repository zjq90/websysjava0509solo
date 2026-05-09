package com.production.controller.api;

import com.production.entity.ProductionPlan;
import com.production.service.ProductionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产计划API控制器
 * 提供给前端可视化排产使用
 */
@RestController
@RequestMapping("/api/plans")
public class ProductionPlanApiController {

    @Autowired
    private ProductionPlanService productionPlanService;

    /**
     * 获取指定日期范围的生产计划（用于甘特图）
     */
    @GetMapping("/gantt")
    public List<ProductionPlan> getGanttData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return productionPlanService.findByDateRange(start, end);
    }

    /**
     * 获取所有活跃计划
     */
    @GetMapping("/active")
    public List<ProductionPlan> getActivePlans() {
        return productionPlanService.findAllActive();
    }

    /**
     * 更新计划状态
     */
    @PostMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            ProductionPlan plan = productionPlanService.updateStatus(id, status);
            result.put("success", true);
            result.put("data", plan);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}