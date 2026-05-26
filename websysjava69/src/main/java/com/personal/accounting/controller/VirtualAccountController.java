package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.entity.VirtualAccount;
import com.personal.accounting.entity.VirtualAccountTransaction;
import com.personal.accounting.service.VirtualAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "虚拟账户", description = "虚拟账户（专项储蓄）管理相关接口")
@RestController
@RequestMapping("/api/virtual-accounts")
@RequiredArgsConstructor
public class VirtualAccountController {

    private final VirtualAccountService virtualAccountService;

    @Operation(summary = "获取用户个人虚拟账户")
    @GetMapping("/user/{userId}")
    public Result<List<VirtualAccount>> getUserAccounts(@PathVariable Long userId) {
        return Result.success(virtualAccountService.getUserAccounts(userId));
    }

    @Operation(summary = "获取家庭虚拟账户")
    @GetMapping("/family/{familyId}")
    public Result<List<VirtualAccount>> getFamilyAccounts(@PathVariable Long familyId) {
        return Result.success(virtualAccountService.getFamilyAccounts(familyId));
    }

    @Operation(summary = "获取用户及家庭所有虚拟账户")
    @GetMapping("/user/{userId}/family/{familyId}")
    public Result<List<VirtualAccount>> getUserAndFamilyAccounts(
            @PathVariable Long userId,
            @PathVariable Long familyId) {
        return Result.success(virtualAccountService.getUserAndFamilyAccounts(userId, familyId));
    }

    @Operation(summary = "获取虚拟账户详情")
    @GetMapping("/{id}")
    public Result<VirtualAccount> getAccountById(@PathVariable Long id) {
        return Result.success(virtualAccountService.getAccountById(id));
    }

    @Operation(summary = "创建虚拟账户")
    @PostMapping
    public Result<VirtualAccount> createAccount(
            @RequestBody VirtualAccount account,
            @RequestParam Long operatorId) {
        return Result.success(virtualAccountService.createAccount(account, operatorId));
    }

    @Operation(summary = "更新虚拟账户")
    @PutMapping("/{id}")
    public Result<VirtualAccount> updateAccount(
            @PathVariable Long id,
            @RequestBody VirtualAccount account,
            @RequestParam Long operatorId) {
        return Result.success(virtualAccountService.updateAccount(id, account, operatorId));
    }

    @Operation(summary = "删除虚拟账户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAccount(
            @PathVariable Long id,
            @RequestParam Long operatorId) {
        virtualAccountService.deleteAccount(id, operatorId);
        return Result.success();
    }

    @Operation(summary = "获取账户交易记录")
    @GetMapping("/{accountId}/transactions")
    public Result<List<VirtualAccountTransaction>> getAccountTransactions(@PathVariable Long accountId) {
        return Result.success(virtualAccountService.getAccountTransactions(accountId));
    }

    @Operation(summary = "添加交易记录（存入/取出）")
    @PostMapping("/{accountId}/transactions")
    public Result<VirtualAccountTransaction> createTransaction(
            @PathVariable Long accountId,
            @RequestBody VirtualAccountTransaction transaction,
            @RequestParam Long operatorId) {
        return Result.success(virtualAccountService.createTransaction(accountId, transaction, operatorId));
    }

    @Operation(summary = "删除交易记录")
    @DeleteMapping("/transactions/{transactionId}")
    public Result<Void> deleteTransaction(
            @PathVariable Long transactionId,
            @RequestParam Long operatorId) {
        virtualAccountService.deleteTransaction(transactionId, operatorId);
        return Result.success();
    }
}
