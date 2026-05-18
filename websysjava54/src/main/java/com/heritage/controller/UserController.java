package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.User;
import com.heritage.enums.AuditStatus;
import com.heritage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "获取所有用户")
    public Result<List<User>> findAll() {
        return Result.success(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户")
    public Result<User> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(Result::success)
                .orElse(Result.error("用户不存在"));
    }

    @PostMapping
    @Operation(summary = "创建用户")
    public Result<User> create(@RequestBody User user) {
        return Result.success(userService.save(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    public Result<User> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return Result.success(userService.save(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/suspicious")
    @Operation(summary = "获取可疑用户列表")
    public Result<List<User>> getSuspiciousUsers() {
        return Result.success(userService.findSuspiciousUsers());
    }

    @PostMapping("/{id}/mark-suspicious")
    @Operation(summary = "标记用户为可疑")
    public Result<User> markSuspicious(@PathVariable Long id, @RequestParam String reason) {
        return Result.success(userService.markSuspicious(id, reason));
    }

    @PostMapping("/{id}/ignore-suspicious")
    @Operation(summary = "忽略可疑用户")
    public Result<User> ignoreSuspicious(@PathVariable Long id) {
        return Result.success(userService.ignoreSuspicious(id));
    }

    @PostMapping("/{id}/freeze-funds")
    @Operation(summary = "冻结用户资金")
    public Result<User> freezeFunds(@PathVariable Long id, @RequestParam String reason) {
        return Result.success(userService.freezeFunds(id, reason));
    }

    @PostMapping("/{id}/unfreeze-funds")
    @Operation(summary = "解冻用户资金")
    public Result<User> unfreezeFunds(@PathVariable Long id) {
        return Result.success(userService.unfreezeFunds(id));
    }

    @GetMapping("/frozen-funds")
    @Operation(summary = "获取资金被冻结的用户列表")
    public Result<List<User>> getFrozenFundsUsers() {
        return Result.success(userService.findFrozenFundsUsers());
    }

    @GetMapping("/realname-audit/pending")
    @Operation(summary = "获取待实名认证审核列表")
    public Result<List<User>> getRealNameAuditPending() {
        return Result.success(userService.findRealNameAuditPending());
    }

    @PostMapping("/{id}/realname-audit")
    @Operation(summary = "实名认证审核")
    public Result<User> auditRealName(@PathVariable Long id,
                                      @RequestParam AuditStatus status,
                                      @RequestParam(required = false) String remark) {
        return Result.success(userService.auditRealName(id, status, remark));
    }

    @GetMapping("/expert-audit/pending")
    @Operation(summary = "获取待专家认证审核列表")
    public Result<List<User>> getExpertAuditPending() {
        return Result.success(userService.findExpertAuditPending());
    }

    @PostMapping("/{id}/expert-audit")
    @Operation(summary = "审核专家认证")
    public Result<User> auditExpert(@PathVariable Long id,
                                     @RequestParam AuditStatus status,
                                     @RequestParam(required = false) String remark) {
        return Result.success(userService.auditExpert(id, status, remark));
    }

    @GetMapping("/experts")
    @Operation(summary = "获取所有专家")
    public Result<List<User>> getAllExperts() {
        return Result.success(userService.findAllExperts());
    }

    @PutMapping("/{id}/credit-score")
    @Operation(summary = "更新用户信用分")
    public Result<User> updateCreditScore(@PathVariable Long id, @RequestParam Integer score) {
        return Result.success(userService.updateCreditScore(id, score));
    }
}
