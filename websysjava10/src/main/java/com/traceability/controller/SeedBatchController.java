package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.SeedBatch;
import com.traceability.service.SeedBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 种子批次REST API控制器
 */
@RestController
@RequestMapping("/api/seed-batch")
public class SeedBatchController {

    @Autowired
    private SeedBatchService seedBatchService;

    /**
     * 获取所有种子批次（分页）
     */
    @GetMapping
    public Result<Page<SeedBatch>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SeedBatch> pageResult = seedBatchService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有种子批次（不分页，用于下拉列表
     */
    @GetMapping("/all")
    public Result<List<SeedBatch>> listAll() {
        List<SeedBatch> list = seedBatchService.findAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取种子批次
     */
    @GetMapping("/{id}")
    public Result<SeedBatch> getById(@PathVariable Long id) {
        Optional<SeedBatch> seedBatch = seedBatchService.findById(id);
        return seedBatch.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    /**
     * 根据批次号获取种子批次
     */
    @GetMapping("/batch-no/{batchNo}")
    public Result<SeedBatch> getByBatchNo(@PathVariable String batchNo) {
        Optional<SeedBatch> seedBatch = seedBatchService.findByBatchNo(batchNo);
        return seedBatch.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    /**
     * 新增种子批次
     */
    @PostMapping
    public Result<SeedBatch> add(@RequestBody SeedBatch seedBatch) {
        SeedBatch saved = seedBatchService.save(seedBatch);
        return Result.success("新增成功", saved);
    }

    /**
     * 更新种子批次
     */
    @PutMapping
    public Result<SeedBatch> update(@RequestBody SeedBatch seedBatch) {
        if (seedBatch.getId() == null) {
            return Result.error("ID不能为空");
        }
        SeedBatch updated = seedBatchService.update(seedBatch);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除种子批次
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        seedBatchService.deleteById(id);
        return Result.success();
    }
}
