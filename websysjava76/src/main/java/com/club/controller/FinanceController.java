package com.club.controller;

import com.club.common.Result;
import com.club.dto.PageQuery;
import com.club.entity.FinanceRecord;
import com.club.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 财务管理控制器
 * 提供财务记录CRUD、异常标记、调查等API接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/finance")
@Tag(name = "财务管理", description = "经费监管相关接口")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询财务记录列表")
    public Result<Page<FinanceRecord>> getFinanceRecordList(PageQuery query) {
        return Result.success(financeService.getFinanceRecordList(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询财务记录详情")
    public Result<FinanceRecord> getFinanceRecordById(@PathVariable Long id) {
        return Result.success(financeService.getFinanceRecordById(id));
    }

    @GetMapping("/club/{clubId}")
    @Operation(summary = "根据社团ID查询财务记录")
    public Result<List<FinanceRecord>> getFinanceRecordsByClubId(@PathVariable Long clubId) {
        return Result.success(financeService.getFinanceRecordsByClubId(clubId));
    }

    @PostMapping
    @Operation(summary = "新增财务记录")
    public Result<FinanceRecord> createFinanceRecord(@RequestBody FinanceRecord record) {
        return Result.success(financeService.createFinanceRecord(record));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新财务记录")
    public Result<FinanceRecord> updateFinanceRecord(@PathVariable Long id, @RequestBody FinanceRecord record) {
        try {
            return Result.success(financeService.updateFinanceRecord(id, record));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除财务记录")
    public Result<Void> deleteFinanceRecord(@PathVariable Long id) {
        financeService.deleteFinanceRecord(id);
        return Result.success();
    }

    @PostMapping("/{id}/investigate")
    @Operation(summary = "标记异常记录进行调查")
    public Result<FinanceRecord> markForInvestigation(@PathVariable Long id) {
        try {
            return Result.success(financeService.markForInvestigation(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/complete-investigation")
    @Operation(summary = "完成调查")
    public Result<FinanceRecord> completeInvestigation(
            @PathVariable Long id,
            @Parameter(description = "调查结果") @RequestParam String result) {
        try {
            return Result.success(financeService.completeInvestigation(id, result));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取财务统计数据")
    public Result<Map<String, Object>> getFinanceStatistics() {
        return Result.success(financeService.getFinanceStatistics());
    }

    @GetMapping("/abnormal")
    @Operation(summary = "获取异常财务记录")
    public Result<List<FinanceRecord>> getAbnormalRecords() {
        return Result.success(financeService.getAbnormalRecords());
    }

    @GetMapping("/pending-investigation")
    @Operation(summary = "获取待调查记录")
    public Result<List<FinanceRecord>> getPendingInvestigationRecords() {
        return Result.success(financeService.getPendingInvestigationRecords());
    }
}
