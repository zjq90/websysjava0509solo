package com.platform.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.management.common.Result;
import com.platform.management.dto.InvoiceAuditDTO;
import com.platform.management.dto.InvoiceIssueDTO;
import com.platform.management.entity.InvoiceApplication;
import com.platform.management.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发票管理Controller
 * 
 * @author platform
 * @version 1.0.0
 */
@RestController
@RequestMapping("/invoices")
@Tag(name = "发票管理", description = "发票相关接口")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/page")
    @Operation(summary = "分页查询发票申请列表")
    public Result<Page<InvoiceApplication>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "订单号") @RequestParam(required = false) String orderNo,
            @Parameter(description = "用户名称") @RequestParam(required = false) String userName,
            @Parameter(description = "发票类型") @RequestParam(required = false) String invoiceType,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        Page<InvoiceApplication> result = invoiceService.getPage(pageNum, pageSize, orderNo, userName, invoiceType, status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取发票申请详情")
    public Result<InvoiceApplication> getDetail(@PathVariable Long id) {
        InvoiceApplication detail = invoiceService.getDetail(id);
        return Result.success(detail);
    }

    @PostMapping
    @Operation(summary = "创建发票申请")
    public Result<Boolean> create(@RequestBody InvoiceApplication invoice) {
        boolean result = invoiceService.createInvoice(invoice);
        return Result.success(result);
    }

    @PostMapping("/audit")
    @Operation(summary = "审核发票申请")
    public Result<Boolean> audit(@RequestBody @Validated InvoiceAuditDTO dto) {
        boolean result = invoiceService.auditInvoice(dto);
        return Result.success(result ? "审核成功" : "审核失败", result);
    }

    @PostMapping("/issue")
    @Operation(summary = "开具发票")
    public Result<Boolean> issue(@RequestBody @Validated InvoiceIssueDTO dto) {
        boolean result = invoiceService.issueInvoice(dto);
        return Result.success(result ? "开票成功" : "开票失败", result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除发票申请")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = invoiceService.deleteInvoice(id);
        return Result.success(result);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除发票申请")
    public Result<Boolean> batchDelete(@RequestBody List<Long> ids) {
        boolean result = invoiceService.batchDelete(ids);
        return Result.success(result);
    }
}
