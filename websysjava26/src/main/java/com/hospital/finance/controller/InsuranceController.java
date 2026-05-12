package com.hospital.finance.controller;

import com.hospital.finance.entity.InsuranceSettlement;
import com.hospital.finance.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 医保结算控制器
 */
@RestController
@RequestMapping("/api/insurance")
@Tag(name = "医保结算管理", description = "医保结算的创建、完成、撤销等接口")
@CrossOrigin(origins = "*")
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @PostMapping("/settlements")
    @Operation(summary = "创建医保结算", description = "创建新的医保结算记录")
    public ResponseEntity<InsuranceSettlement> create(@RequestBody InsuranceSettlement settlement) {
        return ResponseEntity.ok(insuranceService.createSettlement(settlement));
    }

    @PostMapping("/settlements/{id}/complete")
    @Operation(summary = "完成医保结算", description = "完成医保结算并记录医保系统返回信息")
    public ResponseEntity<InsuranceSettlement> complete(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String operator = request.get("operator");
        String insuranceResponse = request.get("insuranceResponse");
        return ResponseEntity.ok(insuranceService.completeSettlement(id, operator, insuranceResponse));
    }

    @PostMapping("/settlements/{id}/cancel")
    @Operation(summary = "撤销医保结算", description = "撤销已完成的医保结算")
    public ResponseEntity<InsuranceSettlement> cancel(
            @PathVariable Long id,
            @RequestParam(required = false) String remark) {
        return ResponseEntity.ok(insuranceService.cancelSettlement(id, remark));
    }

    @GetMapping("/settlements/{id}")
    @Operation(summary = "查询结算记录", description = "根据ID查询医保结算记录")
    public ResponseEntity<InsuranceSettlement> findById(@PathVariable Long id) {
        return insuranceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/settlements")
    @Operation(summary = "查询所有结算记录", description = "获取所有医保结算记录列表")
    public ResponseEntity<List<InsuranceSettlement>> findAll() {
        return ResponseEntity.ok(insuranceService.findAll());
    }

    @GetMapping("/settlements/patient/{patientId}")
    @Operation(summary = "根据患者查询", description = "根据患者ID查询医保结算记录")
    public ResponseEntity<List<InsuranceSettlement>> findByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(insuranceService.findByPatientId(patientId));
    }

    @GetMapping("/settlements/status/{status}")
    @Operation(summary = "根据状态查询", description = "根据状态查询医保结算记录")
    public ResponseEntity<List<InsuranceSettlement>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(insuranceService.findByStatus(status));
    }
}