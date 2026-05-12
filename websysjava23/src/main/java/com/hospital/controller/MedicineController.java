package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.Medicine;
import com.hospital.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 药品Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/medicine")
@CrossOrigin
@Tag(name = "药品管理", description = "药品信息的增删改查和库存管理")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping
    @Operation(summary = "查询所有药品")
    public Result<List<Medicine>> findAll() {
        return Result.success(medicineService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询药品")
    public Result<Medicine> findById(@PathVariable Long id) {
        return medicineService.findById(id)
                .map(Result::success)
                .orElse(Result.error("药品不存在"));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索药品")
    public Result<List<Medicine>> search(@RequestParam String keyword) {
        return Result.success(medicineService.search(keyword));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类查询药品")
    public Result<List<Medicine>> findByCategory(@PathVariable String category) {
        return Result.success(medicineService.findByCategory(category));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "查询库存不足药品")
    public Result<List<Medicine>> findLowStockMedicines() {
        return Result.success(medicineService.findLowStockMedicines());
    }

    @PostMapping
    @Operation(summary = "新增药品")
    public Result<Medicine> save(@RequestBody Medicine medicine) {
        return Result.success(medicineService.save(medicine));
    }

    @PutMapping
    @Operation(summary = "修改药品")
    public Result<Medicine> update(@RequestBody Medicine medicine) {
        return Result.success(medicineService.save(medicine));
    }

    @PutMapping("/stock/{id}")
    @Operation(summary = "更新药品库存")
    public Result<Medicine> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        Medicine result = medicineService.updateStock(id, quantity);
        if (result == null) {
            return Result.error("药品不存在");
        }
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除药品")
    public Result<Void> deleteById(@PathVariable Long id) {
        medicineService.deleteById(id);
        return Result.success();
    }
}
