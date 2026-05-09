package com.production.controller;

import com.production.entity.Product;
import com.production.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 产品管理控制器
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 产品列表页面
     */
    @GetMapping
    public String list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = productService.findAll(page, size);
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("pageSize", size);
        return "product/list";
    }

    /**
     * 新增产品页面
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/form";
    }

    /**
     * 编辑产品页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "product/form";
        }
        redirectAttributes.addFlashAttribute("error", "产品不存在");
        return "redirect:/products";
    }

    /**
     * 保存产品
     */
    @PostMapping("/save")
    public String save(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.save(product);
            redirectAttributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/products";
    }

    /**
     * 删除产品
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/products";
    }

    /**
     * 激活/停用产品
     */
    @GetMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if ("ACTIVE".equals(product.getStatus())) {
                productService.deactivate(id);
                redirectAttributes.addFlashAttribute("success", "已停用");
            } else {
                productService.activate(id);
                redirectAttributes.addFlashAttribute("success", "已激活");
            }
        }
        return "redirect:/products";
    }
}