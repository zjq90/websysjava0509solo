package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Consultation;
import com.pethospital.entity.ConsultationMessage;
import com.pethospital.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultation")
@Tag(name = "问诊管理", description = "问诊列表、消息发送等接口")
@CrossOrigin(origins = "*")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping("/doctor/{doctorId}/pending")
    @Operation(summary = "获取医生待接诊列表（按紧急程度排序）")
    public Result<List<Consultation>> getDoctorPendingConsultations(@PathVariable Long doctorId) {
        return Result.success(consultationService.getDoctorPendingConsultations(doctorId));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "获取医生所有问诊")
    public Result<List<Consultation>> getDoctorConsultations(@PathVariable Long doctorId) {
        return Result.success(consultationService.getDoctorConsultations(doctorId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取问诊详情")
    public Result<Consultation> getConsultationById(@PathVariable Long id) {
        Optional<Consultation> consultation = consultationService.getConsultationById(id);
        return consultation.map(Result::success).orElseGet(() -> Result.error("问诊不存在"));
    }

    @GetMapping("/{id}/messages")
    @Operation(summary = "获取问诊消息列表")
    public Result<List<ConsultationMessage>> getConsultationMessages(@PathVariable Long id) {
        return Result.success(consultationService.getConsultationMessages(id));
    }

    @PostMapping("/{id}/message")
    @Operation(summary = "发送消息")
    public Result<ConsultationMessage> sendMessage(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String senderType = params.get("senderType");
        Long senderId = Long.parseLong(params.get("senderId"));
        String content = params.get("content");
        String messageType = params.getOrDefault("messageType", "TEXT");
        return Result.success(consultationService.sendMessage(id, senderType, senderId, content, messageType));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新问诊状态")
    public Result<Consultation> updateConsultationStatus(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String status = params.get("status");
        Consultation consultation = consultationService.updateConsultationStatus(id, status);
        if (consultation != null) {
            return Result.success(consultation);
        }
        return Result.error("更新失败");
    }

    @PostMapping
    @Operation(summary = "创建问诊")
    public Result<Consultation> createConsultation(@RequestBody Consultation consultation) {
        return Result.success(consultationService.createConsultation(consultation));
    }

    @GetMapping
    @Operation(summary = "获取所有问诊")
    public Result<List<Consultation>> getAllConsultations() {
        return Result.success(consultationService.getAllConsultations());
    }
}
