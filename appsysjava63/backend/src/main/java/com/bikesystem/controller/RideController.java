package com.bikesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bikesystem.common.PageResult;
import com.bikesystem.common.Result;
import com.bikesystem.dto.LockRequest;
import com.bikesystem.dto.RideCostResponse;
import com.bikesystem.dto.UnlockRequest;
import com.bikesystem.entity.RideRecord;
import com.bikesystem.mapper.RideRecordMapper;
import com.bikesystem.service.RideService;
import com.bikesystem.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * 骑行控制器
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/ride")
@Tag(name = "骑行模块", description = "开锁、锁车、计费等接口")
@Validated
public class RideController {

    @Resource
    private RideService rideService;

    @Resource
    private RideRecordMapper rideRecordMapper;

    @PostMapping("/unlock")
    @Operation(summary = "开锁骑行", description = "扫码或蓝牙开锁开始骑行")
    public Result<Map<String, Object>> unlockBike(@Valid @RequestBody UnlockRequest request) {
        return Result.success(rideService.unlockBike(request));
    }

    @PostMapping("/lock")
    @Operation(summary = "锁车结算", description = "结束骑行并结算费用")
    public Result<RideCostResponse> lockBike(@Valid @RequestBody LockRequest request) {
        return Result.success(rideService.lockBike(request));
    }

    @GetMapping("/ongoing")
    @Operation(summary = "获取当前骑行", description = "获取用户进行中的骑行记录")
    public Result<RideRecord> getOngoingRide() {
        return Result.success(rideService.getOngoingRide());
    }

    @GetMapping("/current-cost")
    @Operation(summary = "获取实时费用", description = "获取当前骑行的实时费用")
    public Result<RideCostResponse> getCurrentCost() {
        return Result.success(rideService.getCurrentCost());
    }

    @GetMapping("/records")
    @Operation(summary = "骑行记录列表", description = "分页获取用户骑行记录")
    public Result<PageResult<RideRecord>> getRideRecords(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<RideRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RideRecord::getUserId, userId)
                .orderByDesc(RideRecord::getCreateTime);
        
        Page<RideRecord> page = new Page<>(pageNum, pageSize);
        rideRecordMapper.selectPage(page, wrapper);
        
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize));
    }

    @GetMapping("/records/{recordId}")
    @Operation(summary = "骑行记录详情", description = "获取指定骑行记录详情")
    public Result<RideRecord> getRideRecordDetail(
            @Parameter(description = "记录ID", required = true) @PathVariable Long recordId) {
        Long userId = UserContext.getUserId();
        RideRecord record = rideRecordMapper.selectById(recordId);
        
        if (record == null || !userId.equals(record.getUserId())) {
            return Result.error("骑行记录不存在");
        }
        
        return Result.success(record);
    }
}
