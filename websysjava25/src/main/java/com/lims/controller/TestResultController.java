package com.lims.controller;

import com.lims.entity.TestResult;
import com.lims.service.TestResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验检查结果控制器
 * 实现技师工作站功能
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/results")
@Tag(name = "检验检查结果管理", description = "检验检查结果的录入、查询功能")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @GetMapping
    @Operation(summary = "查询所有结果")
    public List<TestResult> findAll() {
        return testResultService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询结果")
    public ResponseEntity<TestResult> findById(
            @Parameter(description = "结果ID") @PathVariable Long id) {
        return testResultService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/application/{applicationId}")
    @Operation(summary = "根据申请ID查询结果")
    public List<TestResult> findByApplicationId(
            @Parameter(description = "申请ID") @PathVariable Long applicationId) {
        return testResultService.findByApplicationId(applicationId);
    }

    @GetMapping("/technician/{technicianId}")
    @Operation(summary = "根据技师ID查询结果")
    public List<TestResult> findByTechnicianId(
            @Parameter(description = "技师ID") @PathVariable Long technicianId) {
        return testResultService.findByTechnicianId(technicianId);
    }

    @PostMapping
    @Operation(summary = "新增检验结果")
    public TestResult save(@RequestBody TestResult testResult) {
        return testResultService.save(testResult);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新检验结果")
    public ResponseEntity<TestResult> update(
            @Parameter(description = "结果ID") @PathVariable Long id,
            @RequestBody TestResult testResult) {
        return testResultService.findById(id)
                .map(existingResult -> {
                    testResult.setId(id);
                    return ResponseEntity.ok(testResultService.update(testResult));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "开始处理检验/检查")
    public ResponseEntity<TestResult> startTest(
            @Parameter(description = "结果ID") @PathVariable Long id) {
        TestResult result = testResultService.startTest(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成检验/检查并录入结果")
    public ResponseEntity<TestResult> completeTest(
            @Parameter(description = "结果ID") @PathVariable Long id,
            @Parameter(description = "结果值") @RequestParam(required = false) String resultValue,
            @Parameter(description = "结果描述") @RequestParam(required = false) String resultDescription,
            @Parameter(description = "异常标识") @RequestParam(required = false, defaultValue = "NORMAL") String abnormalFlag) {
        TestResult result = testResultService.completeTest(id, resultValue, resultDescription, abnormalFlag);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/collect-device")
    @Operation(summary = "从设备自动采集结果")
    public ResponseEntity<TestResult> collectFromDevice(
            @Parameter(description = "结果ID") @PathVariable Long id,
            @Parameter(description = "设备编号") @RequestParam String deviceCode,
            @Parameter(description = "结果值") @RequestParam String resultValue) {
        TestResult result = testResultService.collectFromDevice(id, deviceCode, resultValue);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除结果")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "结果ID") @PathVariable Long id) {
        if (testResultService.findById(id).isPresent()) {
            testResultService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
