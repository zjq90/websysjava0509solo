package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.dto.RepaymentSimulationDTO;
import com.personal.accounting.entity.Debt;
import com.personal.accounting.entity.DebtPayment;
import com.personal.accounting.service.DebtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "债务管理", description = "债务管理相关接口")
@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;

    @Operation(summary = "获取用户债务列表")
    @GetMapping("/user/{userId}")
    public Result<List<Debt>> getUserDebts(@PathVariable Long userId) {
        return Result.success(debtService.getUserDebts(userId));
    }

    @Operation(summary = "获取用户未结清债务列表")
    @GetMapping("/user/{userId}/active")
    public Result<List<Debt>> getUserActiveDebts(@PathVariable Long userId) {
        return Result.success(debtService.getUserActiveDebts(userId));
    }

    @Operation(summary = "获取债务详情")
    @GetMapping("/{id}")
    public Result<Debt> getDebtById(@PathVariable Long id) {
        return Result.success(debtService.getDebtById(id));
    }

    @Operation(summary = "创建债务")
    @PostMapping
    public Result<Debt> createDebt(@RequestBody Debt debt) {
        return Result.success(debtService.createDebt(debt));
    }

    @Operation(summary = "更新债务")
    @PutMapping("/{id}")
    public Result<Debt> updateDebt(@PathVariable Long id, @RequestBody Debt debt) {
        return Result.success(debtService.updateDebt(id, debt));
    }

    @Operation(summary = "删除债务")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDebt(@PathVariable Long id) {
        debtService.deleteDebt(id);
        return Result.success();
    }

    @Operation(summary = "还款")
    @PostMapping("/{debtId}/payments")
    public Result<DebtPayment> makePayment(@PathVariable Long debtId, @RequestBody DebtPayment payment) {
        return Result.success(debtService.makePayment(debtId, payment));
    }

    @Operation(summary = "获取还款记录")
    @GetMapping("/{debtId}/payments")
    public Result<List<DebtPayment>> getDebtPayments(@PathVariable Long debtId) {
        return Result.success(debtService.getDebtPayments(debtId));
    }

    @Operation(summary = "还款方式模拟")
    @PostMapping("/simulate")
    public Result<RepaymentSimulationDTO> simulateRepayment(@Valid @RequestBody RepaymentSimulationDTO simulation) {
        return Result.success(debtService.simulateRepayment(simulation));
    }

    @Operation(summary = "获取即将到期的还款提醒")
    @GetMapping("/reminders")
    public Result<List<Debt>> getDebtsForReminder() {
        return Result.success(debtService.getDebtsForReminder());
    }
}
