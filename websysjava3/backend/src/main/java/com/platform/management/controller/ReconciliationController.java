package com.platform.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.management.common.Result;
import com.platform.management.dto.ReconciliationDTO;
import com.platform.management.entity.Reconciliation;
import com.platform.management.entity.ReconciliationDetail;
import com.platform.management.service.ReconciliationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 对账管理Controller
 * 
 * @author platform
 * @version 1.0.0
 */
@RestController
@RequestMapping("/reconciliations")
@Tag(name = "对账管理", description = "对账相关接口")
public class ReconciliationController {

    @Autowired
    private ReconciliationService reconciliationService;

    @GetMapping("/page")
    @Operation(summary = "分页查询对账记录列表")
    public Result<Page<Reconciliation>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "对账日期") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reconDate,
            @Parameter(description = "支付渠道") @RequestParam(required = false) String payChannel,
            @Parameter(description = "对账类型") @RequestParam(required = false) String reconType,
            @Parameter(description = "对账状态") @RequestParam(required = false) String status) {
        Page<Reconciliation> result = reconciliationService.getPage(pageNum, pageSize, reconDate, payChannel, reconType, status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取对账记录详情")
    public Result<Reconciliation> getDetail(@PathVariable Long id) {
        Reconciliation reconciliation = reconciliationService.getById(id);
        return Result.success(reconciliation);
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "获取对账差异明细")
    public Result<List<ReconciliationDetail>> getDetails(@PathVariable Long id) {
        List<ReconciliationDetail> details = reconciliationService.getReconciliationDetails(id);
        return Result.success(details);
    }

    @PostMapping("/execute")
    @Operation(summary = "执行对账")
    public Result<Reconciliation> execute(@RequestBody @Validated ReconciliationDTO dto) {
        Reconciliation result = reconciliationService.executeReconciliation(dto);
        return Result.success("对账完成", result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除对账记录")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = reconciliationService.deleteReconciliation(id);
        return Result.success(result);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除对账记录")
    public Result<Boolean> batchDelete(@RequestBody List<Long> ids) {
        boolean result = reconciliationService.batchDelete(ids);
        return Result.success(result);
    }
}
