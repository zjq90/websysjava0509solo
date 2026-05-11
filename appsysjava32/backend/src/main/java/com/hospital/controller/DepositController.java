package com.hospital.controller;

import com.hospital.common.PageResult;
import com.hospital.common.Result;
import com.hospital.entity.Admission;
import com.hospital.entity.DepositPayment;
import com.hospital.repository.AdmissionRepository;
import com.hospital.repository.DepositPaymentRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Tag(name = "押金管理", description = "住院押金补缴相关接口")
@RestController
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    private DepositPaymentRepository depositPaymentRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取当前用户押金支付记录")
    @GetMapping("/my")
    public Result<PageResult<DepositPayment>> getMyDeposits(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false) Long admissionId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        List<DepositPayment> payments;
        if (admissionId != null) {
            payments = depositPaymentRepository.findByAdmissionIdOrderByCreateTimeDesc(admissionId);
        } else {
            payments = depositPaymentRepository.findByUserIdOrderByCreateTimeDesc(userId);
        }

        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, payments.size());
        List<DepositPayment> pageContent = start < payments.size() ? payments.subList(start, end) : new ArrayList<>();

        PageResult<DepositPayment> pageResult = PageResult.of(
                pageContent,
                (long) payments.size(),
                pageNum,
                pageSize
        );
        return Result.success(pageResult);
    }

    @Operation(summary = "提交押金补缴申请")
    @PostMapping
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public Result<Map<String, Object>> createDepositPayment(
            @RequestBody DepositPaymentRequest request,
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        Admission admission = admissionRepository.findById(request.getAdmissionId()).orElse(null);
        if (admission == null) {
            return Result.error("入院申请不存在");
        }
        if (!admission.getUserId().equals(userId)) {
            return Result.forbidden();
        }

        DepositPayment payment = new DepositPayment();
        payment.setOrderNo("DP" + System.currentTimeMillis() + (int)(Math.random() * 1000));
        payment.setAdmissionId(request.getAdmissionId());
        payment.setUserId(userId);
        payment.setPatientName(admission.getPatientName());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(0);
        payment.setRemark(request.getRemark());
        payment.setCreateBy(admission.getPatientName());

        DepositPayment saved = depositPaymentRepository.save(payment);

        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", saved.getOrderNo());
        result.put("amount", saved.getAmount());
        result.put("paymentUrl", "/payment/" + saved.getOrderNo());
        result.put("expireTime", LocalDateTime.now().plusMinutes(30));

        return Result.success("支付订单创建成功，请完成支付", result);
    }

    @Operation(summary = "模拟支付（测试用）")
    @PostMapping("/pay/{orderNo}")
    public Result<DepositPayment> simulatePayment(@PathVariable String orderNo) {
        return depositPaymentRepository.findByOrderNo(orderNo)
                .<Result<DepositPayment>>map(payment -> {
                    if (payment.getStatus() == 2) {
                        return Result.<DepositPayment>error("订单已支付");
                    }
                    payment.setStatus(2);
                    payment.setPaymentTime(LocalDateTime.now());
                    payment.setTransactionId("TX" + System.currentTimeMillis());
                    DepositPayment saved = depositPaymentRepository.save(payment);
                    
                    Admission admission = admissionRepository.findById(payment.getAdmissionId()).orElse(null);
                    if (admission != null) {
                        admission.setDepositAmount(admission.getDepositAmount().add(payment.getAmount()));
                        admissionRepository.save(admission);
                    }
                    
                    return Result.success("支付成功", saved);
                })
                .orElse(Result.<DepositPayment>notFound());
    }

    @Operation(summary = "取消支付")
    @PostMapping("/cancel/{orderNo}")
    public Result<DepositPayment> cancelPayment(@PathVariable String orderNo) {
        return depositPaymentRepository.findByOrderNo(orderNo)
                .<Result<DepositPayment>>map(payment -> {
                    if (payment.getStatus() >= 2) {
                        return Result.<DepositPayment>error("已支付订单无法取消");
                    }
                    payment.setStatus(4);
                    payment.setFailReason("用户取消");
                    DepositPayment saved = depositPaymentRepository.save(payment);
                    return Result.success("支付已取消", saved);
                })
                .orElse(Result.<DepositPayment>notFound());
    }

    @Operation(summary = "获取支付订单详情")
    @GetMapping("/{orderNo}")
    public Result<DepositPayment> getPaymentDetail(@PathVariable String orderNo) {
        return depositPaymentRepository.findByOrderNo(orderNo)
                .<Result<DepositPayment>>map(Result::success)
                .orElse(Result.<DepositPayment>notFound());
    }

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return jwtUtil.getUserIdFromToken(jwt);
        }
        return null;
    }

    @Data
    public static class DepositPaymentRequest {
        private Long admissionId;
        private BigDecimal amount;
        private String paymentMethod;
        private String remark;
    }
}
