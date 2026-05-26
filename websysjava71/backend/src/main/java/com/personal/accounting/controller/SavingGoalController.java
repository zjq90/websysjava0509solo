package com.personal.accounting.controller;

import com.personal.accounting.entity.SavingGoal;
import com.personal.accounting.service.SavingGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 储蓄目标控制器
 * 提供储蓄目标管理API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/saving-goals")
@RequiredArgsConstructor
@Tag(name = "储蓄目标管理", description = "储蓄目标的设置与进度跟踪API")
public class SavingGoalController {

    private final SavingGoalService savingGoalService;

    @GetMapping
    @Operation(summary = "获取所有储蓄目标", description = "获取所有储蓄目标列表")
    public ResponseEntity<List<SavingGoal>> findAll() {
        return ResponseEntity.ok(savingGoalService.findAll());
    }

    @GetMapping("/active")
    @Operation(summary = "获取进行中的储蓄目标", description = "获取所有未完成的储蓄目标")
    public ResponseEntity<List<SavingGoal>> findActiveGoals() {
        return ResponseEntity.ok(savingGoalService.findActiveGoals());
    }

    @GetMapping("/completed")
    @Operation(summary = "获取已完成的储蓄目标", description = "获取所有已完成的储蓄目标")
    public ResponseEntity<List<SavingGoal>> findCompletedGoals() {
        return ResponseEntity.ok(savingGoalService.findCompletedGoals());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取储蓄目标", description = "根据ID获取储蓄目标详细信息")
    public ResponseEntity<SavingGoal> findById(@PathVariable Long id) {
        return ResponseEntity.ok(savingGoalService.findById(id));
    }

    @PostMapping
    @Operation(summary = "创建储蓄目标", description = "创建新的储蓄目标")
    public ResponseEntity<SavingGoal> create(@RequestBody SavingGoal goal) {
        return new ResponseEntity<>(savingGoalService.create(goal), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新储蓄目标", description = "根据ID更新储蓄目标信息")
    public ResponseEntity<SavingGoal> update(
            @PathVariable Long id,
            @RequestBody SavingGoal goal) {
        return ResponseEntity.ok(savingGoalService.update(id, goal));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除储蓄目标", description = "根据ID删除储蓄目标")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        savingGoalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "向储蓄目标存款", description = "向指定储蓄目标存入金额")
    public ResponseEntity<SavingGoal> deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(savingGoalService.deposit(id, amount));
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "从储蓄目标取款", description = "从指定储蓄目标取出金额")
    public ResponseEntity<SavingGoal> withdraw(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(savingGoalService.withdraw(id, amount));
    }

    @GetMapping("/{id}/progress")
    @Operation(summary = "获取储蓄进度", description = "获取储蓄目标的完成百分比")
    public ResponseEntity<Double> getProgressPercentage(@PathVariable Long id) {
        return ResponseEntity.ok(savingGoalService.getProgressPercentage(id));
    }
}
