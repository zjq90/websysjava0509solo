package com.production.controller;

import com.production.entity.Equipment;
import com.production.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 设备管理控制器
 */
@Controller
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 设备列表页面
     */
    @GetMapping
    public String list(Model model, 
                       @RequestParam(required = false) String status,
                       @RequestParam(defaultValue = "0") int page, 
                       @RequestParam(defaultValue = "10") int size) {
        Page<Equipment> equipments = equipmentService.findAll(page, size);
        model.addAttribute("equipments", equipments.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipments.getTotalPages());
        model.addAttribute("totalElements", equipments.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("currentStatus", status);
        return "equipment/list";
    }

    /**
     * 新增设备页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        return "equipment/form";
    }

    /**
     * 编辑设备页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Equipment> equipmentOpt = equipmentService.findById(id);
        if (equipmentOpt.isPresent()) {
            model.addAttribute("equipment", equipmentOpt.get());
            return "equipment/form";
        }
        redirectAttributes.addFlashAttribute("error", "设备不存在");
        return "redirect:/equipment";
    }

    /**
     * 保存设备
     */
    @PostMapping("/save")
    public String save(@ModelAttribute Equipment equipment, RedirectAttributes redirectAttributes) {
        try {
            equipmentService.save(equipment);
            redirectAttributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/equipment";
    }

    /**
     * 删除设备
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            equipmentService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/equipment";
    }

    /**
     * 更新设备状态
     */
    @GetMapping("/status/{id}/{status}")
    public String updateStatus(@PathVariable Long id, @PathVariable String status, RedirectAttributes redirectAttributes) {
        try {
            equipmentService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "状态更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "状态更新失败：" + e.getMessage());
        }
        return "redirect:/equipment";
    }
}