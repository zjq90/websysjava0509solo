package com.hospital.finance.controller;

import com.hospital.finance.entity.OutpatientCharge;
import com.hospital.finance.entity.OutpatientChargeDetail;
import com.hospital.finance.service.OutpatientChargeService;
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
 * 门诊收费控制器
 */
@RestController
@RequestMapping("/api/outpatient-charges")
@Tag(name = "门诊收费管理", description = "门诊收费的记账、结算、退费等接口")
@CrossOrigin(origins = "*")
public class OutpatientChargeController {

    @Autowired
    private OutpatientChargeService chargeService;

    @PostMapping
    @Operation(summary = "创建门诊收费", description = "创建门诊收费记录及明细")
    public ResponseEntity<OutpatientCharge> create(@RequestBody Map<String, Object> request) {
        OutpatientCharge charge = new OutpatientCharge();
        charge.setPatientId(Long.valueOf(request.get("patientId").toString()));
        charge.setPatientName((String) request.get("patientName"));
        charge.setDepartment((String) request.get("department"));
        charge.setDoctorName((String) request.get("doctorName"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> detailList = (List<Map<String, Object>>) request.get("details");
        List<OutpatientChargeDetail> details = detailList.stream().map(map -> {
            OutpatientChargeDetail detail = new OutpatientChargeDetail();
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

    @PostMapping("/{id}/settle")
    @Operation(summary = "收费结算", description = "完成门诊收费结算")
    public ResponseEntity<OutpatientCharge> settle(
            @PathVariable Long id,
            @RequestParam String paymentMethod,
            @RequestParam String operator) {
        return ResponseEntity.ok(chargeService.settleCharge(id, paymentMethod, operator));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "退费", description = "门诊收费退费")
    public ResponseEntity<OutpatientCharge> refund(
            @PathVariable Long id,
            @RequestParam(required = false) String remark) {
        return ResponseEntity.ok(chargeService.refundCharge(id, remark));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询收费记录", description = "根据ID查询门诊收费记录")
    public ResponseEntity<OutpatientCharge> findById(@PathVariable Long id) {
        return chargeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "查询所有收费记录", description = "获取所有门诊收费记录列表")
    public ResponseEntity<List<OutpatientCharge>> findAll() {
        return ResponseEntity.ok(chargeService.findAll());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "根据患者查询", description = "根据患者ID查询门诊收费记录")
    public ResponseEntity<List<OutpatientCharge>> findByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(chargeService.findByPatientId(patientId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询", description = "根据状态查询门诊收费记录")
    public ResponseEntity<List<OutpatientCharge>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(chargeService.findByStatus(status));
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "查询收费明细", description = "查询门诊收费明细列表")
    public ResponseEntity<List<OutpatientChargeDetail>> findDetails(@PathVariable Long id) {
        return ResponseEntity.ok(chargeService.findDetailsByChargeId(id));
    }

    @GetMapping("/time-range")
    @Operation(summary = "按时间范围查询", description = "查询指定时间范围内的门诊收费记录")
    public ResponseEntity<List<OutpatientCharge>> findByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(chargeService.findByTimeRange(startTime, endTime));
    }
}