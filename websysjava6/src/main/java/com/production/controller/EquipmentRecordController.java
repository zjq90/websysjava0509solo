package com.production.controller;

import com.production.entity.Equipment;
import com.production.entity.EquipmentRecord;
import com.production.service.EquipmentRecordService;
import com.production.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 设备记录控制器
 */
@Controller
@RequestMapping("/equipment-records")
public class EquipmentRecordController {

    @Autowired
    private EquipmentRecordService equipmentRecordService;

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 设备记录列表页面
     */
    @GetMapping
    public String list(Model model, 
                       @RequestParam(required = false) Long equipmentId,
                       @RequestParam(defaultValue = "0") int page, 
                       @RequestParam(defaultValue = "10") int size) {
        Page<EquipmentRecord> records = equipmentRecordService.findByEquipmentIdPage(equipmentId, page, size);
        model.addAttribute("records", records.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", records.getTotalPages());
        model.addAttribute("totalElements", records.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("equipments", equipmentService.findAll());
        model.addAttribute("currentEquipmentId", equipmentId);
        return "equipment-records/list";
    }

    /**
     * 新增记录页面（模拟手动录入）
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("record", new EquipmentRecord());
        model.addAttribute("equipments", equipmentService.findAll());
        return "equipment-records/form";
    }

    /**
     * 保存记录
     */
    @PostMapping("/save")
    public String save(@ModelAttribute EquipmentRecord record,
                       @RequestParam Long equipmentId,
                       RedirectAttributes redirectAttributes) {
        try {
            Optional<Equipment> equipmentOpt = equipmentService.findById(equipmentId);
            equipmentOpt.ifPresent(record::setEquipment);
            record.setDataSource("MANUAL");
            equipmentRecordService.save(record);
            redirectAttributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/equipment-records";
    }

    /**
     * 删除记录
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            equipmentRecordService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/equipment-records";
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<EquipmentRecord> recordOpt = equipmentRecordService.findById(id);
        if (recordOpt.isPresent()) {
            model.addAttribute("record", recordOpt.get());
            return "equipment-records/detail";
        }
        redirectAttributes.addFlashAttribute("error", "记录不存在");
        return "redirect:/equipment-records";
    }
}