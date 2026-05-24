package com.bikeshare.controller;

import com.bikeshare.common.Result;
import com.bikeshare.dto.FinanceDTO;
import com.bikeshare.entity.CostRecord;
import com.bikeshare.entity.Invoice;
import com.bikeshare.entity.PaymentRecord;
import com.bikeshare.entity.Reconciliation;
import com.bikeshare.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 财务管理控制器
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/finance")
@RequiredArgsConstructor
@Tag(name = "财务管理", description = "财务管理相关接口")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/payments")
    @Operation(summary = "获取支付记录", description = "获取支付记录列表")
    public Result<List<PaymentRecord>> getPayments(
            @Parameter(description = "支付类型") @RequestParam(required = false) String paymentType) {
        return Result.success(financeService.getPaymentRecords(paymentType));
    }

    @GetMapping("/reconciliations")
    @Operation(summary = "获取对账记录", description = "获取对账记录列表")
    public Result<List<Reconciliation>> getReconciliations(
            @Parameter(description = "对账状态") @RequestParam(required = false) String status) {
        return Result.success(financeService.getReconciliations(status));
    }

    @PostMapping("/reconciliation")
    @Operation(summary = "执行对账", description = "执行对账操作")
    public Result<Reconciliation> doReconciliation(@RequestBody Map<String, Object> params) {
        LocalDate reconDate = LocalDate.parse(params.get("reconDate").toString());
        String reconType = params.getOrDefault("reconType", "DAILY_PAYMENT").toString();
        BigDecimal actualAmount = new BigDecimal(params.get("actualAmount").toString());
        String operator = params.getOrDefault("operator", "admin").toString();
        return Result.success(financeService.doReconciliation(reconDate, reconType, actualAmount, operator));
    }

    @GetMapping("/invoices")
    @Operation(summary = "获取发票列表", description = "获取发票列表")
    public Result<List<Invoice>> getInvoices(
            @Parameter(description = "发票状态") @RequestParam(required = false) String status) {
        return Result.success(financeService.getInvoices(status));
    }

    @PostMapping("/invoice")
    @Operation(summary = "开具发票", description = "开具单张发票")
    public Result<Invoice> createInvoice(@RequestBody Invoice invoice) {
        return Result.success(financeService.createInvoice(invoice));
    }

    @PostMapping("/invoices/batch")
    @Operation(summary = "批量开票", description = "企业用户批量开票")
    public Result<List<Invoice>> batchCreateInvoices(@RequestBody List<Invoice> invoices) {
        return Result.success(financeService.batchCreateInvoices(invoices));
    }

    @GetMapping("/cost-analysis")
    @Operation(summary = "成本分析", description = "获取成本分析数据")
    public Result<FinanceDTO.CostAnalysis> getCostAnalysis(
            @Parameter(description = "开始日期")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate) {
        return Result.success(financeService.getCostAnalysis(startDate));
    }

    @GetMapping("/costs")
    @Operation(summary = "获取成本记录", description = "获取成本记录列表")
    public Result<List<CostRecord>> getCosts(
            @Parameter(description = "成本类型") @RequestParam(required = false) String costType) {
        return Result.success(financeService.getCostRecords(costType));
    }
}
