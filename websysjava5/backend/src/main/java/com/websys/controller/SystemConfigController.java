package com.websys.controller;

import com.websys.common.PageResult;
import com.websys.common.Result;
import com.websys.entity.SystemConfig;
import com.websys.entity.User;
import com.websys.service.AuthService;
import com.websys.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置控制器
 * 
 * @author websys
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/configs")
@Tag(name = "系统配置", description = "系统配置相关接口")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private AuthService authService;

    /**
     * 分页查询系统配置列表
     * 
     * @param configGroup 配置分组
     * @param configKey 配置键名
     * @param configName 配置名称
     * @param enabled 是否启用
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询系统配置", description = "支持多条件筛选查询系统配置")
    public Result<PageResult<SystemConfig>> getConfigPage(
            @RequestParam(required = false) String configGroup,
            @RequestParam(required = false) String configKey,
            @RequestParam(required = false) String configName,
            @RequestParam(required = false) Integer enabled,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<SystemConfig> result = systemConfigService.getConfigPage(
            configGroup, configKey, configName, enabled, current, size);
        return Result.success(result);
    }

    /**
     * 根据ID查询配置详情
     * 
     * @param id 配置ID
     * @return 配置信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询配置详情", description = "根据ID获取系统配置详细信息")
    public Result<SystemConfig> getConfigById(@PathVariable Long id) {
        SystemConfig config = systemConfigService.getConfigById(id);
        return Result.success(config);
    }

    /**
     * 根据配置键名查询配置
     * 
     * @param configKey 配置键名
     * @return 配置信息
     */
    @GetMapping("/key/{configKey}")
    @Operation(summary = "根据键名查询配置", description = "根据配置键名获取配置信息")
    public Result<SystemConfig> getConfigByKey(@PathVariable String configKey) {
        Optional<SystemConfig> config = systemConfigService.getConfigByKey(configKey);
        return config.map(Result::success).orElseGet(() -> Result.error("配置不存在"));
    }

    /**
     * 创建系统配置
     * 
     * @param config 配置信息
     * @param userDetails 用户详情
     * @return 创建后的配置
     */
    @PostMapping
    @Operation(summary = "创建系统配置", description = "新增系统配置项")
    public Result<SystemConfig> createConfig(@RequestBody SystemConfig config,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        SystemConfig created = systemConfigService.createConfig(config, currentUser.getId(), currentUser.getUsername());
        return Result.success(created);
    }

    /**
     * 更新系统配置
     * 
     * @param config 配置信息
     * @param userDetails 用户详情
     * @return 更新后的配置
     */
    @PutMapping
    @Operation(summary = "更新系统配置", description = "修改系统配置项")
    public Result<SystemConfig> updateConfig(@RequestBody SystemConfig config,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        SystemConfig updated = systemConfigService.updateConfig(config, currentUser.getId(), currentUser.getUsername());
        return Result.success(updated);
    }

    /**
     * 删除系统配置
     * 
     * @param id 配置ID
     * @param userDetails 用户详情
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除系统配置", description = "根据ID删除系统配置（系统内置配置不可删除）")
    public Result<Void> deleteConfig(@PathVariable Long id,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        systemConfigService.deleteConfig(id, currentUser.getId(), currentUser.getUsername());
        return Result.success();
    }

    /**
     * 根据配置分组获取配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    @GetMapping("/group/{configGroup}")
    @Operation(summary = "按分组查询配置", description = "根据配置分组获取该分组下的所有配置")
    public Result<List<SystemConfig>> getConfigsByGroup(@PathVariable String configGroup) {
        List<SystemConfig> configs = systemConfigService.getConfigsByGroup(configGroup);
        return Result.success(configs);
    }

    /**
     * 获取所有配置分组
     * 
     * @return 分组列表
     */
    @GetMapping("/groups")
    @Operation(summary = "获取所有配置分组", description = "获取所有可用的配置分组")
    public Result<List<String>> getAllConfigGroups() {
        List<String> groups = systemConfigService.getAllConfigGroups();
        return Result.success(groups);
    }

    /**
     * 获取所有配置类型
     * 
     * @return 类型列表
     */
    @GetMapping("/types")
    @Operation(summary = "获取所有配置类型", description = "获取所有可用的配置类型")
    public Result<List<String>> getAllConfigTypes() {
        List<String> types = systemConfigService.getAllConfigTypes();
        return Result.success(types);
    }
}
