package com.photostudio.controller;

import com.photostudio.entity.Costume;
import com.photostudio.entity.Costume.CleaningStatus;
import com.photostudio.entity.Costume.CostumeType;
import com.photostudio.service.CostumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服装管理控制器
 * 提供服装管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/costumes")
@Tag(name = "服装管理", description = "服装库存管理与清洗状态")
public class CostumeController {

    private final CostumeService costumeService;

    @Autowired
    public CostumeController(CostumeService costumeService) {
        this.costumeService = costumeService;
    }

    @GetMapping
    @Operation(summary = "获取所有服装", description = "获取系统中所有服装信息")
    public ResponseEntity<List<Costume>> getAllCostumes() {
        return ResponseEntity.ok(costumeService.getAllCostumes());
    }

    @GetMapping("/available")
    @Operation(summary = "获取可用服装", description = "获取所有可用的服装")
    public ResponseEntity<List<Costume>> getAvailableCostumes() {
        return ResponseEntity.ok(costumeService.getAvailableCostumes());
    }

    @GetMapping("/available-clean")
    @Operation(summary = "获取可用且已清洁的服装", description = "获取可用且已清洁的服装")
    public ResponseEntity<List<Costume>> getAvailableAndCleanCostumes() {
        return ResponseEntity.ok(costumeService.getAvailableAndCleanCostumes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取服装", description = "根据服装ID获取详细信息")
    public ResponseEntity<Costume> getCostumeById(
            @Parameter(description = "服装ID") @PathVariable Long id) {
        return costumeService.getCostumeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取服装", description = "根据服装类型获取服装列表")
    public ResponseEntity<List<Costume>> getCostumesByType(
            @Parameter(description = "服装类型") @PathVariable CostumeType type) {
        return ResponseEntity.ok(costumeService.getCostumesByType(type));
    }

    @GetMapping("/type/{type}/available")
    @Operation(summary = "获取可用的指定类型服装", description = "获取可用的指定类型服装")
    public ResponseEntity<List<Costume>> getAvailableCostumesByType(
            @Parameter(description = "服装类型") @PathVariable CostumeType type) {
        return ResponseEntity.ok(costumeService.getAvailableCostumesByType(type));
    }

    @GetMapping("/cleaning-status/{status}")
    @Operation(summary = "根据清洗状态获取服装", description = "根据清洗状态获取服装列表")
    public ResponseEntity<List<Costume>> getCostumesByCleaningStatus(
            @Parameter(description = "清洗状态") @PathVariable CleaningStatus status) {
        return ResponseEntity.ok(costumeService.getCostumesByCleaningStatus(status));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索服装", description = "根据名称关键词搜索服装")
    public ResponseEntity<List<Costume>> searchCostumes(
            @Parameter(description = "名称关键词") @RequestParam String name) {
        return ResponseEntity.ok(costumeService.searchCostumesByName(name));
    }

    @PostMapping
    @Operation(summary = "创建服装", description = "创建新的服装信息")
    public ResponseEntity<Costume> createCostume(@RequestBody Costume costume) {
        return ResponseEntity.ok(costumeService.createCostume(costume));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新服装", description = "更新服装信息")
    public ResponseEntity<Costume> updateCostume(
            @Parameter(description = "服装ID") @PathVariable Long id,
            @RequestBody Costume costume) {
        return ResponseEntity.ok(costumeService.updateCostume(id, costume));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除服装", description = "删除服装信息")
    public ResponseEntity<Void> deleteCostume(
            @Parameter(description = "服装ID") @PathVariable Long id) {
        costumeService.deleteCostume(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/mark-used")
    @Operation(summary = "标记服装已使用", description = "标记服装已使用并更新清洗状态")
    public ResponseEntity<Costume> markAsUsed(
            @Parameter(description = "服装ID") @PathVariable Long id) {
        return ResponseEntity.ok(costumeService.markAsUsed(id));
    }

    @PutMapping("/{id}/mark-cleaned")
    @Operation(summary = "标记服装已清洁", description = "标记服装已清洁")
    public ResponseEntity<Costume> markAsCleaned(
            @Parameter(description = "服装ID") @PathVariable Long id) {
        return ResponseEntity.ok(costumeService.markAsCleaned(id));
    }

    @PutMapping("/{id}/mark-cleaning")
    @Operation(summary = "标记服装清洗中", description = "标记服装正在清洗")
    public ResponseEntity<Costume> markAsCleaning(
            @Parameter(description = "服装ID") @PathVariable Long id) {
        return ResponseEntity.ok(costumeService.markAsCleaning(id));
    }
}
