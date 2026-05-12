package com.lims.controller;

import com.lims.entity.TestApplication;
import com.lims.service.TestApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验检查申请控制器
 * 实现申请管理、任务确认与分配功能
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/applications")
@Tag(name = "检验检查申请管理", description = "检验检查申请的增删改查、确认、分配功能")
public class TestApplicationController {

    @Autowired
    private TestApplicationService testApplicationService;

    @GetMapping
    @Operation(summary = "查询所有申请")
    public List<TestApplication> findAll() {
        return testApplicationService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询申请")
    public ResponseEntity<TestApplication> findById(
            @Parameter(description = "申请ID") @PathVariable Long id) {
        return testApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询申请")
    public List<TestApplication> findByStatus(
            @Parameter(description = "申请状态") @PathVariable String status) {
        return testApplicationService.findByStatus(status);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "根据执行科室ID查询申请")
    public List<TestApplication> findByExecuteDepartmentId(
            @Parameter(description = "执行科室ID") @PathVariable Long departmentId) {
        return testApplicationService.findByExecuteDepartmentId(departmentId);
    }

    @GetMapping("/technician/{technicianId}")
    @Operation(summary = "根据分配的技师ID查询申请")
    public List<TestApplication> findByAssignedTechnicianId(
            @Parameter(description = "技师ID") @PathVariable Long technicianId) {
        return testApplicationService.findByAssignedTechnicianId(technicianId);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "根据患者ID查询申请")
    public List<TestApplication> findByPatientId(
            @Parameter(description = "患者ID") @PathVariable Long patientId) {
        return testApplicationService.findByPatientId(patientId);
    }

    @PostMapping
    @Operation(summary = "新增申请")
    public TestApplication save(@RequestBody TestApplication testApplication) {
        return testApplicationService.save(testApplication);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新申请")
    public ResponseEntity<TestApplication> update(
            @Parameter(description = "申请ID") @PathVariable Long id,
            @RequestBody TestApplication testApplication) {
        return testApplicationService.findById(id)
                .map(existingApp -> {
                    testApplication.setId(id);
                    return ResponseEntity.ok(testApplicationService.update(testApplication));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "确认申请")
    public ResponseEntity<TestApplication> confirmApplication(
            @Parameter(description = "申请ID") @PathVariable Long id,
            @Parameter(description = "确认人ID") @RequestParam Long confirmedBy) {
        TestApplication result = testApplicationService.confirmApplication(id, confirmedBy);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "分配任务给技师")
    public ResponseEntity<TestApplication> assignTechnician(
            @Parameter(description = "申请ID") @PathVariable Long id,
            @Parameter(description = "技师ID") @RequestParam Long technicianId) {
        TestApplication result = testApplicationService.assignTechnician(id, technicianId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "开始处理")
    public ResponseEntity<TestApplication> startProcessing(
            @Parameter(description = "申请ID") @PathVariable Long id) {
        TestApplication result = testApplicationService.startProcessing(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成检验/检查")
    public ResponseEntity<TestApplication> completeTest(
            @Parameter(description = "申请ID") @PathVariable Long id) {
        TestApplication result = testApplicationService.completeTest(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除申请")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "申请ID") @PathVariable Long id) {
        if (testApplicationService.findById(id).isPresent()) {
            testApplicationService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
