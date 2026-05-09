package com.production.controller;

import com.production.entity.*;
import com.production.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 加工流程控制器
 */
@Controller
@RequestMapping("/processing")
public class ProcessingRecordController {

    @Autowired
    private ProcessingRecordService processingRecordService;

    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ProcessParameterService processParameterService;

    /**
     * 加工记录列表页面
     */
    @GetMapping
    public String list(Model model, 
                       @RequestParam(required = false) Long planId, 
                       @RequestParam(required = false) String status,
                       @RequestParam(defaultValue = "0") int page, 
                       @RequestParam(defaultValue = "10") int size) {
        Page<ProcessingRecord> records = processingRecordService.findByPlanIdPage(planId, page, size);
        model.addAttribute("records", records.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", records.getTotalPages());
        model.addAttribute("totalElements", records.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("plans", productionPlanService.findAllActive());
        model.addAttribute("currentPlanId", planId);
        return "processing/list";
    }

    /**
     * 新增加工记录页面
     */
    @GetMapping("/add")
    public String addForm(Model model, @RequestParam(required = false) Long planId) {
        ProcessingRecord record = new ProcessingRecord();
        if (planId != null) {
            Optional<ProductionPlan> planOpt = productionPlanService.findById(planId);
            planOpt.ifPresent(record::setProductionPlan);
        }
        model.addAttribute("record", record);
        model.addAttribute("plans", productionPlanService.findAllActive());
        model.addAttribute("equipments", equipmentService.findAllOnline());
        return "processing/form";
    }

    /**
     * 编辑加工记录页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProcessingRecord> recordOpt = processingRecordService.findById(id);
        if (recordOpt.isPresent()) {
            ProcessingRecord record = recordOpt.get();
            model.addAttribute("record", record);
            model.addAttribute("plans", productionPlanService.findAllActive());
            model.addAttribute("equipments", equipmentService.findAllOnline());
            List<ProcessParameter> parameters = processParameterService.findByRecordId(id);
            model.addAttribute("parameters", parameters);
            return "processing/form";
        }
        redirectAttributes.addFlashAttribute("error", "记录不存在");
        return "redirect:/processing";
    }

    /**
     * 保存加工记录
     */
    @PostMapping("/save")
    public String save(@ModelAttribute ProcessingRecord record,
                       @RequestParam Long planId,
                       @RequestParam(required = false) Long equipmentId,
                       RedirectAttributes redirectAttributes) {
        try {
            Optional<ProductionPlan> planOpt = productionPlanService.findById(planId);
            planOpt.ifPresent(record::setProductionPlan);
            
            if (equipmentId != null) {
                Optional<Equipment> equipmentOpt = equipmentService.findById(equipmentId);
                equipmentOpt.ifPresent(record::setEquipment);
            }
            
            processingRecordService.save(record);
            redirectAttributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/processing";
    }

    /**
     * 删除加工记录
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            processingRecordService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/processing";
    }

    /**
     * 更新加工状态
     */
    @GetMapping("/status/{id}/{status}")
    public String updateStatus(@PathVariable Long id, @PathVariable String status, RedirectAttributes redirectAttributes) {
        try {
            processingRecordService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "状态更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "状态更新失败：" + e.getMessage());
        }
        return "redirect:/processing";
    }

    /**
     * 查看详细参数
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProcessingRecord> recordOpt = processingRecordService.findById(id);
        if (recordOpt.isPresent()) {
            model.addAttribute("record", recordOpt.get());
            model.addAttribute("parameters", processParameterService.findByRecordId(id));
            return "processing/detail";
        }
        redirectAttributes.addFlashAttribute("error", "记录不存在");
        return "redirect:/processing";
    }
}