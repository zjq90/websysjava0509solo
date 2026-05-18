package com.psyconsult.controller;

import com.psyconsult.dto.ApiResponse;
import com.psyconsult.entity.Assessment;
import com.psyconsult.service.AssessmentService;
import com.psyconsult.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@Tag(name = "心理测评接口", description = "心理测评问卷提交、查询等接口")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AuthService authService;

    public AssessmentController(AssessmentService assessmentService, AuthService authService) {
        this.assessmentService = assessmentService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "提交测评", description = "提交心理测评问卷结果")
    public ApiResponse<Assessment> createAssessment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Assessment assessment) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        return ApiResponse.success("测评提交成功", assessmentService.createAssessment(userId, assessment));
    }

    @GetMapping
    @Operation(summary = "获取我的测评", description = "获取当前用户的所有测评记录")
    public ApiResponse<List<Assessment>> getUserAssessments(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        return ApiResponse.success(assessmentService.getUserAssessments(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取测评详情", description = "根据ID获取测评详细结果")
    public ApiResponse<Assessment> getAssessmentById(@PathVariable Long id) {
        return ApiResponse.success(assessmentService.getAssessmentById(id));
    }
}
