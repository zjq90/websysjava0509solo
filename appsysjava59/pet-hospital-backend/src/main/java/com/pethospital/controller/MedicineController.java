package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Medicine;
import com.pethospital.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicine")
@Tag(name = "药品管理", description = "药品查询、库存管理等接口")
@CrossOrigin(origins = "*")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping
    @Operation(summary = "获取所有药品列表")
    public Result<List<Medicine>> getAllMedicines() {
        return Result.success(medicineService.getAllMedicines());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "按分类获取药品")
    public Result<List<Medicine>> getMedicinesByCategory(@PathVariable String category) {
        return Result.success(medicineService.getMedicinesByCategory(category));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索药品（自动联想）")
    public Result<List<Medicine>> searchMedicine(@RequestParam String keyword) {
        return Result.success(medicineService.searchMedicine(keyword));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取药品详情")
    public Result<Medicine> getMedicineById(@PathVariable Long id) {
        Optional<Medicine> medicine = medicineService.getMedicineById(id);
        return medicine.map(Result::success).orElseGet(() -> Result.error("药品不存在"));
    }

    @PostMapping
    @Operation(summary = "新增药品")
    public Result<Medicine> createMedicine(@RequestBody Medicine medicine) {
        return Result.success(medicineService.saveMedicine(medicine));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新药品信息")
    public Result<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        medicine.setId(id);
        return Result.success(medicineService.saveMedicine(medicine));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除药品")
    public Result<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return Result.success();
    }

    @PostMapping("/check-stock")
    @Operation(summary = "检查药品库存")
    public Result<Boolean> checkStock(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        int quantity = Integer.parseInt(params.get("quantity").toString());
        return Result.success(medicineService.checkStock(id, quantity));
    }
}
