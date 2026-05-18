package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.Transaction;
import com.heritage.enums.AuditStatus;
import com.heritage.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
@Tag(name = "交易监管", description = "交易监管相关接口")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @Operation(summary = "获取所有交易")
    public Result<List<Transaction>> findAll() {
        return Result.success(transactionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取交易")
    public Result<Transaction> findById(@PathVariable Long id) {
        return transactionService.findById(id)
                .map(Result::success)
                .orElse(Result.error("交易不存在"));
    }

    @PostMapping
    @Operation(summary = "创建交易")
    public Result<Transaction> create(@RequestBody Transaction transaction) {
        return Result.success(transactionService.createTransaction(transaction));
    }

    @GetMapping("/abnormal")
    @Operation(summary = "获取异常交易列表")
    public Result<List<Transaction>> getAbnormalTransactions() {
        return Result.success(transactionService.findAbnormalTransactions());
    }

    @GetMapping("/frozen")
    @Operation(summary = "获取冻结交易列表")
    public Result<List<Transaction>> getFrozenTransactions() {
        return Result.success(transactionService.findFrozenTransactions());
    }

    @PostMapping("/{id}/request-freeze")
    @Operation(summary = "申请冻结交易资金")
    public Result<Transaction> requestFreeze(@PathVariable Long id, @RequestParam String reason) {
        return Result.success(transactionService.requestFreeze(id, reason));
    }

    @PostMapping("/{id}/audit-freeze")
    @Operation(summary = "审核冻结申请")
    public Result<Transaction> auditFreeze(@PathVariable Long id,
                                            @RequestParam AuditStatus status,
                                            @RequestParam(required = false) String remark,
                                            @RequestParam(required = false) Long auditorId) {
        return Result.success(transactionService.auditFreeze(id, status, remark, auditorId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户的交易列表")
    public Result<List<Transaction>> getByUserId(@PathVariable Long userId) {
        return Result.success(transactionService.findByUserId(userId));
    }

    @GetMapping("/recent/{minutes}")
    @Operation(summary = "获取最近交易")
    public Result<List<Transaction>> getRecentTransactions(@PathVariable int minutes) {
        return Result.success(transactionService.findRecentTransactions(minutes));
    }
}
