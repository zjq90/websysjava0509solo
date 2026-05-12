package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.FeeRecord;
import com.hospital.service.FeeRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用记录Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/fee-record")
@CrossOrigin
@Tag(name = "费用管理", description = "患者住院费用的记录和查询")
public class FeeRecordController {

    @Autowired
    private FeeRecordService feeRecordService;

    @GetMapping
    @Operation(summary = "查询所有费用记录")
    public Result<List<FeeRecord>> findAll() {
        return Result.success(feeRecordService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询费用记录")
    public Result<FeeRecord> findById(@PathVariable Long id) {
        return feeRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error("费用记录不存在"));
    }

    @GetMapping("/hospitalization/{hospitalizationId}")
    @Operation(summary = "根据住院ID查询费用记录")
    public Result<List<FeeRecord>> findByHospitalizationId(@PathVariable Long hospitalizationId) {
        return Result.success(feeRecordService.findByHospitalizationId(hospitalizationId));
    }

    @GetMapping("/hospitalization/{hospitalizationId}/total")
    @Operation(summary = "查询总费用")
    public Result<BigDecimal> sumAmountByHospitalizationId(@PathVariable Long hospitalizationId) {
        return Result.success(feeRecordService.sumAmountByHospitalizationId(hospitalizationId));
    }

    @GetMapping("/hospitalization/{hospitalizationId}/paid")
    @Operation(summary = "查询已缴费用")
    public Result<BigDecimal> sumPaidAmountByHospitalizationId(@PathVariable Long hospitalizationId) {
        return Result.success(feeRecordService.sumPaidAmountByHospitalizationId(hospitalizationId));
    }

    @PostMapping
    @Operation(summary = "新增费用记录")
    public Result<FeeRecord> save(@RequestBody FeeRecord feeRecord) {
        return Result.success(feeRecordService.save(feeRecord));
    }

    @PutMapping
    @Operation(summary = "修改费用记录")
    public Result<FeeRecord> update(@RequestBody FeeRecord feeRecord) {
        return Result.success(feeRecordService.save(feeRecord));
    }

    @PostMapping("/pay/{id}")
    @Operation(summary = "缴费")
    public Result<FeeRecord> pay(@PathVariable Long id) {
        FeeRecord result = feeRecordService.pay(id);
        if (result == null) {
            return Result.error("费用记录不存在");
        }
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除费用记录")
    public Result<Void> deleteById(@PathVariable Long id) {
        feeRecordService.deleteById(id);
        return Result.success();
    }
}
