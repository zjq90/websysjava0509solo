package com.lims.controller;

import com.lims.entity.TestReport;
import com.lims.service.TestReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验检查报告控制器
 * 实现报告审核与发布、患者查询功能
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/reports")
@Tag(name = "检验检查报告管理", description = "检验检查报告的生成、审核、发布、查询功能")
public class TestReportController {

    @Autowired
    private TestReportService testReportService;

    @GetMapping
    @Operation(summary = "查询所有报告")
    public List<TestReport> findAll() {
        return testReportService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询报告")
    public ResponseEntity<TestReport> findById(
            @Parameter(description = "报告ID") @PathVariable Long id) {
        return testReportService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询报告")
    public List<TestReport> findByStatus(
            @Parameter(description = "报告状态") @PathVariable String status) {
        return testReportService.findByStatus(status);
    }

    @GetMapping("/application/{applicationId}")
    @Operation(summary = "根据申请ID查询报告")
    public List<TestReport> findByApplicationId(
            @Parameter(description = "申请ID") @PathVariable Long applicationId) {
        return testReportService.findByApplicationId(applicationId);
    }

    @PostMapping
    @Operation(summary = "新增报告")
    public TestReport save(@RequestBody TestReport testReport) {
        return testReportService.save(testReport);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新报告")
    public ResponseEntity<TestReport> update(
            @Parameter(description = "报告ID") @PathVariable Long id,
            @RequestBody TestReport testReport) {
        return testReportService.findById(id)
                .map(existingReport -> {
                    testReport.setId(id);
                    return ResponseEntity.ok(testReportService.update(testReport));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/submit-audit")
    @Operation(summary = "提交一级审核")
    public ResponseEntity<TestReport> submitFirstAudit(
            @Parameter(description = "报告ID") @PathVariable Long id) {
        TestReport result = testReportService.submitFirstAudit(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/first-audit-pass")
    @Operation(summary = "一级审核通过")
    public ResponseEntity<TestReport> firstAuditPass(
            @Parameter(description = "报告ID") @PathVariable Long id,
            @Parameter(description = "审核人ID") @RequestParam Long auditorId,
            @Parameter(description = "审核意见") @RequestParam(required = false, defaultValue = "审核通过") String opinion) {
        TestReport result = testReportService.firstAuditPass(id, auditorId, opinion);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/first-audit-reject")
    @Operation(summary = "一级审核驳回")
    public ResponseEntity<TestReport> firstAuditReject(
            @Parameter(description = "报告ID") @PathVariable Long id,
            @Parameter(description = "审核人ID") @RequestParam Long auditorId,
            @Parameter(description = "审核意见") @RequestParam String opinion) {
        TestReport result = testReportService.firstAuditReject(id, auditorId, opinion);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/second-audit-publish")
    @Operation(summary = "二级审核通过并发布")
    public ResponseEntity<TestReport> secondAuditPassAndPublish(
            @Parameter(description = "报告ID") @PathVariable Long id,
            @Parameter(description = "审核人ID") @RequestParam Long auditorId,
            @Parameter(description = "审核意见") @RequestParam(required = false, defaultValue = "审核通过，同意发布") String opinion) {
        TestReport result = testReportService.secondAuditPassAndPublish(id, auditorId, opinion);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/patient-view")
    @Operation(summary = "患者查看报告")
    public ResponseEntity<TestReport> patientViewReport(
            @Parameter(description = "报告ID") @PathVariable Long id) {
        TestReport result = testReportService.patientViewReport(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除报告")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "报告ID") @PathVariable Long id) {
        if (testReportService.findById(id).isPresent()) {
            testReportService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
