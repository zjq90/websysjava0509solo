package com.management.controller;

import com.management.entity.Inventory;
import com.management.entity.Product;
import com.management.service.InventoryService;
import com.management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 产品控制器
 * 提供产品的增删改查功能
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("products", productService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "product/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "product/form";
        }
        return "redirect:/products";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        boolean isNew = product.getId() == null;
        Product savedProduct = productService.save(product);

        if (isNew) {
            Inventory inventory = new Inventory();
            inventory.setProduct(savedProduct);
            inventory.setQuantity(0);
            inventory.setAvgCostPrice(product.getCostPrice());
            inventory.setTotalValue(BigDecimal.ZERO);
            inventory.setMinStock(10);
            inventoryService.save(inventory);
        }

        redirectAttributes.addFlashAttribute("message", isNew ? "产品添加成功！" : "产品修改成功！");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Inventory> inventoryOpt = inventoryService.findByProductId(id);
        if (inventoryOpt.isPresent()) {
            inventoryService.deleteById(inventoryOpt.get().getId());
        }
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "产品删除成功！");
        return "redirect:/products";
    }
}
