package com.production.controller;

import com.production.entity.*;
import com.production.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 测试数据控制器
 * 提供测试功能辅助完成生成功能的测试
 */
@Controller
@RequestMapping("/test-data")
public class TestDataController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private ProcessingRecordService processingRecordService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentRecordService equipmentRecordService;

    private Random random = new Random();

    /**
     * 测试数据管理页面
     */
    @GetMapping
    public String testDataPage(Model model) {
        Map<String, Long> statistics = new LinkedHashMap<>();
        statistics.put("产品数量", (long) productService.findAll().size());
        statistics.put("生产计划数量", (long) productionPlanService.findAll().size());
        statistics.put("加工记录数量", (long) processingRecordService.findAll().size());
        statistics.put("设备数量", (long) equipmentService.findAll().size());
        statistics.put("设备数据记录", (long) equipmentRecordService.findAll().size());
        
        model.addAttribute("statistics", statistics);
        return "test-data/list";
    }

    /**
     * 模拟设备推送数据（测试API）
     */
    @PostMapping("/simulate-equipment-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> simulateEquipmentData(@RequestParam String equipmentCode) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Equipment> equipmentOpt = equipmentService.findByEquipmentCode(equipmentCode);
        if (!equipmentOpt.isPresent()) {
            result.put("success", false);
            result.put("message", "设备不存在：" + equipmentCode);
            return ResponseEntity.ok(result);
        }
        
        Equipment equipment = equipmentOpt.get();
        
        // 生成模拟数据
        EquipmentRecord record = new EquipmentRecord();
        record.setEquipment(equipment);
        record.setDataSource("AUTO");
        record.setRunStatus("RUNNING");
        record.setCurrentSpeed(new BigDecimal(100 + random.nextInt(50)));
        record.setProductionOutput(new BigDecimal(random.nextInt(1000)));
        record.setRunDuration(new BigDecimal(random.nextInt(480)));
        record.setTemperature(new BigDecimal(35 + random.nextDouble() * 20).setScale(2, BigDecimal.ROUND_HALF_UP));
        record.setPressure(new BigDecimal(0.2 + random.nextDouble() * 0.3).setScale(2, BigDecimal.ROUND_HALF_UP));
        record.setRecordTime(LocalDateTime.now());
        
        // 随机添加告警
        if (random.nextDouble() > 0.9) {
            record.setAlarmMessage("模拟告警：设备温度过高");
        }
        
        equipmentRecordService.save(record);
        
        result.put("success", true);
        result.put("message", "设备数据模拟推送成功");
        result.put("record", record);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 批量模拟设备数据
     */
    @PostMapping("/simulate-batch-equipment-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> simulateBatchEquipmentData(@RequestParam(defaultValue = "10") int count) {
        Map<String, Object> result = new HashMap<>();
        List<Equipment> equipments = equipmentService.findAll();
        
        if (equipments.isEmpty()) {
            result.put("success", false);
            result.put("message", "没有可用设备，请先添加设备");
            return ResponseEntity.ok(result);
        }
        
        int successCount = 0;
        List<Map<String, Object>> records = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            Equipment equipment = equipments.get(random.nextInt(equipments.size()));
            
            EquipmentRecord record = new EquipmentRecord();
            record.setEquipment(equipment);
            record.setDataSource("AUTO");
            record.setRunStatus("RUNNING");
            record.setCurrentSpeed(new BigDecimal(80 + random.nextInt(80)));
            record.setProductionOutput(new BigDecimal(random.nextInt(2000)));
            record.setRunDuration(new BigDecimal(random.nextInt(600)));
            record.setTemperature(new BigDecimal(30 + random.nextDouble() * 30).setScale(2, BigDecimal.ROUND_HALF_UP));
            record.setPressure(new BigDecimal(0.1 + random.nextDouble() * 0.5).setScale(2, BigDecimal.ROUND_HALF_UP));
            record.setRecordTime(LocalDateTime.now().minusMinutes(random.nextInt(60)));
            
            if (random.nextDouble() > 0.95) {
                record.setRunStatus("FAULT");
                record.setAlarmMessage("模拟故障告警");
            }
            
            equipmentRecordService.save(record);
            successCount++;
            
            Map<String, Object> rec = new HashMap<>();
            rec.put("equipment", equipment.getEquipmentName());
            rec.put("temperature", record.getTemperature());
            rec.put("pressure", record.getPressure());
            rec.put("status", record.getRunStatus());
            records.add(rec);
        }
        
        result.put("success", true);
        result.put("message", "成功模拟 " + successCount + " 条设备数据");
        result.put("records", records);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 快速创建测试生产计划
     */
    @PostMapping("/quick-create-plan")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quickCreatePlan(@RequestParam Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Product> productOpt = productService.findById(productId);
        if (!productOpt.isPresent()) {
            result.put("success", false);
            result.put("message", "产品不存在");
            return ResponseEntity.ok(result);
        }
        
        Product product = productOpt.get();
        LocalDateTime now = LocalDateTime.now();
        
        ProductionPlan plan = new ProductionPlan();
        plan.setPlanCode("TEST" + System.currentTimeMillis());
        plan.setPlanName("测试计划-" + product.getProductName());
        plan.setProduct(product);
        plan.setBatchNumber("TEST" + now.getYear() + String.format("%02d%02d", now.getMonthValue(), now.getDayOfMonth()));
        plan.setPlanQuantity(new BigDecimal(1000 + random.nextInt(5000)));
        plan.setProducedQuantity(BigDecimal.ZERO);
        plan.setPlanStartDate(now.toLocalDate());
        plan.setPlanEndDate(now.plusDays(7).toLocalDate());
        plan.setProductionLine("测试线");
        plan.setPriority("MEDIUM");
        plan.setStatus("PENDING");
        plan.setResponsiblePerson("测试员");
        plan.setRemarks("快速创建的测试计划");
        
        productionPlanService.save(plan);
        
        result.put("success", true);
        result.put("message", "测试计划创建成功");
        result.put("plan", plan);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有产品列表（用于测试）
     */
    @GetMapping("/products")
    @ResponseBody
    public List<Product> getProducts() {
        return productService.findAllActive();
    }

    /**
     * 获取所有设备列表（用于测试）
     */
    @GetMapping("/equipments")
    @ResponseBody
    public List<Equipment> getEquipments() {
        return equipmentService.findAll();
    }

    /**
     * 更新生产计划进度（测试用）
     */
    @PostMapping("/update-plan-progress")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePlanProgress(
            @RequestParam Long planId,
            @RequestParam BigDecimal producedQuantity) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<ProductionPlan> planOpt = productionPlanService.findById(planId);
        if (!planOpt.isPresent()) {
            result.put("success", false);
            result.put("message", "计划不存在");
            return ResponseEntity.ok(result);
        }
        
        ProductionPlan plan = planOpt.get();
        plan.setProducedQuantity(producedQuantity);
        
        // 如果达到计划数量，自动标记为完成
        if (producedQuantity.compareTo(plan.getPlanQuantity()) >= 0) {
            plan.setStatus("COMPLETED");
            plan.setActualEndTime(LocalDateTime.now());
        }
        
        productionPlanService.save(plan);
        
        result.put("success", true);
        result.put("message", "进度更新成功");
        result.put("plan", plan);
        
        return ResponseEntity.ok(result);
    }
}