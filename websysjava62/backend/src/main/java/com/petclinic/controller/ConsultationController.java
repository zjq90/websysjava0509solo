package com.petclinic.controller;

import com.petclinic.annotation.NeedReview;
import com.petclinic.common.Result;
import com.petclinic.dto.ReviewResult;
import com.petclinic.entity.ConsultationRecord;
import com.petclinic.entity.DiagnosisSuggestion;
import com.petclinic.entity.InsuranceClaim;
import com.petclinic.service.ConsultationService;
import com.petclinic.service.ReviewRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 问诊管理Controller
 * 包含问诊记录、AI诊断建议、保险理赔
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/consultation")
@Tag(name = "问诊管理", description = "问诊记录、AI诊断建议、保险理赔")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private ReviewRuleService reviewRuleService;

    // ==================== 问诊记录管理 ====================

    @GetMapping("/records")
    @Operation(summary = "查询所有问诊记录")
    public Result<List<ConsultationRecord>> findAllConsultations() {
        return Result.success(consultationService.findAllConsultations());
    }

    @GetMapping("/records/{id}")
    @Operation(summary = "根据ID查询问诊记录")
    public Result<ConsultationRecord> findConsultationById(@PathVariable Long id) {
        Optional<ConsultationRecord> record = consultationService.findConsultationById(id);
        return record.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping("/records")
    @Operation(summary = "新增问诊记录")
    @NeedReview
    public Result<ConsultationRecord> saveConsultation(@RequestBody ConsultationRecord record) {
        return Result.success(consultationService.saveConsultation(record));
    }

    @PutMapping("/records/{id}")
    @Operation(summary = "更新问诊记录")
    @NeedReview
    public Result<ConsultationRecord> updateConsultation(@PathVariable Long id, @RequestBody ConsultationRecord record) {
        ConsultationRecord updated = consultationService.updateConsultation(id, record);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @PostMapping("/records/review-preview")
    @Operation(summary = "预览问诊记录内容审核结果（不实际保存）")
    public Result<ReviewResult> previewConsultationReview(@RequestBody ConsultationRecord record) {
        StringBuilder content = new StringBuilder();
        if (record.getChiefComplaint() != null) content.append(record.getChiefComplaint()).append(" ");
        if (record.getSymptomDetail() != null) content.append(record.getSymptomDetail()).append(" ");
        if (record.getDiagnosisResult() != null) content.append(record.getDiagnosisResult()).append(" ");
        if (record.getTreatmentPlan() != null) content.append(record.getTreatmentPlan()).append(" ");
        if (record.getDoctorAdvice() != null) content.append(record.getDoctorAdvice()).append(" ");
        
        ReviewResult result = reviewRuleService.reviewContent(content.toString());
        return Result.success(result);
    }

    @DeleteMapping("/records/{id}")
    @Operation(summary = "删除问诊记录")
    public Result<Void> deleteConsultationById(@PathVariable Long id) {
        consultationService.deleteConsultationById(id);
        return Result.success();
    }

    @GetMapping("/records/status/{status}")
    @Operation(summary = "根据状态查询问诊记录")
    public Result<List<ConsultationRecord>> findConsultationsByStatus(@PathVariable String status) {
        return Result.success(consultationService.findConsultationsByStatus(status));
    }

    @GetMapping("/records/search")
    @Operation(summary = "根据宠物名称搜索问诊记录")
    public Result<List<ConsultationRecord>> searchConsultationsByPetName(@RequestParam String petName) {
        return Result.success(consultationService.searchConsultationsByPetName(petName));
    }

    // ==================== AI诊断建议管理 ====================

    @GetMapping("/suggestions")
    @Operation(summary = "查询所有AI诊断建议")
    public Result<List<DiagnosisSuggestion>> findAllSuggestions() {
        return Result.success(consultationService.findAllSuggestions());
    }

    @GetMapping("/suggestions/{id}")
    @Operation(summary = "根据ID查询AI诊断建议")
    public Result<DiagnosisSuggestion> findSuggestionById(@PathVariable Long id) {
        Optional<DiagnosisSuggestion> suggestion = consultationService.findSuggestionById(id);
        return suggestion.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @GetMapping("/suggestions/consultation/{consultationId}")
    @Operation(summary = "根据问诊ID查询AI诊断建议")
    public Result<List<DiagnosisSuggestion>> findSuggestionsByConsultationId(@PathVariable Long consultationId) {
        return Result.success(consultationService.findSuggestionsByConsultationId(consultationId));
    }

    @PostMapping("/suggestions/generate/{consultationId}")
    @Operation(summary = "生成AI诊断建议")
    public Result<DiagnosisSuggestion> generateAIDiagnosis(@PathVariable Long consultationId) {
        DiagnosisSuggestion suggestion = consultationService.generateAIDiagnosis(consultationId);
        return suggestion != null ? Result.success(suggestion) : Result.error("生成失败");
    }

    @PutMapping("/suggestions/{id}/review")
    @Operation(summary = "审核AI诊断建议")
    public Result<DiagnosisSuggestion> reviewSuggestion(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam String reviewedBy) {
        DiagnosisSuggestion reviewed = consultationService.reviewSuggestion(id, status, reviewedBy);
        return reviewed != null ? Result.success(reviewed) : Result.error("审核失败");
    }

    // ==================== 保险理赔管理 ====================

    @GetMapping("/claims")
    @Operation(summary = "查询所有理赔记录")
    public Result<List<InsuranceClaim>> findAllClaims() {
        return Result.success(consultationService.findAllClaims());
    }

    @GetMapping("/claims/{id}")
    @Operation(summary = "根据ID查询理赔记录")
    public Result<InsuranceClaim> findClaimById(@PathVariable Long id) {
        Optional<InsuranceClaim> claim = consultationService.findClaimById(id);
        return claim.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping("/claims")
    @Operation(summary = "创建保险理赔申请")
    public Result<InsuranceClaim> createClaim(@RequestBody InsuranceClaim claim) {
        return Result.success(consultationService.createClaim(claim));
    }

    @PutMapping("/claims/{id}/status")
    @Operation(summary = "更新理赔状态")
    public Result<InsuranceClaim> updateClaimStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam String reviewedBy,
            @RequestParam(required = false) String rejectReason) {
        InsuranceClaim updated = consultationService.updateClaimStatus(id, status, reviewedBy, rejectReason);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @GetMapping("/claims/status/{status}")
    @Operation(summary = "根据状态查询理赔记录")
    public Result<List<InsuranceClaim>> findClaimsByStatus(@PathVariable String status) {
        return Result.success(consultationService.findClaimsByStatus(status));
    }
}
