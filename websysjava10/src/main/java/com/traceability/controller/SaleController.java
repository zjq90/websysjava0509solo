package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.Sale;
import com.traceability.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    /**
     * 获取所有销售记录（分页）
     */
    @GetMapping
    public Result<Page<Sale>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Sale> pageResult = saleService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有销售记录（不分页，用于下拉列表）
     */
    @GetMapping("/all")
    public Result<List<Sale>> listAll() {
        return Result.success(saleService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Sale> getById(@PathVariable Long id) {
        Optional<Sale> entity = saleService.findById(id);
        return entity.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/batch-no/{batchNo}")
    public Result<List<Sale>> getByBatchNo(@PathVariable String batchNo) {
        return Result.success(saleService.findByBatchNo(batchNo));
    }

    @GetMapping("/package-no/{packageNo}")
    public Result<List<Sale>> getByPackageNo(@PathVariable String packageNo) {
        return Result.success(saleService.findByPackageNo(packageNo));
    }

    @PostMapping
    public Result<Sale> add(@RequestBody Sale entity) {
        Sale saved = saleService.save(entity);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    public Result<Sale> update(@RequestBody Sale entity) {
        if (entity.getId() == null) return Result.error("ID不能为空");
        Sale updated = saleService.update(entity);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        saleService.deleteById(id);
        return Result.success();
    }
}
