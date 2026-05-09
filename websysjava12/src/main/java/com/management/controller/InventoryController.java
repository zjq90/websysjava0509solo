package com.management.controller;

import com.management.entity.Inventory;
import com.management.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 库存控制器
 * 提供库存查询和管理功能
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inventoryList", inventoryService.findAll());
        model.addAttribute("totalValue", inventoryService.getTotalInventoryValue());
        return "inventory/list";
    }

    @GetMapping("/low-stock")
    public String lowStock(Model model) {
        model.addAttribute("inventoryList", inventoryService.findLowStockItems());
        model.addAttribute("totalValue", inventoryService.getTotalInventoryValue());
        return "inventory/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Inventory> inventoryOpt = inventoryService.findById(id);
        if (inventoryOpt.isPresent()) {
            model.addAttribute("inventory", inventoryOpt.get());
            return "inventory/form";
        }
        return "redirect:/inventory";
    }

    @PostMapping("/save")
    public String save(Inventory inventory, RedirectAttributes redirectAttributes) {
        if (inventory.getAvgCostPrice() != null && inventory.getQuantity() != null) {
            inventory.setTotalValue(inventory.getAvgCostPrice().multiply(BigDecimal.valueOf(inventory.getQuantity())));
        }
        inventoryService.save(inventory);
        redirectAttributes.addFlashAttribute("message", "库存更新成功！");
        return "redirect:/inventory";
    }
}
