package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.entity.Investment;
import com.personal.accounting.enums.InvestmentType;
import com.personal.accounting.service.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "投资管理", description = "投资组合管理相关接口")
@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @Operation(summary = "获取用户投资列表")
    @GetMapping("/user/{userId}")
    public Result<List<Investment>> getUserInvestments(@PathVariable Long userId) {
        return Result.success(investmentService.getUserInvestments(userId));
    }

    @Operation(summary = "获取用户指定类型投资")
    @GetMapping("/user/{userId}/type/{type}")
    public Result<List<Investment>> getUserInvestmentsByType(@PathVariable Long userId, @PathVariable InvestmentType type) {
        return Result.success(investmentService.getUserInvestmentsByType(userId, type));
    }

    @Operation(summary = "获取投资详情")
    @GetMapping("/{id}")
    public Result<Investment> getInvestmentById(@PathVariable Long id) {
        return Result.success(investmentService.getInvestmentById(id));
    }

    @Operation(summary = "创建投资")
    @PostMapping
    public Result<Investment> createInvestment(@RequestBody Investment investment) {
        return Result.success(investmentService.createInvestment(investment));
    }

    @Operation(summary = "更新投资")
    @PutMapping("/{id}")
    public Result<Investment> updateInvestment(@PathVariable Long id, @RequestBody Investment investment) {
        return Result.success(investmentService.updateInvestment(id, investment));
    }

    @Operation(summary = "删除投资")
    @DeleteMapping("/{id}")
    public Result<Void> deleteInvestment(@PathVariable Long id) {
        investmentService.deleteInvestment(id);
        return Result.success();
    }

    @Operation(summary = "获取用户投资总市值")
    @GetMapping("/user/{userId}/total-value")
    public Result<BigDecimal> getTotalInvestmentValue(@PathVariable Long userId) {
        return Result.success(investmentService.getTotalInvestmentValue(userId));
    }

    @Operation(summary = "获取用户投资总盈亏")
    @GetMapping("/user/{userId}/total-profit")
    public Result<BigDecimal> getTotalInvestmentProfit(@PathVariable Long userId) {
        return Result.success(investmentService.getTotalInvestmentProfit(userId));
    }
}
