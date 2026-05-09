package com.management.controller;

import com.management.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * 测试数据控制器
 * 提供测试数据生成和清除功能
 */
@Controller
@RequestMapping("/test-data")
public class TestDataController {

    @Autowired
    private TestDataService testDataService;

    @GetMapping
    public String index() {
        return "test-data/index";
    }

    @PostMapping("/generate-all")
    public String generateAll(RedirectAttributes redirectAttributes) {
        Map<String, Object> result = testDataService.generateAllTestData();
        StringBuilder message = new StringBuilder("测试数据生成成功！\n");
        message.append("供应商: ").append(result.get("suppliers")).append(" 个\n");
        message.append("客户: ").append(result.get("customers")).append(" 个\n");
        message.append("产品: ").append(result.get("products")).append(" 个\n");
        message.append("采购单: ").append(result.get("purchases")).append(" 个\n");
        message.append("销售单: ").append(result.get("sales")).append(" 个");

        redirectAttributes.addFlashAttribute("message", message.toString());
        return "redirect:/test-data";
    }

    @PostMapping("/generate-products")
    public String generateProducts(RedirectAttributes redirectAttributes) {
        int count = testDataService.generateProducts();
        redirectAttributes.addFlashAttribute("message", "成功生成 " + count + " 个产品测试数据");
        return "redirect:/test-data";
    }

    @PostMapping("/generate-suppliers")
    public String generateSuppliers(RedirectAttributes redirectAttributes) {
        int count = testDataService.generateSuppliers();
        redirectAttributes.addFlashAttribute("message", "成功生成 " + count + " 个供应商测试数据");
        return "redirect:/test-data";
    }

    @PostMapping("/generate-customers")
    public String generateCustomers(RedirectAttributes redirectAttributes) {
        int count = testDataService.generateCustomers();
        redirectAttributes.addFlashAttribute("message", "成功生成 " + count + " 个客户测试数据");
        return "redirect:/test-data";
    }

    @PostMapping("/generate-purchases")
    public String generatePurchases(RedirectAttributes redirectAttributes) {
        int count = testDataService.generatePurchases();
        redirectAttributes.addFlashAttribute("message", "成功生成 " + count + " 个采购单测试数据");
        return "redirect:/test-data";
    }

    @PostMapping("/generate-sales")
    public String generateSales(RedirectAttributes redirectAttributes) {
        int count = testDataService.generateSales();
        redirectAttributes.addFlashAttribute("message", "成功生成 " + count + " 个销售单测试数据");
        return "redirect:/test-data";
    }

    @PostMapping("/clear-all")
    public String clearAll(RedirectAttributes redirectAttributes) {
        testDataService.clearAllTestData();
        redirectAttributes.addFlashAttribute("message", "所有测试数据已清除");
        return "redirect:/test-data";
    }
}
