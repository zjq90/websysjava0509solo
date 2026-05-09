package com.agricultural.controller;

import com.agricultural.dto.*;
import com.agricultural.service.ProfitAnalysisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * 利润分析控制器
 * 提供多维度利润分析
 */
@Controller
@RequestMapping("/profit")
public class ProfitController {

    private final ProfitAnalysisService profitAnalysisService;

    public ProfitController(ProfitAnalysisService profitAnalysisService) {
        this.profitAnalysisService = profitAnalysisService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("topVarieties", profitAnalysisService.getTopVarieties(5));
        model.addAttribute("topCustomers", profitAnalysisService.getTopCustomers(5));
        return "profit/index";
    }

    @GetMapping("/variety")
    public String byVariety(Model model) {
        List<ProfitByVarietyDTO> profits = profitAnalysisService.analyzeByVariety();
        model.addAttribute("profits", profits);
        
        BigDecimal totalSales = profits.stream()
            .map(ProfitByVarietyDTO::getSalesAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCost = profits.stream()
            .map(ProfitByVarietyDTO::getTotalCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalProfit = profits.stream()
            .map(ProfitByVarietyDTO::getProfit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMargin = totalSales.compareTo(BigDecimal.ZERO) > 0
            ? totalProfit.multiply(new BigDecimal("100")).divide(totalSales, 2, java.math.RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("totalProfit", totalProfit);
        model.addAttribute("totalMargin", totalMargin);
        return "profit/variety";
    }

    @GetMapping("/customer")
    public String byCustomer(Model model) {
        List<ProfitByCustomerDTO> profits = profitAnalysisService.analyzeByCustomer();
        model.addAttribute("profits", profits);
        
        int customerCount = profits.size();
        int activeCustomerCount = (int) profits.stream()
            .filter(p -> p.getSalesAmount().compareTo(BigDecimal.ZERO) > 0)
            .count();
        BigDecimal totalProfit = profits.stream()
            .map(ProfitByCustomerDTO::getProfit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avgProfit = customerCount > 0
            ? totalProfit.divide(new BigDecimal(customerCount), 2, java.math.RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        
        model.addAttribute("customerCount", customerCount);
        model.addAttribute("activeCustomerCount", activeCustomerCount);
        model.addAttribute("totalProfit", totalProfit);
        model.addAttribute("avgProfit", avgProfit);
        return "profit/customer";
    }

    @GetMapping("/region")
    public String byRegion(Model model) {
        List<ProfitByRegionDTO> profits = profitAnalysisService.analyzeByRegion();
        model.addAttribute("profits", profits);
        
        int regionCount = profits.size();
        BigDecimal totalSales = profits.stream()
            .map(ProfitByRegionDTO::getSalesAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalProfit = profits.stream()
            .map(ProfitByRegionDTO::getProfit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avgSales = regionCount > 0
            ? totalSales.divide(new BigDecimal(regionCount), 2, java.math.RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        
        model.addAttribute("regionCount", regionCount);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalProfit", totalProfit);
        model.addAttribute("avgSales", avgSales);
        return "profit/region";
    }
}
