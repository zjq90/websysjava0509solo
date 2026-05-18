package com.psyconsult.controller;

import com.psyconsult.dto.ApiResponse;
import com.psyconsult.entity.Counselor;
import com.psyconsult.service.AuthService;
import com.psyconsult.service.CounselorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/counselors")
@Tag(name = "咨询师接口", description = "咨询师查询、推荐等接口")
public class CounselorController {

    private final CounselorService counselorService;
    private final AuthService authService;

    public CounselorController(CounselorService counselorService, AuthService authService) {
        this.counselorService = counselorService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "获取所有咨询师", description = "获取所有可用的咨询师列表")
    public ApiResponse<List<Counselor>> getAllCounselors() {
        return ApiResponse.success(counselorService.getAllCounselors());
    }

    @GetMapping("/recommended")
    @Operation(summary = "获取推荐咨询师", description = "根据用户的心理测评结果推荐匹配的咨询师")
    public ApiResponse<List<Counselor>> getRecommendedCounselors(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        return ApiResponse.success(counselorService.getRecommendedCounselors(userId));
    }

    @GetMapping("/filter")
    @Operation(summary = "筛选咨询师", description = "按领域、语言、价格范围筛选咨询师")
    public ApiResponse<List<Counselor>> getCounselorsByFilters(
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ApiResponse.success(counselorService.getCounselorsByFilters(specialty, language, minPrice, maxPrice));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取咨询师详情", description = "根据ID获取咨询师详细信息")
    public ApiResponse<Counselor> getCounselorById(@PathVariable Long id) {
        return ApiResponse.success(counselorService.getCounselorById(id));
    }
}
