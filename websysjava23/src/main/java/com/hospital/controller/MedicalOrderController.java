package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.MedicalOrder;
import com.hospital.service.MedicalOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医嘱Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/medical-order")
@CrossOrigin
@Tag(name = "医嘱管理", description = "医生开具医嘱、护士执行医嘱")
public class MedicalOrderController {

    @Autowired
    private MedicalOrderService medicalOrderService;

    @GetMapping
    @Operation(summary = "查询所有医嘱")
    public Result<List<MedicalOrder>> findAll() {
        return Result.success(medicalOrderService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询医嘱")
    public Result<MedicalOrder> findById(@PathVariable Long id) {
        return medicalOrderService.findById(id)
                .map(Result::success)
                .orElse(Result.error("医嘱不存在"));
    }

    @GetMapping("/hospitalization/{hospitalizationId}")
    @Operation(summary = "根据住院ID查询医嘱")
    public Result<List<MedicalOrder>> findByHospitalizationId(@PathVariable Long hospitalizationId) {
        return Result.success(medicalOrderService.findByHospitalizationId(hospitalizationId));
    }

    @GetMapping("/hospitalization/{hospitalizationId}/pending")
    @Operation(summary = "查询待执行医嘱")
    public Result<List<MedicalOrder>> findPendingOrders(@PathVariable Long hospitalizationId) {
        return Result.success(medicalOrderService.findPendingOrdersByHospitalizationId(hospitalizationId));
    }

    @PostMapping
    @Operation(summary = "开具医嘱")
    public Result<MedicalOrder> save(@RequestBody MedicalOrder order) {
        return Result.success(medicalOrderService.save(order));
    }

    @PutMapping
    @Operation(summary = "修改医嘱")
    public Result<MedicalOrder> update(@RequestBody MedicalOrder order) {
        return Result.success(medicalOrderService.save(order));
    }

    @PostMapping("/execute/{id}")
    @Operation(summary = "执行医嘱")
    public Result<MedicalOrder> executeOrder(@PathVariable Long id, @RequestParam Long nurseId) {
        MedicalOrder result = medicalOrderService.executeOrder(id, nurseId);
        if (result == null) {
            return Result.error("医嘱不存在");
        }
        return Result.success(result);
    }

    @PostMapping("/stop/{id}")
    @Operation(summary = "停止医嘱")
    public Result<MedicalOrder> stopOrder(@PathVariable Long id) {
        MedicalOrder result = medicalOrderService.stopOrder(id);
        if (result == null) {
            return Result.error("医嘱不存在");
        }
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除医嘱")
    public Result<Void> deleteById(@PathVariable Long id) {
        medicalOrderService.deleteById(id);
        return Result.success();
    }
}
