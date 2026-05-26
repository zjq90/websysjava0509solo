package com.accounting.system.controller;

import com.accounting.system.common.Result;
import com.accounting.system.dto.TagDTO;
import com.accounting.system.entity.Tag;
import com.accounting.system.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 标签管理Controller
 * 提供自定义标签管理API，如"旅行""生日礼物""医疗"等
 * 支持多标签组合和标签统计
 */
@Api(tags = "账单标签管理")
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @ApiOperation("查询所有标签列表")
    @GetMapping
    public Result<List<Tag>> listAll() {
        return Result.success(tagService.listAll());
    }

    @ApiOperation("根据ID获取标签详情")
    @GetMapping("/{id}")
    public Result<Tag> getDetailById(
            @ApiParam(value = "标签ID", required = true)
            @PathVariable Long id) {
        return Result.success(tagService.getDetailById(id));
    }

    @ApiOperation("新增标签")
    @PostMapping
    public Result<Tag> addTag(
            @ApiParam(value = "标签信息", required = true)
            @Valid @RequestBody TagDTO dto) {
        return Result.success("新增标签成功", tagService.addTag(dto));
    }

    @ApiOperation("更新标签")
    @PutMapping
    public Result<Tag> updateTag(
            @ApiParam(value = "标签信息", required = true)
            @Valid @RequestBody TagDTO dto) {
        return Result.success("更新标签成功", tagService.updateTag(dto));
    }

    @ApiOperation("删除标签")
    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(
            @ApiParam(value = "标签ID", required = true)
            @PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success("删除标签成功", null);
    }

    @ApiOperation("根据交易记录ID查询关联的标签列表")
    @GetMapping("/transaction/{transactionId}")
    public Result<List<Tag>> listByTransactionId(
            @ApiParam(value = "交易记录ID", required = true)
            @PathVariable Long transactionId) {
        return Result.success(tagService.listByTransactionId(transactionId));
    }
}
