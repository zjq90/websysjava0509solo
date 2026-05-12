package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.Bed;
import com.hospital.service.BedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 床位Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/bed")
@CrossOrigin
@Tag(name = "床位管理", description = "床位信息的增删改查")
public class BedController {

    @Autowired
    private BedService bedService;

    @GetMapping
    @Operation(summary = "查询所有床位")
    public Result<List<Bed>> findAll() {
        return Result.success(bedService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询床位")
    public Result<Bed> findById(@PathVariable Long id) {
        return bedService.findById(id)
                .map(Result::success)
                .orElse(Result.error("床位不存在"));
    }

    @GetMapping("/available")
    @Operation(summary = "查询所有空闲床位")
    public Result<List<Bed>> findAllAvailable() {
        return Result.success(bedService.findAllAvailable());
    }

    @GetMapping("/ward/{wardName}")
    @Operation(summary = "根据病区查询床位")
    public Result<List<Bed>> findByWardName(@PathVariable String wardName) {
        return Result.success(bedService.findByWardName(wardName));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询床位")
    public Result<List<Bed>> findByStatus(@PathVariable String status) {
        return Result.success(bedService.findByStatus(status));
    }

    @PostMapping
    @Operation(summary = "新增床位")
    public Result<Bed> save(@RequestBody Bed bed) {
        return Result.success(bedService.save(bed));
    }

    @PutMapping
    @Operation(summary = "修改床位")
    public Result<Bed> update(@RequestBody Bed bed) {
        return Result.success(bedService.save(bed));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除床位")
    public Result<Void> deleteById(@PathVariable Long id) {
        bedService.deleteById(id);
        return Result.success();
    }
}
