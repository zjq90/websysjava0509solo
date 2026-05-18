package com.psyconsult.controller;

import com.psyconsult.dto.ApiResponse;
import com.psyconsult.entity.ChatMessage;
import com.psyconsult.entity.Consultation;
import com.psyconsult.entity.EmotionRecord;
import com.psyconsult.service.AuthService;
import com.psyconsult.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultations")
@Tag(name = "咨询接口", description = "咨询创建、聊天、记录等接口")
public class ConsultationController {

    private final ConsultationService consultationService;
    private final AuthService authService;

    public ConsultationController(ConsultationService consultationService, AuthService authService) {
        this.consultationService = consultationService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "开始咨询", description = "创建新的咨询会话")
    public ApiResponse<Consultation> createConsultation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> request) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        Long counselorId = Long.valueOf(request.get("counselorId").toString());
        String type = (String) request.get("type");
        String topic = (String) request.get("topic");
        return ApiResponse.success("咨询开始", consultationService.createConsultation(userId, counselorId, type, topic));
    }

    @GetMapping
    @Operation(summary = "获取我的咨询", description = "获取当前用户的所有咨询记录")
    public ApiResponse<List<Consultation>> getUserConsultations(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        return ApiResponse.success(consultationService.getUserConsultations(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取咨询详情", description = "根据ID获取咨询详细信息")
    public ApiResponse<Consultation> getConsultationById(@PathVariable Long id) {
        return ApiResponse.success(consultationService.getConsultationById(id));
    }

    @PostMapping("/{consultationId}/messages")
    @Operation(summary = "发送消息", description = "在咨询会话中发送消息")
    public ApiResponse<ChatMessage> sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long consultationId,
            @RequestBody Map<String, Object> request) {
        Long senderId = authService.getCurrentUser(userDetails.getUsername()).getId();
        String senderType = "USER";
        String messageType = (String) request.getOrDefault("type", "TEXT");
        String content = (String) request.get("content");
        String mediaUrl = (String) request.get("mediaUrl");
        return ApiResponse.success("消息发送成功", 
                consultationService.sendMessage(consultationId, senderId, senderType, messageType, content, mediaUrl));
    }

    @GetMapping("/{consultationId}/messages")
    @Operation(summary = "获取聊天记录", description = "获取咨询会话的所有聊天消息")
    public ApiResponse<List<ChatMessage>> getChatMessages(@PathVariable Long consultationId) {
        return ApiResponse.success(consultationService.getChatMessages(consultationId));
    }

    @PostMapping("/{consultationId}/end")
    @Operation(summary = "结束咨询", description = "结束咨询会话并记录总结")
    public ApiResponse<Consultation> endConsultation(
            @PathVariable Long consultationId,
            @RequestBody Map<String, Object> request) {
        String notes = (String) request.get("notes");
        Integer emotionScore = request.get("emotionScore") != null ? 
                Integer.valueOf(request.get("emotionScore").toString()) : null;
        String keywords = (String) request.get("keywords");
        return ApiResponse.success("咨询已结束", 
                consultationService.endConsultation(consultationId, notes, emotionScore, keywords));
    }

    @GetMapping("/emotion-trend")
    @Operation(summary = "获取情绪趋势", description = "获取用户的情绪变化趋势数据")
    public ApiResponse<List<EmotionRecord>> getEmotionTrend(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        return ApiResponse.success(consultationService.getEmotionTrend(userId, startDate, endDate));
    }
}
