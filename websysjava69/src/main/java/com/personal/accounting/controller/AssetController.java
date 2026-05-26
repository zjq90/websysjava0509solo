package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.entity.Asset;
import com.personal.accounting.enums.AssetType;
import com.personal.accounting.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "资产管理", description = "资产管理相关接口")
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @Operation(summary = "获取用户资产列表")
    @GetMapping("/user/{userId}")
    public Result<List<Asset>> getUserAssets(@PathVariable Long userId) {
        return Result.success(assetService.getUserAssets(userId));
    }

    @Operation(summary = "获取用户指定类型资产")
    @GetMapping("/user/{userId}/type/{type}")
    public Result<List<Asset>> getUserAssetsByType(@PathVariable Long userId, @PathVariable AssetType type) {
        return Result.success(assetService.getUserAssetsByType(userId, type));
    }

    @Operation(summary = "获取资产详情")
    @GetMapping("/{id}")
    public Result<Asset> getAssetById(@PathVariable Long id) {
        return Result.success(assetService.getAssetById(id));
    }

    @Operation(summary = "创建资产")
    @PostMapping
    public Result<Asset> createAsset(@RequestBody Asset asset) {
        return Result.success(assetService.createAsset(asset));
    }

    @Operation(summary = "更新资产")
    @PutMapping("/{id}")
    public Result<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
        return Result.success(assetService.updateAsset(id, asset));
    }

    @Operation(summary = "删除资产")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return Result.success();
    }

    @Operation(summary = "获取用户总资产价值")
    @GetMapping("/user/{userId}/total")
    public Result<BigDecimal> getTotalAssetValue(@PathVariable Long userId) {
        return Result.success(assetService.getTotalAssetValue(userId));
    }
}
