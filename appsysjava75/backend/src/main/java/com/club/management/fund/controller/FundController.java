package com.club.management.fund.controller;

import com.club.management.common.result.PageResult;
import com.club.management.common.result.Result;
import com.club.management.fund.entity.FundRecord;
import com.club.management.fund.entity.FundStatistics;
import com.club.management.fund.entity.Reimbursement;
import com.club.management.fund.service.FundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 经费管理Controller
 *
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "经费管理", description = "经费记录、报销审批、统计报表等接口")
@RestController
@RequestMapping("/fund")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @Operation(summary = "获取经费记录列表")
    @GetMapping("/record/list")
    public Result<PageResult<FundRecord>> getFundRecordList(
            @Parameter(description = "社团ID") @RequestParam Long clubId,
            @Parameter(description = "类型 0-支出 1-收入") @RequestParam(required = false) Integer type,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(fundService.getFundRecordList(clubId, type, pageNum, pageSize));
    }

    @Operation(summary = "添加经费记录")
    @PostMapping("/record/add")
    public Result<FundRecord> addFundRecord(@RequestBody FundRecord record) {
        return Result.success(fundService.addFundRecord(record));
    }

    @Operation(summary = "更新经费记录")
    @PutMapping("/record/update")
    public Result<FundRecord> updateFundRecord(@RequestBody FundRecord record) {
        return Result.success(fundService.updateFundRecord(record));
    }

    @Operation(summary = "删除经费记录")
    @DeleteMapping("/record/{id}")
    public Result<Void> deleteFundRecord(@Parameter(description = "记录ID") @PathVariable Long id) {
        fundService.deleteFundRecord(id);
        return Result.success();
    }

    @Operation(summary = "获取经费记录详情")
    @GetMapping("/record/{id}")
    public Result<FundRecord> getFundRecordDetail(@Parameter(description = "记录ID") @PathVariable Long id) {
        return Result.success(fundService.getFundRecordDetail(id));
    }

    @Operation(summary = "获取经费汇总信息")
    @GetMapping("/summary")
    public Result<Map<String, Object>> getFundSummary(
            @Parameter(description = "社团ID") @RequestParam Long clubId) {
        return Result.success(fundService.getFundSummary(clubId));
    }

    @Operation(summary = "获取报销申请列表")
    @GetMapping("/reimbursement/list")
    public Result<PageResult<Reimbursement>> getReimbursementList(
            @Parameter(description = "社团ID") @RequestParam Long clubId,
            @Parameter(description = "状态 0-待审核 1-已通过 2-已拒绝 3-已付款") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(fundService.getReimbursementList(clubId, status, pageNum, pageSize));
    }

    @Operation(summary = "获取我的报销申请列表")
    @GetMapping("/reimbursement/my")
    public Result<PageResult<Reimbursement>> getMyReimbursementList(
            @Parameter(description = "申请人ID") @RequestParam Long applicantId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(fundService.getMyReimbursementList(applicantId, pageNum, pageSize));
    }

    @Operation(summary = "提交报销申请")
    @PostMapping("/reimbursement/submit")
    public Result<Reimbursement> submitReimbursement(@RequestBody Reimbursement reimbursement) {
        return Result.success(fundService.submitReimbursement(reimbursement));
    }

    @Operation(summary = "审批报销申请")
    @PostMapping("/reimbursement/audit")
    public Result<Reimbursement> auditReimbursement(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        Long auditorId = Long.valueOf(params.get("auditorId").toString());
        String auditorName = (String) params.get("auditorName");
        String auditOpinion = (String) params.get("auditOpinion");
        return Result.success(fundService.auditReimbursement(id, status, auditorId, auditorName, auditOpinion));
    }

    @Operation(summary = "获取报销申请详情")
    @GetMapping("/reimbursement/{id}")
    public Result<Reimbursement> getReimbursementDetail(@Parameter(description = "申请ID") @PathVariable Long id) {
        return Result.success(fundService.getReimbursementDetail(id));
    }

    @Operation(summary = "获取经费统计报表")
    @GetMapping("/statistics")
    public Result<FundStatistics> getFundStatistics(
            @Parameter(description = "社团ID") @RequestParam Long clubId,
            @Parameter(description = "统计类型 0-日报 1-周报 2-月报 3-季报 4-年报") @RequestParam(defaultValue = "2") Integer statisticsType,
            @Parameter(description = "统计周期，如2024-01") @RequestParam String period) {
        return Result.success(fundService.getFundStatistics(clubId, statisticsType, period));
    }

    @Operation(summary = "获取经费统计报表列表")
    @GetMapping("/statistics/list")
    public Result<List<FundStatistics>> getFundStatisticsList(
            @Parameter(description = "社团ID") @RequestParam Long clubId,
            @Parameter(description = "统计类型 0-日报 1-周报 2-月报 3-季报 4-年报") @RequestParam(defaultValue = "2") Integer statisticsType) {
        return Result.success(fundService.getFundStatisticsList(clubId, statisticsType));
    }

    @Operation(summary = "生成经费统计报表")
    @PostMapping("/statistics/generate")
    public Result<FundStatistics> generateFundStatistics(@RequestBody Map<String, Object> params) {
        Long clubId = Long.valueOf(params.get("clubId").toString());
        Integer statisticsType = Integer.valueOf(params.get("statisticsType").toString());
        String period = (String) params.get("period");
        return Result.success(fundService.generateFundStatistics(clubId, statisticsType, period));
    }
}
