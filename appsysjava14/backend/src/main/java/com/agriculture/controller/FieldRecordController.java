package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.FieldRecord;
import com.agriculture.service.FieldRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 田间记录控制器
 * 处理田间数据采集的增删改查操作
 * 
 * 采集流程：
 * 用户登录 -> 选择"田间记录"模块 -> 定位当前地块 -> 选择作物品种 ->
 * 录入生长阶段 -> 填写观测数据 -> 上传照片 -> 提交
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "田间记录管理", description = "田间数据采集的增删改查操作")
@RestController
@RequestMapping("/field-records")
@CrossOrigin(origins = "*")
public class FieldRecordController {

    @Autowired
    private FieldRecordService fieldRecordService;

    /**
     * 查询所有田间记录
     */
    @Operation(summary = "查询所有记录", description = "获取所有田间数据采集记录")
    @GetMapping
    public Result<List<FieldRecord>> list() {
        return Result.success(fieldRecordService.findAll());
    }

    /**
     * 查询最新的田间记录
     */
    @Operation(summary = "查询最新记录", description = "获取最近创建的田间记录")
    @GetMapping("/latest")
    public Result<List<FieldRecord>> listLatest() {
        return Result.success(fieldRecordService.findLatestRecords());
    }

    /**
     * 根据ID获取记录详情
     */
    @Operation(summary = "获取记录详情", description = "根据ID获取田间记录详细信息")
    @GetMapping("/{id}")
    public Result<FieldRecord> getById(@Parameter(description = "记录ID") @PathVariable Long id) {
        Optional<FieldRecord> recordOpt = fieldRecordService.findById(id);
        if (recordOpt.isPresent()) {
            return Result.success(recordOpt.get());
        } else {
            return Result.notFound("田间记录不存在");
        }
    }

    /**
     * 根据记录编号查询
     */
    @Operation(summary = "根据编号查询", description = "通过记录编号查询田间记录")
    @GetMapping("/no/{recordNo}")
    public Result<FieldRecord> getByNo(@Parameter(description = "记录编号") @PathVariable String recordNo) {
        Optional<FieldRecord> recordOpt = fieldRecordService.findByRecordNo(recordNo);
        if (recordOpt.isPresent()) {
            return Result.success(recordOpt.get());
        } else {
            return Result.notFound("田间记录不存在");
        }
    }

    /**
     * 根据地块ID查询记录
     */
    @Operation(summary = "按地块查询记录", description = "查询某个地块的所有田间记录")
    @GetMapping("/plot/{plotId}")
    public Result<List<FieldRecord>> getByPlotId(@Parameter(description = "地块ID") @PathVariable Long plotId) {
        return Result.success(fieldRecordService.findByPlotId(plotId));
    }

    /**
     * 根据作物ID查询记录
     */
    @Operation(summary = "按作物查询记录", description = "查询某个作物品种的所有田间记录")
    @GetMapping("/crop/{cropId}")
    public Result<List<FieldRecord>> getByCropId(@Parameter(description = "作物ID") @PathVariable Long cropId) {
        return Result.success(fieldRecordService.findByCropId(cropId));
    }

    /**
     * 根据观测人查询记录
     */
    @Operation(summary = "按观测人查询记录", description = "查询某个用户创建的所有田间记录")
    @GetMapping("/observer/{observerId}")
    public Result<List<FieldRecord>> getByObserverId(@Parameter(description = "观测人ID") @PathVariable Long observerId) {
        return Result.success(fieldRecordService.findByObserverId(observerId));
    }

    /**
     * 创建田间记录
     */
    @Operation(summary = "创建田间记录", description = "提交新的田间数据采集记录")
    @PostMapping
    public Result<FieldRecord> create(@RequestBody FieldRecord fieldRecord) {
        try {
            FieldRecord created = fieldRecordService.create(fieldRecord);
            return Result.success("提交成功", created);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新田间记录
     */
    @Operation(summary = "更新田间记录", description = "修改已有的田间记录")
    @PutMapping
    public Result<FieldRecord> update(@RequestBody FieldRecord fieldRecord) {
        try {
            FieldRecord updated = fieldRecordService.update(fieldRecord);
            return Result.success("更新成功", updated);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除田间记录
     */
    @Operation(summary = "删除记录", description = "根据ID删除田间记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "记录ID") @PathVariable Long id) {
        fieldRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 统计用户记录数量
     */
    @Operation(summary = "统计记录数量", description = "统计某个用户创建的田间记录数量")
    @GetMapping("/count/{observerId}")
    public Result<Long> countByObserver(@Parameter(description = "观测人ID") @PathVariable Long observerId) {
        return Result.success(fieldRecordService.countByObserverId(observerId));
    }
}
