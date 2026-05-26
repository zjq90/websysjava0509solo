package com.accounting.controller;

import com.accounting.dto.ApiResponse;
import com.accounting.entity.Account;
import com.accounting.service.AccountService;
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
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "账户管理", description = "账户的增删改查接口")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "获取全部账户", description = "获取所有账户列表，按排序号排列")
    public ApiResponse<List<Account>> getAllAccounts() {
        log.info("API: 获取全部账户");
        return ApiResponse.success(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取账户", description = "根据账户ID获取账户详情")
    public ApiResponse<Account> getAccountById(
            @Parameter(description = "账户ID", required = true)
            @PathVariable Long id) {
        log.info("API: 根据ID获取账户: {}", id);
        return ApiResponse.success(accountService.getAccountById(id));
    }

    @GetMapping("/total-balance")
    @Operation(summary = "获取总资产", description = "计算所有账户的总余额")
    public ApiResponse<BigDecimal> getTotalBalance() {
        log.info("API: 获取总资产");
        return ApiResponse.success(accountService.getTotalBalance());
    }

    @PostMapping
    @Operation(summary = "创建账户", description = "创建新的账户")
    public ApiResponse<Account> createAccount(
            @RequestBody Account account) {
        log.info("API: 创建账户: {}", account.getName());
        return ApiResponse.success("账户创建成功", accountService.createAccount(account));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新账户", description = "更新账户信息")
    public ApiResponse<Account> updateAccount(
            @Parameter(description = "账户ID", required = true)
            @PathVariable Long id,
            @RequestBody Account account) {
        log.info("API: 更新账户: {}", id);
        return ApiResponse.success("账户更新成功", accountService.updateAccount(id, account));
    }

    @PatchMapping("/{id}/balance")
    @Operation(summary = "更新账户余额", description = "调整账户余额，正数增加，负数减少")
    public ApiResponse<Account> updateBalance(
            @Parameter(description = "账户ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "变动金额", required = true)
            @RequestParam BigDecimal amount) {
        log.info("API: 更新账户余额: id={}, amount={}", id, amount);
        return ApiResponse.success("余额更新成功", accountService.updateBalance(id, amount));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除账户", description = "删除指定账户")
    public ApiResponse<Void> deleteAccount(
            @Parameter(description = "账户ID", required = true)
            @PathVariable Long id) {
        log.info("API: 删除账户: {}", id);
        accountService.deleteAccount(id);
        return ApiResponse.success("账户删除成功", null);
    }
}
