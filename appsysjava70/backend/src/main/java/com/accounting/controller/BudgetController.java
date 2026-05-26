package com.accounting.controller;

import com.accounting.dto.ApiResponse;
import com.accounting.entity.Budget;
import com.accounting.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
@Tag(name = "预算管理", description = "预算的增删改查接口")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    @Operation(summary = "获取全部预算", description = "获取所有预算列表")
    public ApiResponse<List<Budget>> getAllBudgets() {
        log.info("API: 获取全部预算");
        return ApiResponse.success(budgetService.getAllBudgets());
    }

    @GetMapping("/active")
    @Operation(summary = "获取当前生效的预算", description = "获取当前时间范围内生效的预算列表")
    public ApiResponse<List<Budget>> getActiveBudgets() {
        log.info("API: 获取当前生效的预算");
        return ApiResponse.success(budgetService.getActiveBudgets());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取预算", description = "根据预算ID获取预算详情")
    public ApiResponse<Budget> getBudgetById(
            @Parameter(description = "预算ID", required = true)
            @PathVariable Long id) {
        log.info("API: 根据ID获取预算: {}", id);
        return ApiResponse.success(budgetService.getBudgetById(id));
    }

    @GetMapping("/{id}/usage")
    @Operation(summary = "获取预算使用百分比", description = "计算指定预算的已使用百分比")
    public ApiResponse<BigDecimal> getBudgetUsage(
            @Parameter(description = "预算ID", required = true)
            @PathVariable Long id) {
        log.info("API: 获取预算使用百分比: {}", id);
        return ApiResponse.success(budgetService.getBudgetUsage(id));
    }

    @PostMapping
    @Operation(summary = "创建预算", description = "创建新的预算，可以是总预算或分类预算")
    public ApiResponse<Budget> createBudget(
            @RequestBody Budget budget) {
        log.info("API: 创建预算: {}", budget.getName());
        return ApiResponse.success("预算创建成功", budgetService.createBudget(budget));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新预算", description = "更新预算信息")
    public ApiResponse<Budget> updateBudget(
            @Parameter(description = "预算ID", required = true)
            @PathVariable Long id,
            @RequestBody Budget budget) {
        log.info("API: 更新预算: {}", id);
        return ApiResponse.success("预算更新成功", budgetService.updateBudget(id, budget));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除预算", description = "删除指定预算")
    public ApiResponse<Void> deleteBudget(
            @Parameter(description = "预算ID", required = true)
            @PathVariable Long id) {
        log.info("API: 删除预算: {}", id);
        budgetService.deleteBudget(id);
        return ApiResponse.success("预算删除成功", null);
    }
}
