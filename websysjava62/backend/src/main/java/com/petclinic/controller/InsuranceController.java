package com.petclinic.controller;

import com.petclinic.common.Result;
import com.petclinic.dto.ClaimCalculateResult;
import com.petclinic.dto.ClaimRequest;
import com.petclinic.dto.ClaimResponse;
import com.petclinic.entity.InsuranceClaim;
import com.petclinic.entity.InsuranceCompany;
import com.petclinic.service.InsuranceApiClientService;
import com.petclinic.service.InsuranceClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 保险理赔Controller
 * 提供保险公司管理、理赔申请、理赔查询、理赔审核等完整功能
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/insurance")
@Tag(name = "保险理赔管理", description = "保险公司管理、问诊费用直接理赔")
public class InsuranceController {

    @Autowired
    private InsuranceClaimService insuranceClaimService;

    @Autowired
    private InsuranceApiClientService insuranceApiClientService;

    // ==================== 保险公司管理 ====================

    @GetMapping("/companies")
    @Operation(summary = "查询所有保险公司")
    public Result<List<InsuranceCompany>> findAllCompanies() {
        return Result.success(insuranceClaimService.findAllCompanies());
    }

    @GetMapping("/companies/active")
    @Operation(summary = "查询启用的保险公司列表")
    public Result<List<InsuranceCompany>> findActiveCompanies() {
        return Result.success(insuranceClaimService.findActiveCompanies());
    }

    @GetMapping("/companies/{id}")
    @Operation(summary = "根据ID查询保险公司")
    public Result<InsuranceCompany> findCompanyById(@PathVariable Long id) {
        Optional<InsuranceCompany> company = insuranceClaimService.findCompanyById(id);
        return company.map(Result::success).orElse(Result.error("保险公司不存在"));
    }

    @PostMapping("/companies")
    @Operation(summary = "新增保险公司")
    public Result<InsuranceCompany> saveCompany(@RequestBody InsuranceCompany company) {
        return Result.success(insuranceClaimService.saveCompany(company));
    }

