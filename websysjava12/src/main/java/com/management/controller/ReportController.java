package com.management.controller;

import com.management.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

/**
 * 报表控制器
 * 提供销售报表、库存周转率、毛利率分析、市场需求预测等功能
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public String index() {
        return "report/index";
    }

    @GetMapping("/sales")
    public String salesReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        Map<String, Object> report = reportService.generateSalesReport(startDate, endDate);
        model.addAllAttributes(report);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "report/sales";
    }

    @GetMapping("/inventory-turnover")
    public String inventoryTurnover(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        Map<String, Object> report = reportService.calculateInventoryTurnover(startDate, endDate);
        model.addAllAttributes(report);

        return "report/inventory-turnover";
    }

    @GetMapping("/gross-margin")
    public String grossMargin(Model model) {
        Map<String, Object> report = reportService.generateGrossMarginAnalysis();
        model.addAllAttributes(report);
        return "report/gross-margin";
    }

    @GetMapping("/demand-forecast")
    public String demandForecast(
            @RequestParam(defaultValue = "3") int months,
            Model model) {

        Map<String, Object> forecast = reportService.generateDemandForecast(months);
        model.addAllAttributes(forecast);
        model.addAttribute("months", months);

        return "report/demand-forecast";
    }
}
