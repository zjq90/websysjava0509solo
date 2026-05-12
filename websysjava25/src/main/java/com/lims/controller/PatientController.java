package com.lims.controller;

import com.lims.entity.Patient;
import com.lims.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者控制器
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/patients")
@Tag(name = "患者管理", description = "患者的增删改查接口")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    @Operation(summary = "查询所有患者")
    public List<Patient> findAll() {
        return patientService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询患者")
    public ResponseEntity<Patient> findById(
            @Parameter(description = "患者ID") @PathVariable Long id) {
        return patientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patientNo/{patientNo}")
    @Operation(summary = "根据患者编号查询")
    public Patient findByPatientNo(
            @Parameter(description = "患者编号") @PathVariable String patientNo) {
        return patientService.findByPatientNo(patientNo);
    }

    @GetMapping("/search/{name}")
    @Operation(summary = "根据姓名模糊查询")
    public List<Patient> findByPatientNameContaining(
            @Parameter(description = "患者姓名") @PathVariable String name) {
        return patientService.findByPatientNameContaining(name);
    }

    @PostMapping
    @Operation(summary = "新增患者")
    public Patient save(@RequestBody Patient patient) {
        return patientService.save(patient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新患者")
    public ResponseEntity<Patient> update(
            @Parameter(description = "患者ID") @PathVariable Long id,
            @RequestBody Patient patient) {
        return patientService.findById(id)
                .map(existingPatient -> {
                    patient.setId(id);
                    return ResponseEntity.ok(patientService.update(patient));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除患者")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "患者ID") @PathVariable Long id) {
        if (patientService.findById(id).isPresent()) {
            patientService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
