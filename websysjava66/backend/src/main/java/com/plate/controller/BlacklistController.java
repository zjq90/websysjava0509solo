package com.plate.controller;

import com.plate.common.Result;
import com.plate.entity.Blacklist;
import com.plate.service.BlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/blacklist")
@Tag(name = "黑名单管理", description = "黑名单增删改查接口")
public class BlacklistController {
    @Autowired
    private BlacklistService blacklistService;

    @GetMapping
    @Operation(summary = "获取所有黑名单")
    public Result<List<Blacklist>> getAll() {
        return Result.success(blacklistService.getAllBlacklists());
    }

    @GetMapping("/active")
    @Operation(summary = "获取所有生效的黑名单")
    public Result<List<Blacklist>> getActive() {
        return Result.success(blacklistService.getAllActiveBlacklists());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取黑名单")
    public Result<Blacklist> getById(@PathVariable Long id) {
        Optional<Blacklist> blacklist = blacklistService.getBlacklistById(id);
        return blacklist.map(Result::success).orElse(Result.error("黑名单记录不存在"));
    }

    @PostMapping
    @Operation(summary = "添加黑名单")
    public Result<Blacklist> add(@RequestBody Blacklist blacklist) {
        try {
            return Result.success(blacklistService.addToBlacklist(blacklist));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/remove")
    @Operation(summary = "解除黑名单")
    public Result<Blacklist> remove(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        Blacklist blacklist = blacklistService.removeFromBlacklist(id, reason);
        if (blacklist != null) {
            return Result.success(blacklist);
        }
        return Result.error("黑名单记录不存在");
    }

    @GetMapping("/check/{plateNumber}")
    @Operation(summary = "检查车牌号是否在黑名单中")
    public Result<Boolean> checkBlacklisted(@PathVariable String plateNumber) {
        return Result.success(blacklistService.isBlacklisted(plateNumber));
    }
}
