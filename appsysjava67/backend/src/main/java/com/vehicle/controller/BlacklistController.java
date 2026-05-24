package com.vehicle.controller;

import com.vehicle.common.Result;
import com.vehicle.entity.Blacklist;
import com.vehicle.service.BlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/blacklist")
@Tag(name = "黑名单管理", description = "车辆黑名单管理接口")
public class BlacklistController {

    private final BlacklistService blacklistService;

    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @GetMapping
    @Operation(summary = "获取黑名单列表", description = "获取所有黑名单车辆")
    public Result<List<Blacklist>> getAllBlacklist() {
        List<Blacklist> list = blacklistService.getAllBlacklist();
        return Result.success(list);
    }

    @PostMapping
    @Operation(summary = "添加黑名单", description = "将车辆加入黑名单")
    public Result<Blacklist> addBlacklist(@RequestBody Blacklist blacklist) {
        Blacklist saved = blacklistService.addBlacklist(blacklist);
        return Result.success("添加黑名单成功", saved);
    }

    @GetMapping("/{plateNumber}")
    @Operation(summary = "查询黑名单车辆", description = "根据车牌号查询是否在黑名单中")
    public Result<Blacklist> getBlacklist(
            @Parameter(description = "车牌号", required = true)
            @PathVariable String plateNumber) {
        Optional<Blacklist> blacklist = blacklistService.findByPlateNumber(plateNumber);
        return blacklist.map(Result::success)
                .orElse(Result.error("该车辆不在黑名单中"));
    }

    @PutMapping("/{id}/disable")
    @Operation(summary = "移除黑名单", description = "禁用指定的黑名单记录")
    public Result<Blacklist> disableBlacklist(
            @Parameter(description = "黑名单ID", required = true)
            @PathVariable Long id) {
        Blacklist blacklist = blacklistService.disableBlacklist(id);
        if (blacklist != null) {
            return Result.success("已从黑名单移除", blacklist);
        }
        return Result.error("记录不存在");
    }
}
