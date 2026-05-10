package com.appsys.field.controller;

import com.appsys.common.result.PageResult;
import com.appsys.common.result.Result;
import com.appsys.field.dto.FieldRecordDTO;
import com.appsys.field.entity.FieldRecord;
import com.appsys.field.service.FieldRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 田间记录控制器
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Tag(name = "田间记录", description = "田间记录增删改查接口")
@RestController
@RequestMapping("/api/field")
public class FieldRecordController {

    @Autowired
    private FieldRecordService fieldRecordService;

    /**
     * 分页查询田间记录列表
     */
    @Operation(summary = "分页查询田间记录列表", description = "根据关键字或日期范围分页查询田间记录")
    @GetMapping
    @PreAuthorize("hasAnyRole('AGRICULTURAL_TECHNICIAN', 'MANAGER', 'ADMIN')")
    public Result<PageResult<FieldRecord>> list(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "搜索关键字（地块名称或作物名称）") @RequestParam(required = false) String keyword,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        PageResult<FieldRecord> result = fieldRecordService.list(page, size, keyword, startDate, endDate);
        return Result.success(result);
    }

    /**
     * 根据ID查询田间记录详情
     */
    @Operation(summary = "查询田间记录详情", description = "根据ID查询田间记录详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGRICULTURAL_TECHNICIAN', 'MANAGER', 'ADMIN')")
    public Result<FieldRecord> getById(@Parameter(description = "记录ID") @PathVariable Long id) {
        FieldRecord record = fieldRecordService.getById(id);
        return Result.success(record);
    }

    /**
     * 新增田间记录
     */
    @Operation(summary = "新增田间记录", description = "新增田间记录")
    @PostMapping
    @PreAuthorize("hasAnyRole('AGRICULTURAL_TECHNICIAN', 'MANAGER', 'ADMIN')")
    public Result<FieldRecord> create(@Validated @RequestBody FieldRecordDTO dto) {
        FieldRecord record = fieldRecordService.create(dto);
        return Result.success("田间记录新增成功", record);
    }

    /**
     * 更新田间记录
     */
    @Operation(summary = "更新田间记录", description = "更新田间记录信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGRICULTURAL_TECHNICIAN', 'MANAGER', 'ADMIN')")
    public Result<FieldRecord> update(
            @Parameter(description = "记录ID") @PathVariable Long id,
            @Validated @RequestBody FieldRecordDTO dto) {
        FieldRecord record = fieldRecordService.update(id, dto);
        return Result.success("田间记录更新成功", record);
    }

    /**
     * 删除田间记录
     */
    @Operation(summary = "删除田间记录", description = "删除田间记录（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGRICULTURAL_TECHNICIAN', 'MANAGER', 'ADMIN')")
    public Result<Void> delete(@Parameter(description = "记录ID") @PathVariable Long id) {
        fieldRecordService.delete(id);
        return Result.success("田间记录删除成功", null);
    }
}
