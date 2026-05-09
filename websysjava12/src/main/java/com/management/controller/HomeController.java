package com.management.controller;

import com.management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 首页控制器
 * 展示系统概览和数据统计
 */
@Controller
public class HomeController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        LocalDate firstDayOfYear = today.withDayOfYear(1);

        BigDecimal todaySales = saleService.getTotalSalesAmount(
            LocalDateTime.of(today, LocalTime.MIN),
            LocalDateTime.of(today, LocalTime.MAX)
        );

        BigDecimal monthSales = saleService.getTotalSalesAmount(
            LocalDateTime.of(firstDayOfMonth, LocalTime.MIN),
            LocalDateTime.of(lastDayOfMonth, LocalTime.MAX)
        );

        BigDecimal yearSales = saleService.getTotalSalesAmount(
            LocalDateTime.of(firstDayOfYear, LocalTime.MIN),
            LocalDateTime.of(today, LocalTime.MAX)
        );

        BigDecimal todayProfit = saleService.getTotalProfit(
            LocalDateTime.of(today, LocalTime.MIN),
            LocalDateTime.of(today, LocalTime.MAX)
        );

        BigDecimal monthProfit = saleService.getTotalProfit(
            LocalDateTime.of(firstDayOfMonth, LocalTime.MIN),
            LocalDateTime.of(lastDayOfMonth, LocalTime.MAX)
        );

        BigDecimal totalInventoryValue = inventoryService.getTotalInventoryValue();
        long productCount = productService.findAll().size();
        int lowStockCount = inventoryService.findLowStockItems().size();

        model.addAttribute("todaySales", todaySales);
        model.addAttribute("monthSales", monthSales);
        model.addAttribute("yearSales", yearSales);
        model.addAttribute("todayProfit", todayProfit);
        model.addAttribute("monthProfit", monthProfit);
        model.addAttribute("totalInventoryValue", totalInventoryValue);
        model.addAttribute("productCount", productCount);
        model.addAttribute("lowStockCount", lowStockCount);

        return "index";
    }
}
