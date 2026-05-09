package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.Processing;
import com.traceability.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/processing")
public class ProcessingController {

    @Autowired
    private ProcessingService processingService;

    /**
     * 获取所有加工记录（分页）
     */
    @GetMapping
    public Result<Page<Processing>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Processing> pageResult = processingService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有加工记录（不分页，用于下拉列表）
     */
    @GetMapping("/all")
    public Result<List<Processing>> listAll() {
        return Result.success(processingService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Processing> getById(@PathVariable Long id) {
        Optional<Processing> entity = processingService.findById(id);
        return entity.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/batch-no/{batchNo}")
    public Result<List<Processing>> getByBatchNo(@PathVariable String batchNo) {
        return Result.success(processingService.findByBatchNo(batchNo));
    }

    @PostMapping
    public Result<Processing> add(@RequestBody Processing entity) {
        Processing saved = processingService.save(entity);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    public Result<Processing> update(@RequestBody Processing entity) {
        if (entity.getId() == null) return Result.error("ID不能为空");
        Processing updated = processingService.update(entity);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        processingService.deleteById(id);
        return Result.success();
    }
}
