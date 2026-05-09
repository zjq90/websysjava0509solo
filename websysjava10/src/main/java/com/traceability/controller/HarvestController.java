package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.Harvest;
import com.traceability.service.HarvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/harvest")
public class HarvestController {

    @Autowired
    private HarvestService harvestService;

    /**
     * 获取所有收获记录（分页）
     */
    @GetMapping
    public Result<Page<Harvest>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Harvest> pageResult = harvestService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有收获记录（不分页，用于下拉列表）
     */
    @GetMapping("/all")
    public Result<List<Harvest>> listAll() {
        return Result.success(harvestService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Harvest> getById(@PathVariable Long id) {
        Optional<Harvest> entity = harvestService.findById(id);
        return entity.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/batch-no/{batchNo}")
    public Result<List<Harvest>> getByBatchNo(@PathVariable String batchNo) {
        return Result.success(harvestService.findByBatchNo(batchNo));
    }

    @PostMapping
    public Result<Harvest> add(@RequestBody Harvest entity) {
        Harvest saved = harvestService.save(entity);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    public Result<Harvest> update(@RequestBody Harvest entity) {
        if (entity.getId() == null) return Result.error("ID不能为空");
        Harvest updated = harvestService.update(entity);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        harvestService.deleteById(id);
        return Result.success();
    }
}
