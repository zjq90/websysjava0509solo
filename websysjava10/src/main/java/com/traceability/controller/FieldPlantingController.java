package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.FieldPlanting;
import com.traceability.service.FieldPlantingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/field-planting")
public class FieldPlantingController {

    @Autowired
    private FieldPlantingService fieldPlantingService;

    /**
     * 获取所有田间种植记录（分页）
     */
    @GetMapping
    public Result<Page<FieldPlanting>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<FieldPlanting> pageResult = fieldPlantingService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有田间种植记录（不分页，用于下拉列表）
     */
    @GetMapping("/all")
    public Result<List<FieldPlanting>> listAll() {
        return Result.success(fieldPlantingService.findAll());
    }

    @GetMapping("/{id}")
    public Result<FieldPlanting> getById(@PathVariable Long id) {
        Optional<FieldPlanting> entity = fieldPlantingService.findById(id);
        return entity.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/batch-no/{batchNo}")
    public Result<List<FieldPlanting>> getByBatchNo(@PathVariable String batchNo) {
        return Result.success(fieldPlantingService.findByBatchNo(batchNo));
    }

    @PostMapping
    public Result<FieldPlanting> add(@RequestBody FieldPlanting entity) {
        FieldPlanting saved = fieldPlantingService.save(entity);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    public Result<FieldPlanting> update(@RequestBody FieldPlanting entity) {
        if (entity.getId() == null) return Result.error("ID不能为空");
        FieldPlanting updated = fieldPlantingService.update(entity);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        fieldPlantingService.deleteById(id);
        return Result.success();
    }
}
