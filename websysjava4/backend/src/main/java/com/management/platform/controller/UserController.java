package com.management.platform.controller;

import com.management.platform.entity.User;
import com.management.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 提供用户CRUD和统计分析的REST API
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "用户管理", description = "用户的增删改查及统计分析接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建新用户
     * @param user 用户信息
     * @return 创建后的用户
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建一个新的用户")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    /**
     * 根据ID获取用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * 分页获取用户列表
     * @param page 页码，从0开始
     * @param size 每页大小
     * @return 用户分页列表
     */
    @GetMapping
    @Operation(summary = "获取用户列表", description = "分页获取所有用户列表")
    public ResponseEntity<Page<User>> getAllUsers(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 更新的用户信息
     * @return 更新后的用户
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新指定用户的信息")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除指定用户")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * 获取活跃用户数统计
     * @param days 统计天数
     * @return 活跃用户统计数据
     */
    @GetMapping("/stats/active")
    @Operation(summary = "活跃用户统计", description = "获取指定时间范围内的活跃用户统计")
    public ResponseEntity<Map<String, Object>> getActiveUsersStatistics(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(userService.getActiveUsersStatistics(days));
    }

    /**
     * 获取复购率统计
     * @return 复购率统计数据
     */
    @GetMapping("/stats/repurchase-rate")
    @Operation(summary = "复购率统计", description = "获取用户复购率统计数据")
    public ResponseEntity<Map<String, Object>> getRepurchaseRateStatistics() {
        return ResponseEntity.ok(userService.getRepurchaseRateStatistics());
    }

    /**
     * 获取新用户增长趋势
     * @return 新用户增长趋势数据
     */
    @GetMapping("/stats/growth-trend")
    @Operation(summary = "新用户增长趋势", description = "获取最近30天的新用户增长趋势")
    public ResponseEntity<List<Map<String, Object>>> getNewUserGrowthTrend() {
        return ResponseEntity.ok(userService.getNewUserGrowthTrend());
    }

    /**
     * 获取购买时段分布热力图数据
     * @param days 统计天数
     * @return 购买时段分布数据
     */
    @GetMapping("/stats/purchase-heatmap")
    @Operation(summary = "购买时段热力图", description = "获取购买时段分布热力图数据")
    public ResponseEntity<Map<String, Object>> getPurchaseTimeHeatmap(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(userService.getPurchaseTimeHeatmap(days));
    }
}
