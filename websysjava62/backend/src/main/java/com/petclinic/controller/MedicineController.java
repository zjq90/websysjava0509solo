package com.petclinic.controller;

import com.petclinic.annotation.NeedReview;
import com.petclinic.common.Result;
import com.petclinic.dto.ReviewResult;
import com.petclinic.entity.Medicine;
import com.petclinic.entity.MedicineCategory;
import com.petclinic.service.MedicineService;
import com.petclinic.service.ReviewRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 药品管理Controller
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/medicine")
@Tag(name = "药品管理", description = "药品分类和药品信息管理")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private ReviewRuleService reviewRuleService;

    // ==================== 药品分类管理 ====================

    @GetMapping("/categories")
    @Operation(summary = "查询所有药品分类")
    public Result<List<MedicineCategory>> findAllCategories() {
        return Result.success(medicineService.findAllCategories());
    }

    @GetMapping("/categories/{id}")
    @Operation(summary = "根据ID查询药品分类")
    public Result<MedicineCategory> findCategoryById(@PathVariable Long id) {
        Optional<MedicineCategory> category = medicineService.findCategoryById(id);
        return category.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping("/categories")
    @Operation(summary = "新增药品分类")
    public Result<MedicineCategory> saveCategory(@RequestBody MedicineCategory category) {
        return Result.success(medicineService.saveCategory(category));
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "更新药品分类")
    public Result<MedicineCategory> updateCategory(@PathVariable Long id, @RequestBody MedicineCategory category) {
        MedicineCategory updated = medicineService.updateCategory(id, category);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "删除药品分类")
    public Result<Void> deleteCategoryById(@PathVariable Long id) {
        medicineService.deleteCategoryById(id);
        return Result.success();
    }

    @GetMapping("/categories/status/{status}")
    @Operation(summary = "根据状态查询药品分类")
    public Result<List<MedicineCategory>> findCategoriesByStatus(@PathVariable String status) {
        return Result.success(medicineService.findCategoriesByStatus(status));
    }

    // ==================== 药品管理 ====================

    @GetMapping("/list")
    @Operation(summary = "查询所有药品")
    public Result<List<Medicine>> findAllMedicines() {
        return Result.success(medicineService.findAllMedicines());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询药品")
    public Result<Medicine> findMedicineById(@PathVariable Long id) {
        Optional<Medicine> medicine = medicineService.findMedicineById(id);
        return medicine.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping
    @Operation(summary = "新增药品")
    @NeedReview
    public Result<Medicine> saveMedicine(@RequestBody Medicine medicine) {
        return Result.success(medicineService.saveMedicine(medicine));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新药品")
    @NeedReview
    public Result<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        Medicine updated = medicineService.updateMedicine(id, medicine);
        return updated != null ? Result.success(updated) : Result.error("更新失败");
    }

    @PostMapping("/review-preview")
    @Operation(summary = "预览药品内容审核结果（不实际保存）")
    public Result<ReviewResult> previewMedicineReview(@RequestBody Medicine medicine) {
        StringBuilder content = new StringBuilder();
        if (medicine.getMedicineName() != null) content.append(medicine.getMedicineName()).append(" ");
        if (medicine.getDescription() != null) content.append(medicine.getDescription()).append(" ");
        if (medicine.getManufacturer() != null) content.append(medicine.getManufacturer()).append(" ");
        if (medicine.getUsageDosage() != null) content.append(medicine.getUsageDosage()).append(" ");
        if (medicine.getAttention() != null) content.append(medicine.getAttention()).append(" ");
        
        ReviewResult result = reviewRuleService.reviewContent(content.toString());
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除药品")
    public Result<Void> deleteMedicineById(@PathVariable Long id) {
        medicineService.deleteMedicineById(id);
        return Result.success();
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类ID查询药品")
    public Result<List<Medicine>> findMedicinesByCategoryId(@PathVariable Long categoryId) {
        return Result.success(medicineService.findMedicinesByCategoryId(categoryId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询药品")
    public Result<List<Medicine>> findMedicinesByStatus(@PathVariable String status) {
        return Result.success(medicineService.findMedicinesByStatus(status));
    }

    @GetMapping("/search")
    @Operation(summary = "根据药品名称搜索")
    public Result<List<Medicine>> searchMedicineByName(@RequestParam String name) {
        return Result.success(medicineService.searchMedicineByName(name));
    }
}
