package com.personal.accounting.controller;

import com.personal.accounting.entity.Account;
import com.personal.accounting.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户控制器
 * 提供账户管理API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "账户管理", description = "账户的增删改查API")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "获取所有账户", description = "获取所有启用的账户列表")
    public ResponseEntity<List<Account>> findAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取账户", description = "根据账户ID获取详细信息")
    public ResponseEntity<Account> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @GetMapping("/total-assets")
    @Operation(summary = "获取总资产", description = "计算所有账户的总资产")
    public ResponseEntity<BigDecimal> getTotalAssets() {
        return ResponseEntity.ok(accountService.getTotalAssets());
    }

    @PostMapping
    @Operation(summary = "创建账户", description = "创建新的账户")
    public ResponseEntity<Account> create(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.create(account), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新账户", description = "根据ID更新账户信息")
    public ResponseEntity<Account> update(
            @PathVariable Long id,
            @RequestBody Account account) {
        return ResponseEntity.ok(accountService.update(id, account));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除账户", description = "根据ID删除账户（逻辑删除）")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/adjust-balance")
    @Operation(summary = "调整账户余额", description = "直接调整账户余额")
    public ResponseEntity<Account> adjustBalance(
            @PathVariable Long id,
            @RequestParam BigDecimal newBalance) {
        return ResponseEntity.ok(accountService.adjustBalance(id, newBalance));
    }
}
