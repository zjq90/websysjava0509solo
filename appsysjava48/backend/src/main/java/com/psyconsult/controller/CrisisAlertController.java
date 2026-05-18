package com.psyconsult.controller;

import com.psyconsult.common.Result;
import com.psyconsult.entity.CrisisAlert;
import com.psyconsult.repository.CrisisAlertRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/crisis")
@Api(tags = "危机预警管理")
@CrossOrigin
public class CrisisAlertController {

    @Autowired
    private CrisisAlertRepository crisisAlertRepository;

    @GetMapping
    @ApiOperation("获取所有危机预警")
    public Result<List<CrisisAlert>> getAllAlerts() {
        return Result.success(crisisAlertRepository.findAll());
    }

    @GetMapping("/status/{status}")
    @ApiOperation("根据状态获取危机预警")
    public Result<List<CrisisAlert>> getAlertsByStatus(@PathVariable String status) {
        return Result.success(crisisAlertRepository.findByStatus(status));
    }

    @GetMapping("/level/{level}")
    @ApiOperation("根据等级获取危机预警")
    public Result<List<CrisisAlert>> getAlertsByLevel(@PathVariable String level) {
        return Result.success(crisisAlertRepository.findByAlertLevel(level));
    }

    @GetMapping("/counselor/{counselorId}")
    @ApiOperation("获取咨询师的危机预警")
    public Result<List<CrisisAlert>> getAlertsByCounselor(@PathVariable Long counselorId) {
        return Result.success(crisisAlertRepository.findByCounselorId(counselorId));
    }

    @PutMapping("/{id}/handle")
    @ApiOperation("处理危机预警")
    public Result<CrisisAlert> handleAlert(@PathVariable Long id, @RequestParam String result) {
        return crisisAlertRepository.findById(id).map(alert -> {
            alert.setStatus("resolved");
            alert.setHandleResult(result);
            alert.setHandleTime(LocalDateTime.now());
            return Result.success(crisisAlertRepository.save(alert));
        }).orElse(Result.error("预警记录不存在"));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取危机预警详情")
    public Result<CrisisAlert> getAlertById(@PathVariable Long id) {
        return Result.success(crisisAlertRepository.findById(id).orElse(null));
    }
}
