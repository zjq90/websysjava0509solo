package com.ops.controller;

import com.ops.common.Result;
import com.ops.entity.Technician;
import com.ops.service.TechnicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 装维人员管理控制器
 * 
 * @author ops-admin
 */
@RestController
@RequestMapping("/api/technicians")
@Tag(name = "装维人员管理", description = "装维人员的增删改查操作")
@CrossOrigin(origins = "*")
public class TechnicianController {

    @Autowired
    private TechnicianService technicianService;

    @GetMapping
    @Operation(summary = "获取所有装维人员")
    public Result<List<Technician>> getAllTechnicians() {
        return Result.success(technicianService.getAllTechnicians());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取装维人员")
    public Result<Technician> getTechnicianById(@PathVariable Long id) {
        return technicianService.getTechnicianById(id)
                .map(Result::success)
                .orElse(Result.error("装维人员不存在"));
    }

    @PostMapping
    @Operation(summary = "创建装维人员")
    public Result<Technician> createTechnician(@RequestBody Technician technician) {
        return Result.success(technicianService.createTechnician(technician));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新装维人员")
    public Result<Technician> updateTechnician(@PathVariable Long id, @RequestBody Technician technician) {
        technician.setId(id);
        return Result.success(technicianService.updateTechnician(technician));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除装维人员")
    public Result<Void> deleteTechnician(@PathVariable Long id) {
        technicianService.deleteTechnician(id);
        return Result.success();
    }
}