    @PutMapping("/companies/{id}")
    @Operation(summary = "更新保险公司")
    public Result<InsuranceCompany> updateCompany(@PathVariable Long id, @RequestBody InsuranceCompany company) {
        InsuranceCompany updated = insuranceClaimService.updateCompany(id, company);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @DeleteMapping("/companies/{id}")
    @Operation(summary = "删除保险公司")
    public Result<Void> deleteCompanyById(@PathVariable Long id) {
        insuranceClaimService.deleteCompanyById(id);
        return Result.success();
    }

    // ==================== 理赔预计算和验证 ====================

    @PostMapping("/calculate")
    @Operation(summary = "预计算理赔金额")
    public Result<ClaimCalculateResult> calculateClaim(
            @Parameter(description = "保险公司ID") @RequestParam Long companyId,
            @Parameter(description = "问诊总费用") @RequestParam BigDecimal totalFee) {
        try {
            ClaimCalculateResult result = insuranceClaimService.calculateClaim(companyId, totalFee);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/validate-policy")
    @Operation(summary = "验证保单有效性")
    public Result<Boolean> validatePolicy(
            @Parameter(description = "保险公司ID") @RequestParam Long companyId,
            @Parameter(description = "保单号") @RequestParam String policyNo) {
        boolean valid = insuranceClaimService.validatePolicy(companyId, policyNo);
        return Result.success(valid);
    }

    @GetMapping("/quote")
    @Operation(summary = "获取保险报价")
    public Result<Map<String, Object>> getInsuranceQuote(
            @Parameter(description = "保险公司ID") @RequestParam Long companyId,
            @Parameter(description = "宠物类型") @RequestParam String petType,
            @Parameter(description = "宠物年龄") @RequestParam int age) {
        Optional<InsuranceCompany> companyOpt = insuranceClaimService.findCompanyById(companyId);
        if (!companyOpt.isPresent()) {
            return Result.error("保险公司不存在");
        }
        Map<String, Object> quote = insuranceApiClientService.getInsuranceQuote(companyOpt.get(), petType, age);
        return Result.success(quote);
    }

    // ==================== 理赔申请 ====================

    @PostMapping("/claims")
    @Operation(summary = "创建理赔申请（问诊费用直接理赔）")
    public Result<ClaimResponse> createClaim(@RequestBody ClaimRequest request) {
        ClaimResponse response = insuranceClaimService.createClaim(request);
        if (response.getSuccess()) {
            return Result.success(response);
        } else {
            return Result.error(response.getMessage());
        }
    }

    @PostMapping("/claims/quick/{consultationId}")
    @Operation(summary = "一键理赔（根据问诊ID快速申请理赔）")
    public Result<ClaimResponse> quickClaim(
            @PathVariable Long consultationId,
            @Parameter(description = "保单号") @RequestParam String policyNo,
            @Parameter(description = "保险公司ID（可选，不传则使用默认）") @RequestParam(required = false) Long companyId) {
        ClaimResponse response = insuranceClaimService.quickClaim(consultationId, policyNo, companyId);
        if (response.getSuccess()) {
            return Result.success(response);
        } else {
            return Result.error(response.getMessage());
        }
    }

    // ==================== 理赔查询 ====================

    @GetMapping("/claims")
    @Operation(summary = "查询所有理赔记录")
    public Result<List<InsuranceClaim>> findAllClaims() {
        return Result.success(insuranceClaimService.findAllClaims());
    }

    @GetMapping("/claims/{id}")
    @Operation(summary = "根据ID查询理赔记录")
    public Result<InsuranceClaim> findClaimById(@PathVariable Long id) {
        Optional<InsuranceClaim> claim = insuranceClaimService.findClaimById(id);
        return claim.map(Result::success).orElse(Result.error("理赔记录不存在"));
    }

    @GetMapping("/claims/status/{status}")
    @Operation(summary = "根据状态查询理赔记录")
    public Result<List<InsuranceClaim>> findClaimsByStatus(@PathVariable String status) {
        return Result.success(insuranceClaimService.findClaimsByStatus(status));
    }

    @GetMapping("/claims/consultation/{consultationId}")
    @Operation(summary = "根据问诊ID查询理赔记录")
    public Result<List<InsuranceClaim>> findClaimsByConsultationId(@PathVariable Long consultationId) {
        return Result.success(insuranceClaimService.findClaimsByConsultationId(consultationId));
    }

    @GetMapping("/claims/search")
    @Operation(summary = "根据宠物名称搜索理赔记录")
    public Result<List<InsuranceClaim>> searchClaimsByPetName(@RequestParam String petName) {
        return Result.success(insuranceClaimService.searchClaimsByPetName(petName));
    }

    // ==================== 理赔审核/处理 ====================

    @PutMapping("/claims/{id}/review")
    @Operation(summary = "审核理赔申请")
    public Result<InsuranceClaim> reviewClaim(
            @PathVariable Long id,
            @Parameter(description = "审核状态：APPROVED-通过，REJECTED-拒绝，COMPLETED-已完成") @RequestParam String status,
            @Parameter(description = "审核人") @RequestParam String reviewedBy,
            @Parameter(description = "拒绝原因（拒绝时必填）") @RequestParam(required = false) String rejectReason) {
        InsuranceClaim updated = insuranceClaimService.reviewClaim(id, status, reviewedBy, rejectReason);
        return updated != null ? Result.success(updated) : Result.error("审核失败");
    }

    @PutMapping("/claims/{id}/complete")
    @Operation(summary = "完成理赔（支付）")
    public Result<InsuranceClaim> completeClaim(@PathVariable Long id) {
        InsuranceClaim updated = insuranceClaimService.completeClaim(id);
        return updated != null ? Result.success(updated) : Result.error("操作失败");
    }

    @PutMapping("/claims/{id}/reject")
    @Operation(summary = "拒绝理赔")
    public Result<InsuranceClaim> rejectClaim(
            @PathVariable Long id,
            @Parameter(description = "拒绝原因") @RequestParam String rejectReason,
            @Parameter(description = "审核人") @RequestParam String reviewedBy) {
        InsuranceClaim updated = insuranceClaimService.rejectClaim(id, rejectReason, reviewedBy);
        return updated != null ? Result.success(updated) : Result.error("操作失败");
    }

    @PutMapping("/claims/{id}/cancel")
    @Operation(summary = "取消理赔申请")
    public Result<Boolean> cancelClaim(@PathVariable Long id) {
        boolean success = insuranceClaimService.cancelClaim(id);
        return success ? Result.success(true) : Result.error("取消失败（可能已完成或不存在）");
    }

    @PutMapping("/claims/{id}/sync-status")
    @Operation(summary = "同步理赔状态（从保险公司获取最新状态）")
    public Result<InsuranceClaim> syncClaimStatus(@PathVariable Long id) {
        InsuranceClaim updated = insuranceClaimService.syncClaimStatus(id);
        return updated != null ? Result.success(updated) : Result.error("同步失败");
    }

    @DeleteMapping("/claims/{id}")
    @Operation(summary = "删除理赔记录")
    public Result<Void> deleteClaimById(@PathVariable Long id) {
        insuranceClaimService.deleteClaimById(id);
        return Result.success();
    }
}
