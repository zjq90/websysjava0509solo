package com.broadband.controller;

import com.broadband.common.Result;
import com.broadband.entity.BroadbandNumber;
import com.broadband.service.BroadbandNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宽带号码控制器
 * 处理号码查询、选号等接口
 * 
 * @author broadband
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/number")
@Tag(name = "宽带号码管理", description = "号码查询、选号等接口")
public class BroadbandNumberController {

    @Autowired
    private BroadbandNumberService broadbandNumberService;

    @Operation(summary = "获取可用号码列表", description = "根据地区获取所有可用的普通号码")
    @GetMapping("/available/{region}")
    public Result<List<BroadbandNumber>> getAvailableNumbers(@PathVariable String region) {
        try {
            List<BroadbandNumber> numbers = broadbandNumberService.getAvailableNumbers(region);
            return Result.success(numbers);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取靓号列表", description = "根据地区获取所有可用的靓号")
    @GetMapping("/fancy/{region}")
    public Result<List<BroadbandNumber>> getFancyNumbers(@PathVariable String region) {
        try {
            List<BroadbandNumber> numbers = broadbandNumberService.getFancyNumbers(region);
            return Result.success(numbers);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "搜索号码", description = "根据号码模式搜索可用号码")
    @GetMapping("/search")
    public Result<List<BroadbandNumber>> searchNumbers(@RequestParam String pattern) {
        try {
            List<BroadbandNumber> numbers = broadbandNumberService.searchNumbers(pattern);
            return Result.success(numbers);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "锁定号码", description = "选择号码后临时锁定")
    @PostMapping("/{numberId}/lock")
    public Result<BroadbandNumber> lockNumber(@PathVariable Long numberId, @RequestParam Long userId) {
        try {
            BroadbandNumber number = broadbandNumberService.lockNumber(numberId, userId);
            return Result.success("号码已锁定", number);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "解锁号码", description = "解锁已锁定的号码")
    @PostMapping("/{numberId}/unlock")
    public Result<BroadbandNumber> unlockNumber(@PathVariable Long numberId) {
        try {
            BroadbandNumber number = broadbandNumberService.unlockNumber(numberId);
            return Result.success("号码已解锁", number);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
