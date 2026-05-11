package com.hospital.controller;

import com.hospital.common.PageResult;
import com.hospital.common.Result;
import com.hospital.entity.Admission;
import com.hospital.entity.User;
import com.hospital.repository.AdmissionRepository;
import com.hospital.repository.UserRepository;
import com.hospital.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Tag(name = "入院预约管理", description = "出入院预约相关接口")
@RestController
@RequestMapping("/admission")
public class AdmissionController {

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取当前用户入院预约列表")
    @GetMapping("/my")
    public Result<PageResult<Admission>> getMyAdmissions(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("createTime").descending());
        Page<Admission> page = admissionRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);

        PageResult<Admission> pageResult = PageResult.of(
                page.getContent(),
                page.getTotalElements(),
                pageNum,
                pageSize
        );
        return Result.success(pageResult);
    }

    @Operation(summary = "获取入院预约详情")
    @GetMapping("/{id}")
    public Result<Admission> getAdmissionDetail(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        return admissionRepository.findById(id)
                .<Result<Admission>>map(admission -> {
                    if (!admission.getUserId().equals(userId)) {
                        return Result.<Admission>forbidden();
                    }
                    return Result.success(admission);
                })
                .orElse(Result.<Admission>notFound());
    }

    @Operation(summary = "提交入院申请")
    @PostMapping
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public Result<Admission> createAdmission(
            @RequestBody AdmissionRequest request,
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Result.error("用户不存在");
        }

        Admission admission = new Admission();
        admission.setApplicationNo("AP" + System.currentTimeMillis());
        admission.setUserId(userId);
        admission.setPatientName(user.getRealName());
        admission.setDeptCode(request.getDeptCode());
        admission.setDeptName(request.getDeptName());
        admission.setWardType(request.getWardType());
        admission.setExpectBedNo(request.getExpectBedNo());
        admission.setAdmissionDate(request.getAdmissionDate());
        admission.setDiagnosis(request.getDiagnosis());
        admission.setDoctorName(request.getDoctorName());
        admission.setStatus(0);
        admission.setDepositAmount(request.getDepositAmount() != null ? request.getDepositAmount() : new java.math.BigDecimal("2000"));
        admission.setRemark(request.getRemark());
        admission.setCreateBy(user.getUsername());

        Admission saved = admissionRepository.save(admission);
        return Result.success("申请提交成功", saved);
    }

    @Operation(summary = "取消入院申请")
    @PostMapping("/cancel/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public Result<Admission> cancelAdmission(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        return admissionRepository.findById(id)
                .<Result<Admission>>map(admission -> {
                    if (!admission.getUserId().equals(userId)) {
                        return Result.<Admission>forbidden();
                    }
                    if (admission.getStatus() >= 2) {
                        return Result.<Admission>error("已入院的申请无法取消");
                    }
                    admission.setStatus(4);
                    admission.setUpdateTime(LocalDateTime.now());
                    Admission saved = admissionRepository.save(admission);
                    return Result.success("取消成功", saved);
                })
                .orElse(Result.<Admission>notFound());
    }

    @Operation(summary = "获取入院预约状态统计")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getAdmissionStats(
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        List<Admission> all = admissionRepository.findByUserIdOrderByCreateTimeDesc(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", all.size());
        
        long pending = all.stream().filter(a -> a.getStatus() == 0).count();
        long approved = all.stream().filter(a -> a.getStatus() == 1).count();
        long admitted = all.stream().filter(a -> a.getStatus() == 2).count();
        long discharged = all.stream().filter(a -> a.getStatus() == 3).count();
        long cancelled = all.stream().filter(a -> a.getStatus() == 4).count();
        
        stats.put("pending", pending);
        stats.put("approved", approved);
        stats.put("admitted", admitted);
        stats.put("discharged", discharged);
        stats.put("cancelled", cancelled);

        return Result.success(stats);
    }

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return jwtUtil.getUserIdFromToken(jwt);
        }
        return null;
    }

    @Data
    public static class AdmissionRequest {
        private String deptCode;
        private String deptName;
        private String wardType;
        private String expectBedNo;
        private java.time.LocalDate admissionDate;
        private String diagnosis;
        private String doctorName;
        private java.math.BigDecimal depositAmount;
        private String remark;
    }
}
