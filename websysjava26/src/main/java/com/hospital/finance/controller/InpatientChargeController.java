package com.hospital.finance.controller;

import com.hospital.finance.entity.InpatientCharge;
import com.hospital.finance.entity.InpatientChargeDetail;
import com.hospital.finance.service.InpatientChargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 住院收费控制器
 */
@RestController
@RequestMapping("/api/inpatient-charges")
@Tag(name = "住院收费管理", description = "住院收费的记账、结算、退费等接口")
@CrossOrigin(origins = "*")
public class InpatientChargeController {

    @Autowired
    private InpatientChargeService chargeService;

    @PostMapping
    @Operation(summary = "创建住院收费", description = "创建住院收费记录及明细")
    public ResponseEntity<InpatientCharge> create(@RequestBody Map<String, Object> request) {
        InpatientCharge charge = new InpatientCharge();
        charge.setPatientId(Long.valueOf(request.get("patientId").toString()));
        charge.setPatientName((String) request.get("patientName"));
        charge.setAdmissionNo((String) request.get("admissionNo"));
        charge.setDepartment((String) request.get("department"));
        charge.setBedNo((String) request.get("bedNo"));
        charge.setDoctorName((String) request.get("doctorName"));
        charge.setDepositAmount(new java.math.BigDecimal(request.get("depositAmount").toString()));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> detailList = (List<Map<String, Object>>) request.get("details");
        List<InpatientChargeDetail> details = detailList.stream().map(map -> {
            InpatientChargeDetail detail = new InpatientChargeDetail();
            detail.setItemType((String) map.get("itemType"));
            detail.setItemCode((String) map.get("itemCode"));
            detail.setItemName((String) map.get("itemName"));
            detail.setSpecification((String) map.get("specification"));
            detail.setUnit((String) map.get("unit"));
            detail.setQuantity(Integer.valueOf(map.get("quantity").toString()));
            detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
            detail.setAmount(new java.math.BigDecimal(map.get("amount").toString()));
            detail.setIsInsurance((Boolean) map.get("isInsurance"));
            if (map.get("insuranceRatio") != null) {
                detail.setInsuranceRatio(Double.valueOf(map.get("insuranceRatio").toString()));
            }
            return detail;
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(chargeService.createCharge(charge, details));
    }

    @PostMapping("/{id}/details")
    @Operation(summary = "添加费用明细", description = "住院期间添加费用明细")
    public ResponseEntity<InpatientChargeDetail> addDetail(
            @PathVariable Long id,
            @RequestBody InpatientChargeDetail detail) {
        return ResponseEntity.ok(chargeService.addDetail(id, detail));
    }

    @PostMapping("/{id}/settle")
    @Operation(summary = "出院结算", description = "完成住院收费结算")
    public ResponseEntity<InpatientCharge> settle(
            @PathVariable Long id,
            @RequestParam String paymentMethod,
            @RequestParam String operator) {
        return ResponseEntity.ok(chargeService.settleCharge(id, paymentMethod, operator));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "退费", description = "住院收费退费")
    public ResponseEntity<InpatientCharge> refund(
            @PathVariable Long id,
            @RequestParam(required = false) String remark) {
        return ResponseEntity.ok(chargeService.refundCharge(id, remark));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询收费记录", description = "根据ID查询住院收费记录")
    public ResponseEntity<InpatientCharge> findById(@PathVariable Long id) {
        return chargeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "查询所有收费记录", description = "获取所有住院收费记录列表")
    public ResponseEntity<List<InpatientCharge>> findAll() {
        return ResponseEntity.ok(chargeService.findAll());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "根据患者查询", description = "根据患者ID查询住院收费记录")
    public ResponseEntity<List<InpatientCharge>> findByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(chargeService.findByPatientId(patientId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询", description = "根据状态查询住院收费记录")
    public ResponseEntity<List<InpatientCharge>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(chargeService.findByStatus(status));
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "查询收费明细", description = "查询住院收费明细列表")
    public ResponseEntity<List<InpatientChargeDetail>> findDetails(@PathVariable Long id) {
        return ResponseEntity.ok(chargeService.findDetailsByChargeId(id));
    }

    @GetMapping("/time-range")
    @Operation(summary = "按时间范围查询", description = "查询指定时间范围内的住院收费记录")
    public ResponseEntity<List<InpatientCharge>> findByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(chargeService.findByTimeRange(startTime, endTime));
    }
}