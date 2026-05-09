package com.management.controller;

import com.management.entity.Product;
import com.management.entity.Purchase;
import com.management.entity.Supplier;
import com.management.service.ProductService;
import com.management.service.PurchaseService;
import com.management.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 采购控制器
 * 提供采购单的增删改查功能
 */
@Controller
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String list(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        List<Purchase> purchases;
        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);
            purchases = purchaseService.findByDateRange(start, end);
        } else {
            purchases = purchaseService.findAll();
        }

        model.addAttribute("purchases", purchases);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "purchase/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("purchase", new Purchase());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("suppliers", supplierService.findAll());
        return "purchase/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Purchase> purchaseOpt = purchaseService.findById(id);
        if (purchaseOpt.isPresent()) {
            model.addAttribute("purchase", purchaseOpt.get());
            model.addAttribute("products", productService.findAll());
            model.addAttribute("suppliers", supplierService.findAll());
            return "purchase/form";
        }
        return "redirect:/purchases";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute Purchase purchase,
            @RequestParam Long productId,
            @RequestParam(required = false) Long supplierId,
            RedirectAttributes redirectAttributes) {

        Optional<Product> productOpt = productService.findById(productId);
        productOpt.ifPresent(purchase::setProduct);

        if (supplierId != null) {
            Optional<Supplier> supplierOpt = supplierService.findById(supplierId);
            supplierOpt.ifPresent(purchase::setSupplier);
        }

        boolean isNew = purchase.getId() == null;
        purchaseService.save(purchase);
        redirectAttributes.addFlashAttribute("message", isNew ? "采购单创建成功！" : "采购单修改成功！");
        return "redirect:/purchases";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        purchaseService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "采购单删除成功！");
        return "redirect:/purchases";
    }
}
