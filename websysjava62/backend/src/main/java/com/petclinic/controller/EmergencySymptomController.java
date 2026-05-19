package com.petclinic.controller;

import com.petclinic.common.Result;
import com.petclinic.entity.EmergencySymptom;
import com.petclinic.service.EmergencySymptomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 紧急症状库Controller
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/emergency-symptoms")
@Tag(name = "紧急症状库管理", description = "兽医专家定期修订的紧急症状库")
public class EmergencySymptomController {

    @Autowired
    private EmergencySymptomService emergencySymptomService;

    @GetMapping
    @Operation(summary = "查询所有紧急症状")
    public Result<List<EmergencySymptom>> findAll() {
        return Result.success(emergencySymptomService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询紧急症状")
    public Result<EmergencySymptom> findById(@PathVariable Long id) {
        Optional<EmergencySymptom> symptom = emergencySymptomService.findById(id);
        return symptom.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询紧急症状")
    public Result<List<EmergencySymptom>> findByStatus(@PathVariable String status) {
        return Result.success(emergencySymptomService.findByStatus(status));
    }

    @GetMapping("/severity/{level}")
    @Operation(summary = "根据严重程度查询紧急症状")
    public Result<List<EmergencySymptom>> findBySeverityLevel(@PathVariable String level) {
        return Result.success(emergencySymptomService.findBySeverityLevel(level));
    }

    @PostMapping
    @Operation(summary = "新增紧急症状")
    public Result<EmergencySymptom> save(@RequestBody EmergencySymptom symptom) {
        return Result.success(emergencySymptomService.save(symptom));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新紧急症状")
    public Result<EmergencySymptom> update(@PathVariable Long id, @RequestBody EmergencySymptom symptom) {
        EmergencySymptom updated = emergencySymptomService.update(id, symptom);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除紧急症状")
    public Result<Void> deleteById(@PathVariable Long id) {
        emergencySymptomService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/search")
    @Operation(summary = "根据症状名称搜索")
    public Result<List<EmergencySymptom>> search(@RequestParam String name) {
        return Result.success(emergencySymptomService.searchBySymptomName(name));
    }
}
