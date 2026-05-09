package com.production.controller;

import com.production.entity.Product;
import com.production.entity.ProductionPlan;
import com.production.service.ProductService;
import com.production.service.ProductionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 生产计划控制器
 */
@Controller
@RequestMapping("/plans")
public class ProductionPlanController {

    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private ProductService productService;

    /**
     * 计划列表页面
     */
    @GetMapping
    public String list(Model model, 
                       @RequestParam(required = false) String status,
                       @RequestParam(defaultValue = "0") int page, 
                       @RequestParam(defaultValue = "10") int size) {
        Page<ProductionPlan> plans = productionPlanService.findByStatusPage(status, page, size);
        model.addAttribute("plans", plans.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", plans.getTotalPages());
        model.addAttribute("totalElements", plans.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("currentStatus", status);
        return "plan/list";
    }

    /**
     * 可视化排产页面
     */
    @GetMapping("/schedule")
    public String schedule(Model model) {
        List<ProductionPlan> plans = productionPlanService.findAllActive();
        List<Product> products = productService.findAllActive();
        model.addAttribute("plans", plans);
        model.addAttribute("products", products);
        return "plan/schedule";
    }

    /**
     * 新增计划页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("plan", new ProductionPlan());
        model.addAttribute("products", productService.findAllActive());
        return "plan/form";
    }

    /**
     * 编辑计划页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProductionPlan> planOpt = productionPlanService.findById(id);
        if (planOpt.isPresent()) {
            model.addAttribute("plan", planOpt.get());
            model.addAttribute("products", productService.findAllActive());
            return "plan/form";
        }
        redirectAttributes.addFlashAttribute("error", "计划不存在");
        return "redirect:/plans";
    }

    /**
     * 保存计划
     */
    @PostMapping("/save")
    public String save(@ModelAttribute ProductionPlan plan, @RequestParam Long productId, RedirectAttributes redirectAttributes) {
        try {
            Optional<Product> productOpt = productService.findById(productId);
            if (productOpt.isPresent()) {
                plan.setProduct(productOpt.get());
            }
            productionPlanService.save(plan);
            redirectAttributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/plans";
    }

    /**
     * 删除计划
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productionPlanService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/plans";
    }

    /**
     * 更新计划状态
     */
    @GetMapping("/status/{id}/{status}")
    public String updateStatus(@PathVariable Long id, @PathVariable String status, RedirectAttributes redirectAttributes) {
        try {
            productionPlanService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "状态更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "状态更新失败：" + e.getMessage());
        }
        return "redirect:/plans";
    }
}