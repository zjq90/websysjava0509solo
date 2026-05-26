package com.personal.accounting.controller;

import com.personal.accounting.dto.BudgetProgressDTO;
import com.personal.accounting.entity.Budget;
import com.personal.accounting.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

/**
 * 预算控制器
 * 提供预算设置和进度跟踪API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Tag(name = "预算管理", description = "预算设置与进度跟踪API")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("/{month}")
    @Operation(summary = "获取月度预算列表", description = "获取指定月份的所有预算")
    public ResponseEntity<List<Budget>> getBudgetsByMonth(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(budgetService.getBudgetsByMonth(month));
    }

    @GetMapping("/{month}/progress")
    @Operation(summary = "获取月度预算进度", description = "获取指定月份所有预算的执行进度")
    public ResponseEntity<List<BudgetProgressDTO>> getBudgetProgress(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(budgetService.getBudgetProgress(month));
    }

    @PostMapping
    @Operation(summary = "设置预算", description = "为指定分类和月份设置预算")
    public ResponseEntity<Budget> setBudget(
            @RequestParam Long categoryId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "false") Boolean rolloverRemaining) {
        return new ResponseEntity<>(
                budgetService.setBudget(categoryId, month, amount, rolloverRemaining),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除预算", description = "根据ID删除预算")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/copy-last-month")
    @Operation(summary = "复制上月预算", description = "将上月预算复制到本月")
    public ResponseEntity<Integer> copyLastMonthBudget(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth targetMonth) {
        return ResponseEntity.ok(budgetService.copyLastMonthBudget(targetMonth));
    }
}
