package com.seedtrace.controller;

import com.seedtrace.dto.ApiResponse;
import com.seedtrace.dto.SeedBatchRequest;
import com.seedtrace.entity.SeedBatch;
import com.seedtrace.service.SeedBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 种子批次管理Controller
 * 
 * <p>提供种子批次的CRUD管理接口。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/batches")
@Tag(name = "批次管理", description = "种子批次信息管理相关接口（增删改查）")
@CrossOrigin(origins = "*")
public class SeedBatchController {

    private static final Logger logger = LoggerFactory.getLogger(SeedBatchController.class);

    @Autowired
    private SeedBatchService seedBatchService;

    /**
     * 查询所有批次
     */
    @GetMapping
    @Operation(summary = "查询所有批次", description = "获取所有种子批次的列表")
    public ApiResponse<List<SeedBatch>> getAllBatches() {
        logger.info("查询所有批次");
        List<SeedBatch> batches = seedBatchService.getAllBatches();
        return ApiResponse.success("查询成功", batches);
    }

    /**
     * 根据ID查询批次
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询批次", description = "根据批次ID获取详细信息")
    public ApiResponse<SeedBatch> getBatchById(
            @Parameter(description = "批次ID", required = true)
            @PathVariable Long id) {
        logger.info("查询批次，ID：{}", id);
        Optional<SeedBatch> batch = seedBatchService.getBatchById(id);
        if (batch.isPresent()) {
            return ApiResponse.success("查询成功", batch.get());
        } else {
            return ApiResponse.error(404, "批次不存在");
        }
    }

    /**
     * 根据批次编号查询批次
     */
    @GetMapping("/code/{batchCode}")
    @Operation(summary = "根据批次号查询批次", description = "根据批次编号获取详细信息")
    public ApiResponse<SeedBatch> getBatchByCode(
            @Parameter(description = "批次编号", required = true)
            @PathVariable String batchCode) {
        logger.info("查询批次，编号：{}", batchCode);
        Optional<SeedBatch> batch = seedBatchService.getBatchByCode(batchCode);
        if (batch.isPresent()) {
            return ApiResponse.success("查询成功", batch.get());
        } else {
            return ApiResponse.error(404, "批次不存在");
        }
    }

    /**
     * 创建新批次
     */
    @PostMapping
    @Operation(summary = "创建批次", description = "创建新的种子批次信息")
    public ApiResponse<SeedBatch> createBatch(
            @Parameter(description = "批次信息", required = true)
            @RequestBody SeedBatchRequest request) {
        logger.info("创建批次，编号：{}", request.getBatchCode());
        try {
            SeedBatch batch = seedBatchService.createBatch(request);
            return ApiResponse.success("创建成功", batch);
        } catch (IllegalArgumentException e) {
            logger.warn("创建批次参数错误：{}", e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("创建批次失败", e);
            return ApiResponse.error(500, "创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新批次
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新批次", description = "更新已有的种子批次信息")
    public ApiResponse<SeedBatch> updateBatch(
            @Parameter(description = "批次ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "更新的批次信息", required = true)
            @RequestBody SeedBatchRequest request) {
        logger.info("更新批次，ID：{}", id);
        try {
            SeedBatch batch = seedBatchService.updateBatch(id, request);
            return ApiResponse.success("更新成功", batch);
        } catch (IllegalArgumentException e) {
            logger.warn("更新批次参数错误：{}", e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新批次失败", e);
            return ApiResponse.error(500, "更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除批次
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除批次", description = "根据ID删除种子批次信息")
    public ApiResponse<Void> deleteBatch(
            @Parameter(description = "批次ID", required = true)
            @PathVariable Long id) {
        logger.info("删除批次，ID：{}", id);
        try {
            seedBatchService.deleteBatch(id);
            return ApiResponse.success("删除成功", null);
        } catch (IllegalArgumentException e) {
            logger.warn("删除批次参数错误：{}", e.getMessage());
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            logger.error("删除批次失败", e);
            return ApiResponse.error(500, "删除失败：" + e.getMessage());
        }
    }

    /**
     * 验证批次号唯一性
     */
    @GetMapping("/check-code/{batchCode}")
    @Operation(summary = "验证批次号", description = "检查批次号是否已存在，用于前端实时验证")
    public ApiResponse<Boolean> checkBatchCode(
            @Parameter(description = "批次编号", required = true)
            @PathVariable String batchCode) {
        Optional<SeedBatch> existing = seedBatchService.getBatchByCode(batchCode);
        return ApiResponse.success("验证完成", existing.isPresent());
    }
}
