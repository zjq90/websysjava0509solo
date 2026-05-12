package com.lims.controller;

import com.lims.entity.Department;
import com.lims.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室控制器
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/departments")
@Tag(name = "科室管理", description = "科室的增删改查接口")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "查询所有科室")
    public List<Department> findAll() {
        return departmentService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询科室")
    public ResponseEntity<Department> findById(
            @Parameter(description = "科室ID") @PathVariable Long id) {
        return departmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{deptCode}")
    @Operation(summary = "根据科室编号查询")
    public Department findByDeptCode(
            @Parameter(description = "科室编号") @PathVariable String deptCode) {
        return departmentService.findByDeptCode(deptCode);
    }

    @GetMapping("/type/{deptType}")
    @Operation(summary = "根据科室类型查询")
    public List<Department> findByDeptType(
            @Parameter(description = "科室类型") @PathVariable String deptType) {
        return departmentService.findByDeptType(deptType);
    }

    @PostMapping
    @Operation(summary = "新增科室")
    public Department save(@RequestBody Department department) {
        return departmentService.save(department);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新科室")
    public ResponseEntity<Department> update(
            @Parameter(description = "科室ID") @PathVariable Long id,
            @RequestBody Department department) {
        return departmentService.findById(id)
                .map(existingDept -> {
                    department.setId(id);
                    return ResponseEntity.ok(departmentService.update(department));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除科室")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "科室ID") @PathVariable Long id) {
        if (departmentService.findById(id).isPresent()) {
            departmentService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
