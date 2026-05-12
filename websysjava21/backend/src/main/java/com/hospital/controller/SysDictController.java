package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.SysDict;
import com.hospital.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 系统字典控制器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/system/dict")
@Tag(name = "字典管理", description = "系统字典管理相关接口")
public class SysDictController {

    @Autowired
    private SysDictService dictService;

    /**
     * 分页查询字典列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询字典列表", description = "分页查询系统字典列表")
    public Result<Page<SysDict>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SysDict> dicts = dictService.findAll(pageable);
        return Result.success(dicts);
    }

    /**
     * 根据类型查询字典列表
     */
    @GetMapping("/type/{dictType}")
    @Operation(summary = "根据类型查询字典", description = "根据字典类型查询字典列表")
    public Result<List<SysDict>> getByType(@PathVariable String dictType) {
        List<SysDict> dicts = dictService.findByDictType(dictType);
        return Result.success(dicts);
    }

    /**
     * 根据ID查询字典
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询字典详情", description = "根据字典ID查询字典详情")
    public Result<SysDict> getById(@PathVariable Long id) {
        Optional<SysDict> dictOptional = dictService.findById(id);
        if (dictOptional.isPresent()) {
            return Result.success(dictOptional.get());
        }
        return Result.fail("字典不存在");
    }

    /**
     * 新增字典
     */
    @PostMapping
    @Operation(summary = "新增字典", description = "新增系统字典")
    public Result<SysDict> add(@RequestBody SysDict dict) {
        SysDict savedDict = dictService.save(dict);
        return Result.success("新增成功", savedDict);
    }

    /**
     * 更新字典
     */
    @PutMapping
    @Operation(summary = "更新字典", description = "更新系统字典信息")
    public Result<SysDict> update(@RequestBody SysDict dict) {
        if (!dictService.findById(dict.getId()).isPresent()) {
            return Result.fail("字典不存在");
        }
        SysDict updatedDict = dictService.save(dict);
        return Result.success("更新成功", updatedDict);
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典", description = "根据字典ID删除字典")
    public Result<Void> delete(@PathVariable Long id) {
        if (!dictService.findById(id).isPresent()) {
            return Result.fail("字典不存在");
        }
        dictService.deleteById(id);
        return Result.success("删除成功", null);
    }
}
