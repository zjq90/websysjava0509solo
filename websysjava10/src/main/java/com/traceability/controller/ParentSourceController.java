package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.ParentSource;
import com.traceability.service.ParentSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 亲本来源REST API控制器
 */
@RestController
@RequestMapping("/api/parent-source")
public class ParentSourceController {

    @Autowired
    private ParentSourceService parentSourceService;

    /**
     * 获取所有亲本来源（分页）
     */
    @GetMapping
    public Result<Page<ParentSource>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ParentSource> pageResult = parentSourceService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有亲本来源（不分页，用于下拉列表）
     */
    @GetMapping("/all")
    public Result<List<ParentSource>> listAll() {
        return Result.success(parentSourceService.findAll());
    }

    @GetMapping("/{id}")
    public Result<ParentSource> getById(@PathVariable Long id) {
        Optional<ParentSource> entity = parentSourceService.findById(id);
        return entity.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/batch-no/{batchNo}")
    public Result<List<ParentSource>> getByBatchNo(@PathVariable String batchNo) {
        return Result.success(parentSourceService.findByBatchNo(batchNo));
    }

    @PostMapping
    public Result<ParentSource> add(@RequestBody ParentSource entity) {
        ParentSource saved = parentSourceService.save(entity);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    public Result<ParentSource> update(@RequestBody ParentSource entity) {
        if (entity.getId() == null) return Result.error("ID不能为空");
        ParentSource updated = parentSourceService.update(entity);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        parentSourceService.deleteById(id);
        return Result.success();
    }
}
