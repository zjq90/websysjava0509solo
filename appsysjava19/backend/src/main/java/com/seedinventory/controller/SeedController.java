package com.seedinventory.controller;

import com.seedinventory.common.Result;
import com.seedinventory.entity.Seed;
import com.seedinventory.repository.SeedRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 种子管理控制器
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/seeds")
@CrossOrigin(origins = "*")
@Tag(name = "种子管理", description = "种子信息的增删改查")
public class SeedController {
    
    @Autowired
    private SeedRepository seedRepository;
    
    @PostMapping
    @Operation(summary = "新增种子")
    public Result<Seed> create(@RequestBody Seed seed) {
        if (seedRepository.existsBySeedCode(seed.getSeedCode())) {
            return Result.error("种子编号已存在");
        }
        Seed result = seedRepository.save(seed);
        return Result.success("创建成功", result);
    }
    
    @PutMapping
    @Operation(summary = "更新种子")
    public Result<Seed> update(@RequestBody Seed seed) {
        if (!seedRepository.existsById(seed.getId())) {
            return Result.error(404, "种子不存在");
        }
        Seed result = seedRepository.save(seed);
        return Result.success("更新成功", result);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "查询种子详情")
    public Result<Seed> getById(@PathVariable Long id) {
        Optional<Seed> opt = seedRepository.findById(id);
        if (opt.isPresent()) {
            return Result.success(opt.get());
        }
        return Result.error(404, "种子不存在");
    }
    
    @GetMapping
    @Operation(summary = "查询种子列表")
    public Result<List<Seed>> list() {
        return Result.success(seedRepository.findAll());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除种子")
    public Result<Void> delete(@PathVariable Long id) {
        if (!seedRepository.existsById(id)) {
            return Result.error(404, "种子不存在");
        }
        seedRepository.deleteById(id);
        return Result.success();
    }
}
