package com.management.controller;

import com.management.entity.Supplier;
import com.management.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 供应商控制器
 * 提供供应商的增删改查功能
 */
@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("suppliers", supplierService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "supplier/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "supplier/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Supplier> supplierOpt = supplierService.findById(id);
        if (supplierOpt.isPresent()) {
            model.addAttribute("supplier", supplierOpt.get());
            return "supplier/form";
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Supplier supplier, RedirectAttributes redirectAttributes) {
        boolean isNew = supplier.getId() == null;
        supplierService.save(supplier);
        redirectAttributes.addFlashAttribute("message", isNew ? "供应商添加成功！" : "供应商修改成功！");
        return "redirect:/suppliers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        supplierService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "供应商删除成功！");
        return "redirect:/suppliers";
    }
}
