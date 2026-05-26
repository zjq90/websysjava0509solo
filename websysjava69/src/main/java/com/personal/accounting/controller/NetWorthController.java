package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.dto.NetWorthDTO;
import com.personal.accounting.service.FamilyService;
import com.personal.accounting.service.NetWorthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "净资产统计", description = "净资产计算相关接口")
@RestController
@RequestMapping("/api/net-worth")
@RequiredArgsConstructor
public class NetWorthController {

    private final NetWorthService netWorthService;
    private final FamilyService familyService;

    @Operation(summary = "计算个人净资产")
    @GetMapping("/user/{userId}")
    public Result<NetWorthDTO> calculateNetWorth(@PathVariable Long userId) {
        return Result.success(netWorthService.calculateNetWorth(userId));
    }

    @Operation(summary = "计算家庭净资产（需管理员权限）")
    @GetMapping("/family/{familyId}")
    public Result<NetWorthDTO> calculateFamilyNetWorth(
            @PathVariable Long familyId,
            @RequestParam Long operatorId) {
        if (!familyService.canViewAllRecords(familyId, operatorId)) {
            return Result.error(403, "无权限查看家庭所有成员资产，需要管理员权限");
        }
        List<Long> userIds = familyService.getFamilyMemberIds(familyId);
        return Result.success(netWorthService.calculateFamilyNetWorth(familyId, userIds));
    }
}
