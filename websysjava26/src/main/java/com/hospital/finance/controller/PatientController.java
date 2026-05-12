package com.hospital.finance.controller;

import com.hospital.finance.entity.Patient;
import com.hospital.finance.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者信息控制器
 */
@RestController
@RequestMapping("/api/patients")
@Tag(name = "患者管理", description = "患者信息的增删改查接口")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    @Operation(summary = "新增患者", description = "创建新的患者信息")
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.save(patient));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新患者", description = "更新患者信息")
    public ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient patient) {
        patient.setId(id);
        return ResponseEntity.ok(patientService.update(patient));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询患者", description = "根据ID查询患者信息")
    public ResponseEntity<Patient> findById(@PathVariable Long id) {
        return patientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "查询所有患者", description = "获取所有患者列表")
    public ResponseEntity<List<Patient>> findAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/no/{patientNo}")
    @Operation(summary = "根据患者编号查询", description = "根据患者编号查询患者信息")
    public ResponseEntity<Patient> findByPatientNo(@PathVariable String patientNo) {
        return ResponseEntity.ok(patientService.findByPatientNo(patientNo));
    }

    @GetMapping("/search")
    @Operation(summary = "根据姓名搜索", description = "根据姓名模糊搜索患者")
    public ResponseEntity<List<Patient>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(patientService.findByName(name));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除患者", description = "根据ID删除患者信息")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/insurance")
    @Operation(summary = "查询有医保患者", description = "获取所有有医保的患者列表")
    public ResponseEntity<List<Patient>> findHasInsurance() {
        return ResponseEntity.ok(patientService.findHasInsurance());
    }
}