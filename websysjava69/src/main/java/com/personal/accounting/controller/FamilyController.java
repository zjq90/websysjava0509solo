package com.personal.accounting.controller;

import com.personal.accounting.common.Result;
import com.personal.accounting.entity.Family;
import com.personal.accounting.entity.FamilyMember;
import com.personal.accounting.enums.FamilyRole;
import com.personal.accounting.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "家庭管理", description = "家庭组管理相关接口")
@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @Operation(summary = "获取用户所属家庭列表")
    @GetMapping("/user/{userId}")
    public Result<List<Family>> getFamiliesByUserId(@PathVariable Long userId) {
        return Result.success(familyService.getFamiliesByUserId(userId));
    }

    @Operation(summary = "获取家庭详情")
    @GetMapping("/{id}")
    public Result<Family> getFamilyById(@PathVariable Long id) {
        return Result.success(familyService.getFamilyById(id));
    }

    @Operation(summary = "创建家庭")
    @PostMapping
    public Result<Family> createFamily(@RequestBody Family family, @RequestParam Long creatorId) {
        return Result.success(familyService.createFamily(family, creatorId));
    }

    @Operation(summary = "更新家庭信息")
    @PutMapping("/{id}")
    public Result<Family> updateFamily(@PathVariable Long id, @RequestBody Family family) {
        return Result.success(familyService.updateFamily(id, family));
    }

    @Operation(summary = "删除家庭")
    @DeleteMapping("/{id}")
    public Result<Void> deleteFamily(@PathVariable Long id) {
        familyService.deleteFamily(id);
        return Result.success();
    }

    @Operation(summary = "获取家庭成员列表")
    @GetMapping("/{familyId}/members")
    public Result<List<FamilyMember>> getFamilyMembers(@PathVariable Long familyId) {
        return Result.success(familyService.getFamilyMembers(familyId));
    }

    @Operation(summary = "添加家庭成员（需管理员权限）")
    @PostMapping("/{familyId}/members")
    public Result<FamilyMember> addMember(
            @PathVariable Long familyId,
            @RequestParam Long userId,
            @RequestParam FamilyRole role,
            @RequestParam Long operatorId) {
        return Result.success(familyService.addMember(familyId, userId, role, operatorId));
    }

    @Operation(summary = "移除家庭成员（需管理员权限）")
    @DeleteMapping("/{familyId}/members/{userId}")
    public Result<Void> removeMember(
            @PathVariable Long familyId,
            @PathVariable Long userId,
            @RequestParam Long operatorId) {
        familyService.removeMember(familyId, userId, operatorId);
        return Result.success();
    }

    @Operation(summary = "更新成员角色（需管理员权限）")
    @PutMapping("/{familyId}/members/{userId}/role")
    public Result<FamilyMember> updateMemberRole(
            @PathVariable Long familyId,
            @PathVariable Long userId,
            @RequestParam FamilyRole role,
            @RequestParam Long operatorId) {
        return Result.success(familyService.updateMemberRole(familyId, userId, role, operatorId));
    }

    @Operation(summary = "检查用户是否为家庭成员")
    @GetMapping("/{familyId}/members/{userId}/exists")
    public Result<Boolean> isFamilyMember(@PathVariable Long familyId, @PathVariable Long userId) {
        return Result.success(familyService.isFamilyMember(familyId, userId));
    }

    @Operation(summary = "检查用户是否为家庭管理员")
    @GetMapping("/{familyId}/members/{userId}/is-admin")
    public Result<Boolean> isFamilyAdmin(@PathVariable Long familyId, @PathVariable Long userId) {
        return Result.success(familyService.isFamilyAdmin(familyId, userId));
    }
}
