package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Consultation;
import com.pethospital.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 在线问诊Controller
 * 提供图文问诊、视频问诊、AI紧急症状识别等API接口
 * 
 * @author Pet Hospital Team
 */
@RestController
@RequestMapping("/consultations")
@Tag(name = "在线问诊", description = "图文问诊、视频问诊、AI症状识别等接口")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    /**
     * 获取症状类型列表（分步引导）
     */
    @GetMapping("/symptom-types")
    @Operation(summary = "获取症状类型", description = "获取分步引导的症状类型列表")
    public Result<List<Map<String, Object>>> getSymptomTypes() {
        return Result.success(consultationService.getSymptomTypes());
    }

    /**
     * AI紧急症状识别
     */
    @PostMapping("/check-emergency")
    @Operation(summary = "AI紧急症状识别", description = "识别症状是否为紧急情况，提供就医建议")
    public Result<Map<String, Object>> checkEmergencySymptoms(
            @Parameter(description = "症状描述") @RequestBody Map<String, String> request) {
        String symptoms = request.get("symptoms");
        return Result.success(consultationService.checkEmergencySymptoms(symptoms));
    }

    /**
     * 创建问诊
     */
    @PostMapping
    @Operation(summary = "创建问诊", description = "创建新的在线问诊（图文/视频）")
    public Result<Consultation> createConsultation(@RequestBody Consultation consultation) {
        return Result.success(consultationService.createConsultation(consultation));
    }

    /**
     * 获取问诊详情
     */
    @GetMapping("/{consultationId}")
    @Operation(summary = "获取问诊详情", description = "根据问诊ID获取问诊详细信息")
    public Result<Consultation> getConsultationById(
            @Parameter(description = "问诊ID") @PathVariable Long consultationId) {
        return Result.success(consultationService.getConsultationById(consultationId));
    }

    /**
     * 获取用户问诊列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户问诊列表", description = "获取指定用户的所有问诊记录")
    public Result<List<Consultation>> getUserConsultations(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(consultationService.getUserConsultations(userId));
    }
}
