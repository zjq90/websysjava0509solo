package com.accounting.system.controller;

import com.accounting.system.common.Result;
import com.accounting.system.dto.TransferRecordDTO;
import com.accounting.system.entity.TransferRecord;
import com.accounting.system.service.TransferRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 转账记录Controller
 * 提供账户间资金流动记录管理API
 * 如"现金→银行卡"、"支付宝→银行卡"等
 */
@Api(tags = "转账记录管理")
@RestController
@RequestMapping("/transfers")
public class TransferRecordController {

    @Autowired
    private TransferRecordService transferRecordService;

    @ApiOperation("分页查询转账记录")
    @GetMapping("/page")
    public Result<IPage<TransferRecord>> pageList(
            @ApiParam(value = "当前页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页条数", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<TransferRecord> page = new Page<>(pageNum, pageSize);
        return Result.success(transferRecordService.pageList(page));
    }

    @ApiOperation("查询所有转账记录")
    @GetMapping
    public Result<List<TransferRecord>> listAll() {
        return Result.success(transferRecordService.listAll());
    }

    @ApiOperation("根据ID获取转账记录详情")
    @GetMapping("/{id}")
    public Result<TransferRecord> getDetailById(
            @ApiParam(value = "转账记录ID", required = true)
            @PathVariable Long id) {
        return Result.success(transferRecordService.getDetailById(id));
    }

    @ApiOperation("新增转账记录（账户间资金流动）")
    @PostMapping
    public Result<TransferRecord> addTransfer(
            @ApiParam(value = "转账记录信息", required = true)
            @Valid @RequestBody TransferRecordDTO dto) {
        return Result.success("新增转账记录成功", transferRecordService.addTransfer(dto));
    }

    @ApiOperation("删除转账记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteTransfer(
            @ApiParam(value = "转账记录ID", required = true)
            @PathVariable Long id) {
        transferRecordService.deleteTransfer(id);
        return Result.success("删除转账记录成功", null);
    }
}
