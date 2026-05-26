package com.accounting.system.controller;

import com.accounting.system.common.Result;
import com.accounting.system.dto.TransactionDTO;
import com.accounting.system.dto.TransactionQueryDTO;
import com.accounting.system.entity.Transaction;
import com.accounting.system.service.TransactionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 交易记录Controller
 * 提供收支录入、查询、编辑、删除API
 * 支持语音输入、拍照识别小票（OCR）、模板快捷录入等快速记录功能
 */
@Api(tags = "交易记录管理")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @ApiOperation("分页查询交易记录")
    @GetMapping("/page")
    public Result<IPage<Transaction>> pageList(
            @ApiParam(value = "当前页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页条数", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "交易类型：INCOME-收入，EXPENSE-支出")
            @RequestParam(required = false) String transactionType,
            @ApiParam(value = "账户ID")
            @RequestParam(required = false) Long accountId,
            @ApiParam(value = "分类ID")
            @RequestParam(required = false) Long categoryId,
            @ApiParam(value = "关键字（搜索描述/商家）")
            @RequestParam(required = false) String keyword) {

        TransactionQueryDTO query = new TransactionQueryDTO();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        query.setTransactionType(transactionType);
        query.setAccountId(accountId);
        query.setCategoryId(categoryId);
        query.setKeyword(keyword);

        Page<Transaction> page = new Page<>(pageNum, pageSize);
        return Result.success(transactionService.pageList(page, query));
    }

    @ApiOperation("查询所有交易记录")
    @GetMapping
    public Result<List<Transaction>> listAll(
            @ApiParam(value = "交易类型：INCOME-收入，EXPENSE-支出")
            @RequestParam(required = false) String transactionType,
            @ApiParam(value = "账户ID")
            @RequestParam(required = false) Long accountId,
            @ApiParam(value = "分类ID")
            @RequestParam(required = false) Long categoryId) {

        TransactionQueryDTO query = new TransactionQueryDTO();
        query.setTransactionType(transactionType);
        query.setAccountId(accountId);
        query.setCategoryId(categoryId);

        return Result.success(transactionService.listAll(query));
    }

    @ApiOperation("根据ID获取交易记录详情")
    @GetMapping("/{id}")
    public Result<Transaction> getDetailById(
            @ApiParam(value = "交易记录ID", required = true)
            @PathVariable Long id) {
        return Result.success(transactionService.getDetailById(id));
    }

    @ApiOperation("新增交易记录（收支录入）")
    @PostMapping
    public Result<Transaction> addTransaction(
            @ApiParam(value = "交易记录信息", required = true)
            @Valid @RequestBody TransactionDTO dto) {
        return Result.success("新增交易记录成功", transactionService.addTransaction(dto));
    }

    @ApiOperation("更新交易记录")
    @PutMapping
    public Result<Transaction> updateTransaction(
            @ApiParam(value = "交易记录信息", required = true)
            @Valid @RequestBody TransactionDTO dto) {
        return Result.success("更新交易记录成功", transactionService.updateTransaction(dto));
    }

    @ApiOperation("删除交易记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteTransaction(
            @ApiParam(value = "交易记录ID", required = true)
            @PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return Result.success("删除交易记录成功", null);
    }

    @ApiOperation("应用分类规则自动匹配分类")
    @PostMapping("/apply-rule")
    public Result<Long> applyCategoryRule(
            @ApiParam(value = "交易记录信息", required = true)
            @RequestBody TransactionDTO dto) {
        Long categoryId = transactionService.applyCategoryRule(dto);
        if (categoryId != null) {
            return Result.success("匹配成功", categoryId);
        }
        return Result.fail("未匹配到合适的分类规则");
    }
}
