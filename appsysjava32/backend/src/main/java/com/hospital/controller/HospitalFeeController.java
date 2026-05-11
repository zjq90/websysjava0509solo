package com.hospital.controller;

import com.hospital.common.PageResult;
import com.hospital.common.Result;
import com.hospital.entity.HospitalFee;
import com.hospital.repository.AdmissionRepository;
import com.hospital.repository.DepositPaymentRepository;
import com.hospital.repository.HospitalFeeRepository;
import com.hospital.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "住院费用管理", description = "住院费用查询相关接口")
@RestController
@RequestMapping("/hospital-fee")
public class HospitalFeeController {

    @Autowired
    private HospitalFeeRepository hospitalFeeRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private DepositPaymentRepository depositPaymentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取当前用户费用列表")
    @GetMapping("/my")
    public Result<PageResult<HospitalFee>> getMyFees(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false) Long admissionId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        List<HospitalFee> fees;
        if (admissionId != null) {
            fees = hospitalFeeRepository.findByAdmissionIdOrderByFeeDateDesc(admissionId);
        } else {
            fees = hospitalFeeRepository.findByUserIdOrderByFeeDateDesc(userId);
        }

        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, fees.size());
        List<HospitalFee> pageContent = start < fees.size() ? fees.subList(start, end) : new ArrayList<>();

        PageResult<HospitalFee> pageResult = PageResult.of(
                pageContent,
                (long) fees.size(),
                pageNum,
                pageSize
        );
        return Result.success(pageResult);
    }

    @Operation(summary = "获取费用统计信息")
    @GetMapping("/summary")
    public Result<Map<String, Object>> getFeeSummary(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false) Long admissionId) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        BigDecimal totalFee;
        BigDecimal totalPaid;
        if (admissionId != null) {
            totalFee = hospitalFeeRepository.sumTotalAmountByAdmissionId(admissionId);
            totalPaid = depositPaymentRepository.sumPaidAmountByAdmissionId(admissionId);
        } else {
            totalFee = hospitalFeeRepository.sumTotalAmountByUserId(userId);
            totalPaid = depositPaymentRepository.sumPaidAmountByUserId(userId);
        }

        if (totalFee == null) totalFee = BigDecimal.ZERO;
        if (totalPaid == null) totalPaid = BigDecimal.ZERO;

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalFee", totalFee);
        summary.put("totalPaid", totalPaid);
        summary.put("balance", totalPaid.subtract(totalFee));

        List<HospitalFee> fees = hospitalFeeRepository.findByUserIdOrderByFeeDateDesc(userId);
        Map<String, BigDecimal> byType = fees.stream()
                .collect(Collectors.groupingBy(
                        HospitalFee::getFeeType,
                        Collectors.reducing(BigDecimal.ZERO, HospitalFee::getAmount, BigDecimal::add)
                ));
        summary.put("feeByType", byType);

        return Result.success(summary);
    }

    @Operation(summary = "按日期分组获取费用")
    @GetMapping("/by-date")
    public Result<List<Map<String, Object>>> getFeesByDate(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false) Long admissionId) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        List<HospitalFee> fees = admissionId != null 
            ? hospitalFeeRepository.findByAdmissionIdOrderByFeeDateDesc(admissionId)
            : hospitalFeeRepository.findByUserIdOrderByFeeDateDesc(userId);

        Map<LocalDate, List<HospitalFee>> grouped = fees.stream()
                .collect(Collectors.groupingBy(HospitalFee::getFeeDate));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<HospitalFee>> entry : grouped.entrySet()) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", entry.getKey());
            dayData.put("fees", entry.getValue());
            dayData.put("dayTotal", entry.getValue().stream()
                    .map(HospitalFee::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            result.add(dayData);
        }

        result.sort((a, b) -> ((LocalDate) b.get("date")).compareTo((LocalDate) a.get("date")));
        return Result.success(result);
    }

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return jwtUtil.getUserIdFromToken(jwt);
        }
        return null;
    }
}
