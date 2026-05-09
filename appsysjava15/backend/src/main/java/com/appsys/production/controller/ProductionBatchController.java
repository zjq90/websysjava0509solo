package com.appsys.production.controller;

import com.appsys.production.common.Result;
import com.appsys.production.dto.ProductionBatchDTO;
import com.appsys.production.entity.ProductionBatch;
import com.appsys.production.service.ProductionBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@Tag(name = "批次管理", description = "生产批次增删改查接口")
@CrossOrigin(origins = "*")
public class ProductionBatchController {

    @Autowired
    private ProductionBatchService batchService;

    @GetMapping
    @Operation(summary = "查询所有批次", description = "获取所有生产批次列表")
    public Result<List<ProductionBatch>> list() {
        return Result.success(batchService.list());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询批次", description = "根据批次ID获取批次详情")
    public Result<ProductionBatch> getById(@Parameter(description = "批次ID") @PathVariable Long id) {
        return Result.success(batchService.getById(id));
    }

    @GetMapping("/batchNo/{batchNo}")
    @Operation(summary = "根据批次号查询批次", description = "扫码查询：根据批次号获取批次详情")
    public Result<ProductionBatch> getByBatchNo(@Parameter(description = "批次编号") @PathVariable String batchNo) {
        return Result.success(batchService.getByBatchNo(batchNo));
    }

    @PostMapping
    @Operation(summary = "创建批次", description = "创建新的生产批次，自动创建四个生产环节")
    public Result<ProductionBatch> create(@Valid @RequestBody ProductionBatchDTO dto) {
        return Result.success(batchService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新批次", description = "更新批次信息")
    public Result<ProductionBatch> update(@Parameter(description = "批次ID") @PathVariable Long id, @Valid @RequestBody ProductionBatchDTO dto) {
        return Result.success(batchService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除批次", description = "删除批次及其相关环节数据")
    public Result<Void> delete(@Parameter(description = "批次ID") @PathVariable Long id) {
        batchService.delete(id);
        return Result.success();
    }
}
