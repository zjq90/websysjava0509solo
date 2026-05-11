package com.medical.registration.controller;

import com.medical.registration.common.Result;
import com.medical.registration.dto.PaymentDTO;
import com.medical.registration.dto.RegistrationDTO;
import com.medical.registration.dto.RegistrationVO;
import com.medical.registration.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "挂号管理", description = "挂号预约相关接口")
@RestController
@RequestMapping("/registrations")
public class RegistrationController {
    
    @Autowired
    private RegistrationService registrationService;
    
    @Operation(summary = "预约挂号")
    @PostMapping
    public Result<RegistrationVO> createRegistration(@Valid @RequestBody RegistrationDTO dto) {
        Long userId = getCurrentUserId();
        return Result.success(registrationService.createRegistration(userId, dto));
    }
    
    @Operation(summary = "支付挂号费")
    @PostMapping("/pay")
    public Result<RegistrationVO> payRegistration(@Valid @RequestBody PaymentDTO dto) {
        Long userId = getCurrentUserId();
        return Result.success(registrationService.payRegistration(userId, dto));
    }
    
    @Operation(summary = "取消挂号")
    @PostMapping("/{registrationNo}/cancel")
    public Result<RegistrationVO> cancelRegistration(@PathVariable String registrationNo) {
        Long userId = getCurrentUserId();
        return Result.success(registrationService.cancelRegistration(userId, registrationNo));
    }
    
    @Operation(summary = "获取挂号记录列表")
    @GetMapping
    public Result<Page<RegistrationVO>> getRegistrationList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getCurrentUserId();
        return Result.success(registrationService.getRegistrationList(userId, page, size));
    }
    
    @Operation(summary = "获取挂号详情")
    @GetMapping("/{registrationNo}")
    public Result<RegistrationVO> getRegistrationDetail(@PathVariable String registrationNo) {
        Long userId = getCurrentUserId();
        return Result.success(registrationService.getRegistrationDetail(userId, registrationNo));
    }
    
    @Operation(summary = "获取今日待就诊预约")
    @GetMapping("/today")
    public Result<List<RegistrationVO>> getTodayAppointments() {
        Long userId = getCurrentUserId();
        return Result.success(registrationService.getTodayAppointments(userId));
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
