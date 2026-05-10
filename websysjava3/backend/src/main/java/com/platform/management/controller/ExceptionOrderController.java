package com.platform.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.management.common.Result;
import com.platform.management.dto.ExceptionOrderHandleDTO;
import com.platform.management.entity.ExceptionOrder;
import com.platform.management.service.ExceptionOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 异常订单Controller
 * 
 * @author platform
 * @version 1.0.0
 */
@RestController
@RequestMapping("/exception-orders")
@Tag(name = "异常订单管理", description = "异常订单相关接口")
public class ExceptionOrderController {

    @Autowired
    private ExceptionOrderService exceptionOrderService;

    @GetMapping("/page")
    @Operation(summary = "分页查询异常订单列表")
    public Result<Page<ExceptionOrder>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "订单号") @RequestParam(required = false) String orderNo,
            @Parameter(description = "异常类型") @RequestParam(required = false) String exceptionType,
            @Parameter(description = "处理状态") @RequestParam(required = false) String handleStatus) {
        Page<ExceptionOrder> result = exceptionOrderService.getPage(pageNum, pageSize, orderNo, exceptionType, handleStatus);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取异常订单详情")
    public Result<ExceptionOrder> getDetail(@PathVariable Long id) {
        ExceptionOrder detail = exceptionOrderService.getDetail(id);
        return Result.success(detail);
    }

    @PostMapping
    @Operation(summary = "创建异常订单")
    public Result<Boolean> create(@RequestBody ExceptionOrder exceptionOrder) {
        boolean result = exceptionOrderService.createExceptionOrder(exceptionOrder);
        return Result.success(result);
    }

    @PostMapping("/handle")
    @Operation(summary = "处理异常订单")
    public Result<Boolean> handle(@RequestBody @Validated ExceptionOrderHandleDTO dto) {
        boolean result = exceptionOrderService.handleException(dto);
        return Result.success(result ? "处理成功" : "处理失败", result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除异常订单")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = exceptionOrderService.deleteExceptionOrder(id);
        return Result.success(result);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除异常订单")
    public Result<Boolean> batchDelete(@RequestBody List<Long> ids) {
        boolean result = exceptionOrderService.batchDelete(ids);
        return Result.success(result);
    }
}
