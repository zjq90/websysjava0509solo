package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.MedicalRecord;
import com.hospital.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 病历Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/medical-record")
@CrossOrigin
@Tag(name = "病历管理", description = "住院病历的书写和管理")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    @Operation(summary = "查询所有病历")
    public Result<List<MedicalRecord>> findAll() {
        return Result.success(medicalRecordService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询病历")
    public Result<MedicalRecord> findById(@PathVariable Long id) {
        return medicalRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error("病历不存在"));
    }

    @GetMapping("/hospitalization/{hospitalizationId}")
    @Operation(summary = "根据住院ID查询病历")
    public Result<List<MedicalRecord>> findByHospitalizationId(@PathVariable Long hospitalizationId) {
        return Result.success(medicalRecordService.findByHospitalizationId(hospitalizationId));
    }

    @GetMapping("/hospitalization/{hospitalizationId}/type/{recordType}")
    @Operation(summary = "根据住院ID和病历类型查询")
    public Result<List<MedicalRecord>> findByHospitalizationIdAndRecordType(
            @PathVariable Long hospitalizationId,
            @PathVariable String recordType) {
        return Result.success(medicalRecordService.findByHospitalizationIdAndRecordType(hospitalizationId, recordType));
    }

    @PostMapping
    @Operation(summary = "新增病历")
    public Result<MedicalRecord> save(@RequestBody MedicalRecord record) {
        return Result.success(medicalRecordService.save(record));
    }

    @PutMapping
    @Operation(summary = "修改病历")
    public Result<MedicalRecord> update(@RequestBody MedicalRecord record) {
        return Result.success(medicalRecordService.save(record));
    }

    @PostMapping("/submit/{id}")
    @Operation(summary = "提交病历")
    public Result<MedicalRecord> submit(@PathVariable Long id) {
        MedicalRecord result = medicalRecordService.submit(id);
        if (result == null) {
            return Result.error("病历不存在");
        }
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除病历")
    public Result<Void> deleteById(@PathVariable Long id) {
        medicalRecordService.deleteById(id);
        return Result.success();
    }
}
