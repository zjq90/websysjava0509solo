package com.accounting.system.controller;

import com.accounting.system.common.Result;
import com.accounting.system.dto.AccountDTO;
import com.accounting.system.entity.Account;
import com.accounting.system.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 账户管理Controller
 * 提供多账户管理API：现金、银行卡、信用卡、支付宝、微信钱包等
 */
@Api(tags = "账户管理")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @ApiOperation("查询所有账户列表")
    @GetMapping
    public Result<List<Account>> listAll() {
        return Result.success(accountService.listAll());
    }

    @ApiOperation("根据ID获取账户详情")
    @GetMapping("/{id}")
    public Result<Account> getDetailById(
            @ApiParam(value = "账户ID", required = true)
            @PathVariable Long id) {
        return Result.success(accountService.getDetailById(id));
    }

    @ApiOperation("新增账户")
    @PostMapping
    public Result<Account> addAccount(
            @ApiParam(value = "账户信息", required = true)
            @Valid @RequestBody AccountDTO dto) {
        return Result.success("新增账户成功", accountService.addAccount(dto));
    }

    @ApiOperation("更新账户")
    @PutMapping
    public Result<Account> updateAccount(
            @ApiParam(value = "账户信息", required = true)
            @Valid @RequestBody AccountDTO dto) {
        return Result.success("更新账户成功", accountService.updateAccount(dto));
    }

    @ApiOperation("删除账户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAccount(
            @ApiParam(value = "账户ID", required = true)
            @PathVariable Long id) {
        accountService.deleteAccount(id);
        return Result.success("删除账户成功", null);
    }
}
