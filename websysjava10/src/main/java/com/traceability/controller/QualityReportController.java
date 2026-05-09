package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.QualityReport;
import com.traceability.service.QualityReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quality-report")
public class QualityReportController {

    @Autowired
    private QualityReportService qualityReportService;

    /**
     * 获取所有质检报告（分页）
     */
    @GetMapping
    public Result<Page<QualityReport>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<QualityReport> pageResult = qualityReportService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有质检报告（不分页，用于下拉列表）
     */
    @GetMapping("/all")
    public Result<List<QualityReport>> listAll() {
        return Result.success(qualityReportService.findAll());
    }

    @GetMapping("/{id}")
    public Result<QualityReport> getById(@PathVariable Long id) {
        Optional<QualityReport> entity = qualityReportService.findById(id);
        return entity.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/batch-no/{batchNo}")
    public Result<List<QualityReport>> getByBatchNo(@PathVariable String batchNo) {
        return Result.success(qualityReportService.findByBatchNo(batchNo));
    }

    @PostMapping
    public Result<QualityReport> add(@RequestBody QualityReport entity) {
        QualityReport saved = qualityReportService.save(entity);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    public Result<QualityReport> update(@RequestBody QualityReport entity) {
        if (entity.getId() == null) return Result.error("ID不能为空");
        QualityReport updated = qualityReportService.update(entity);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        qualityReportService.deleteById(id);
        return Result.success();
    }
}
