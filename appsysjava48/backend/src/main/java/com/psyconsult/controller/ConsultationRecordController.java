package com.psyconsult.controller;

import com.psyconsult.common.Result;
import com.psyconsult.entity.ConsultationRecord;
import com.psyconsult.service.ConsultationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@Api(tags = "咨询记录管理")
@CrossOrigin
public class ConsultationRecordController {

    @Autowired
    private ConsultationRecordService recordService;

    @GetMapping("/counselor/{counselorId}")
    @ApiOperation("获取咨询师的咨询记录")
    public Result<List<ConsultationRecord>> getRecordsByCounselor(@PathVariable Long counselorId) {
        return Result.success(recordService.getRecordsByCounselor(counselorId));
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("获取用户的咨询记录")
    public Result<List<ConsultationRecord>> getRecordsByUser(@PathVariable Long userId) {
        return Result.success(recordService.getRecordsByUser(userId));
    }

    @GetMapping("/counselor/{counselorId}/tag/{tag}")
    @ApiOperation("根据标签筛选咨询师的咨询记录")
    public Result<List<ConsultationRecord>> getRecordsByCounselorAndTag(
            @PathVariable Long counselorId,
            @PathVariable String tag) {
        return Result.success(recordService.getRecordsByCounselorAndTag(counselorId, tag));
    }

    @PostMapping
    @ApiOperation("创建咨询记录")
    public Result<ConsultationRecord> createRecord(@RequestBody ConsultationRecord record) {
        try {
            return Result.success(recordService.createRecord(record));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("更新咨询记录")
    public Result<ConsultationRecord> updateRecord(@PathVariable Long id, @RequestBody ConsultationRecord record) {
        try {
            return Result.success(recordService.updateRecord(id, record));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("获取咨询记录详情")
    public Result<ConsultationRecord> getRecordById(@PathVariable Long id) {
        return Result.success(recordService.getRecordById(id).orElse(null));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除咨询记录")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return Result.success();
    }
}
