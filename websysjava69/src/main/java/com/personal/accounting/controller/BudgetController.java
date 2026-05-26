package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.entity.Budget;
import com.personal.accounting.entity.BudgetExpense;
import com.personal.accounting.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@Tag(name = "预算管理", description = "家庭预算管理相关接口")
@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @Operation(summary = "获取家庭月度预算列表")
    @GetMapping("/family/{familyId}")
    public Result<List<Budget>> getFamilyBudgets(
            @PathVariable Long familyId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return Result.success(budgetService.getFamilyBudgets(familyId, month));
    }

    @Operation(summary = "获取预算详情")
    @GetMapping("/{id}")
    public Result<Budget> getBudgetById(@PathVariable Long id) {
        return Result.success(budgetService.getBudgetById(id));
    }

    @Operation(summary = "创建预算（需管理员权限）")
    @PostMapping
    public Result<Budget> createBudget(@RequestBody Budget budget, @RequestParam Long operatorId) {
        return Result.success(budgetService.createBudget(budget, operatorId));
    }

    @Operation(summary = "更新预算（需管理员权限）")
    @PutMapping("/{id}")
    public Result<Budget> updateBudget(
            @PathVariable Long id,
            @RequestBody Budget budget,
            @RequestParam Long operatorId) {
        return Result.success(budgetService.updateBudget(id, budget, operatorId));
    }

    @Operation(summary = "删除预算（需管理员权限）")
    @DeleteMapping("/{id}")
    public Result<Void> deleteBudget(@PathVariable Long id, @RequestParam Long operatorId) {
        budgetService.deleteBudget(id, operatorId);
        return Result.success();
    }

    @Operation(summary = "获取预算支出记录")
    @GetMapping("/{budgetId}/expenses")
    public Result<List<BudgetExpense>> getBudgetExpenses(@PathVariable Long budgetId) {
        return Result.success(budgetService.getBudgetExpenses(budgetId));
    }

    @Operation(summary = "获取家庭所有支出记录")
    @GetMapping("/family/{familyId}/expenses")
    public Result<List<BudgetExpense>> getFamilyBudgetExpenses(@PathVariable Long familyId) {
        return Result.success(budgetService.getFamilyBudgetExpenses(familyId));
    }

    @Operation(summary = "获取用户个人支出记录")
    @GetMapping("/user/{userId}/expenses")
    public Result<List<BudgetExpense>> getUserBudgetExpenses(@PathVariable Long userId) {
        return Result.success(budgetService.getUserBudgetExpenses(userId));
    }

    @Operation(summary = "添加预算支出")
    @PostMapping("/expenses")
    public Result<BudgetExpense> addExpense(
            @RequestBody BudgetExpense expense,
            @RequestParam Long operatorId) {
        return Result.success(budgetService.addExpense(expense, operatorId));
    }

    @Operation(summary = "删除支出记录")
    @DeleteMapping("/expenses/{expenseId}")
    public Result<Void> deleteExpense(
            @PathVariable Long expenseId,
            @RequestParam Long operatorId) {
        budgetService.deleteExpense(expenseId, operatorId);
        return Result.success();
    }
}
