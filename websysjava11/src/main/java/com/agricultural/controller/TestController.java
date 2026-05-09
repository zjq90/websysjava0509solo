package com.agricultural.controller;

import com.agricultural.dto.SystemStatsDTO;
import com.agricultural.repository.*;
import com.agricultural.service.TestDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 测试功能控制器
 * 提供测试数据生成和系统测试功能
 */
@Controller
@RequestMapping("/test")
public class TestController {

    private final TestDataService testDataService;
    private final VarietyRepository varietyRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final FinanceRepository financeRepository;
    private final CostRecordRepository costRecordRepository;

    public TestController(TestDataService testDataService,
                          VarietyRepository varietyRepository,
                          CustomerRepository customerRepository,
                          OrderRepository orderRepository,
                          FinanceRepository financeRepository,
                          CostRecordRepository costRecordRepository) {
        this.testDataService = testDataService;
        this.varietyRepository = varietyRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.financeRepository = financeRepository;
        this.costRecordRepository = costRecordRepository;
    }

    @GetMapping
    public String index(Model model) {
        SystemStatsDTO stats = new SystemStatsDTO();
        stats.setVarietyCount(varietyRepository.count());
        stats.setCustomerCount(customerRepository.count());
        stats.setOrderCount(orderRepository.count());
        stats.setFinanceCount(financeRepository.count());
        stats.setCostCount(costRecordRepository.count());
        model.addAttribute("stats", stats);
        return "test/index";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam(defaultValue = "10") int salesCount,
                           @RequestParam(defaultValue = "5") int purchaseCount,
                           @RequestParam(defaultValue = "20") int costCount,
                           RedirectAttributes attributes) {
        try {
            int sales = testDataService.generateSalesOrders(salesCount);
            int purchase = testDataService.generatePurchaseOrders(purchaseCount);
            int cost = testDataService.generateCostRecords(costCount);
            attributes.addFlashAttribute("success",
                "成功生成：销售订单 " + sales + " 条，采购订单 " + purchase + " 条，成本记录 " + cost + " 条");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "生成失败：" + e.getMessage());
        }
        return "redirect:/test";
    }

    @PostMapping("/generate-sales")
    public String generateSales(@RequestParam(defaultValue = "10") int count,
                                 RedirectAttributes attributes) {
        try {
            int generated = testDataService.generateSalesOrders(count);
            attributes.addFlashAttribute("success", "成功生成 " + generated + " 条销售订单");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "生成失败：" + e.getMessage());
        }
        return "redirect:/test";
    }

    @PostMapping("/generate-purchase")
    public String generatePurchase(@RequestParam(defaultValue = "10") int count,
                                    RedirectAttributes attributes) {
        try {
            int generated = testDataService.generatePurchaseOrders(count);
            attributes.addFlashAttribute("success", "成功生成 " + generated + " 条采购订单");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "生成失败：" + e.getMessage());
        }
        return "redirect:/test";
    }

    @PostMapping("/generate-cost")
    public String generateCost(@RequestParam(defaultValue = "10") int count,
                                RedirectAttributes attributes) {
        try {
            int generated = testDataService.generateCostRecords(count);
            attributes.addFlashAttribute("success", "成功生成 " + generated + " 条成本记录");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "生成失败：" + e.getMessage());
        }
        return "redirect:/test";
    }

    @PostMapping("/generate-all")
    public String generateAll(@RequestParam(defaultValue = "20") int count,
                               RedirectAttributes attributes) {
        try {
            int sales = testDataService.generateSalesOrders(count);
            int purchase = testDataService.generatePurchaseOrders(count / 2);
            int cost = testDataService.generateCostRecords(count);
            attributes.addFlashAttribute("success",
                "成功生成：销售订单 " + sales + " 条，采购订单 " + purchase + " 条，成本记录 " + cost + " 条");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "生成失败：" + e.getMessage());
        }
        return "redirect:/test";
    }

    @PostMapping("/clear-all")
    public String clearAll(RedirectAttributes attributes) {
        try {
            testDataService.clearAllData();
            attributes.addFlashAttribute("success", "所有数据已清空");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "清空失败：" + e.getMessage());
        }
        return "redirect:/test";
    }
}
