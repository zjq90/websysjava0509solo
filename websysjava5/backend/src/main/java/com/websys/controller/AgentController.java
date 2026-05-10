package com.websys.controller;

import com.websys.common.PageResult;
import com.websys.common.Result;
import com.websys.entity.Agent;
import com.websys.entity.User;
import com.websys.service.AgentService;
import com.websys.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代理商控制器
 * 
 * @author websys
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/agents")
@Tag(name = "代理商管理", description = "代理商相关接口")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private AuthService authService;

    /**
     * 分页查询代理商列表（支持数据隔离）
     * 
     * @param agentName 代理商名称
     * @param agentCode 代理商编码
     * @param parentId 父级代理商ID
     * @param level 代理商级别
     * @param status 状态
     * @param current 当前页
     * @param size 每页大小
     * @param userDetails 用户详情（用于获取当前登录用户信息）
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询代理商列表", description = "支持多条件筛选查询（支持数据隔离）")
    public Result<PageResult<Agent>> getAgentPage(
            @RequestParam(required = false) String agentName,
            @RequestParam(required = false) String agentCode,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        PageResult<Agent> result = agentService.getAgentPage(
            agentName, agentCode, parentId, level, status, current, size,
            currentUser.getRoleType(), currentUser.getAgentId()
        );
        return Result.success(result);
    }

    /**
     * 根据ID查询代理商详情（支持数据隔离校验）
     * 
     * @param id 代理商ID
     * @param userDetails 用户详情
     * @return 代理商信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询代理商详情", description = "根据ID获取代理商详细信息（支持权限校验）")
    public Result<Agent> getAgentById(@PathVariable Long id,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        Agent agent = agentService.getAgentByIdWithPermission(
            id, currentUser.getRoleType(), currentUser.getAgentId()
        );
        return Result.success(agent);
    }

    /**
     * 创建代理商
     * 
     * @param agent 代理商信息
     * @param userDetails 用户详情
     * @return 创建后的代理商
     */
    @PostMapping
    @Operation(summary = "创建代理商", description = "新增代理商信息")
    public Result<Agent> createAgent(@RequestBody Agent agent, 
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        Agent created = agentService.createAgent(agent, currentUser.getId(), currentUser.getUsername());
        return Result.success(created);
    }

    /**
     * 更新代理商
     * 
     * @param agent 代理商信息
     * @param userDetails 用户详情
     * @return 更新后的代理商
     */
    @PutMapping
    @Operation(summary = "更新代理商", description = "修改代理商信息")
    public Result<Agent> updateAgent(@RequestBody Agent agent,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        Agent updated = agentService.updateAgent(agent, currentUser.getId(), currentUser.getUsername());
        return Result.success(updated);
    }

    /**
     * 删除代理商
     * 
     * @param id 代理商ID
     * @param userDetails 用户详情
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除代理商", description = "根据ID删除代理商（无子级时可删除）")
    public Result<Void> deleteAgent(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        agentService.deleteAgent(id, currentUser.getId(), currentUser.getUsername());
        return Result.success();
    }

    /**
     * 获取所有顶级代理商（支持数据隔离）
     * 
     * @param userDetails 用户详情
     * @return 顶级代理商列表
     */
    @GetMapping("/top-level")
    @Operation(summary = "获取顶级代理商", description = "获取所有一级代理商（支持数据隔离）")
    public Result<List<Agent>> getTopLevelAgents(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        List<Agent> agents = agentService.getTopLevelAgentsWithPermission(
            currentUser.getRoleType(), currentUser.getAgentId()
        );
        return Result.success(agents);
    }

    /**
     * 获取子级代理商（支持数据隔离校验）
     * 
     * @param parentId 父级代理商ID
     * @param userDetails 用户详情
     * @return 子级代理商列表
     */
    @GetMapping("/children/{parentId}")
    @Operation(summary = "获取子级代理商", description = "根据父级ID获取所有直接子级代理商（支持权限校验）")
    public Result<List<Agent>> getChildAgents(@PathVariable Long parentId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        List<Agent> agents = agentService.getChildAgentsWithPermission(
            parentId, currentUser.getRoleType(), currentUser.getAgentId()
        );
        return Result.success(agents);
    }

    /**
     * 获取代理商及其所有下级代理商ID（支持数据隔离校验）
     * 
     * @param agentId 代理商ID
     * @param userDetails 用户详情
     * @return ID列表
     */
    @GetMapping("/descendants/{agentId}")
    @Operation(summary = "获取所有下级代理商ID", description = "获取代理商及其所有下级代理商的ID列表（支持权限校验）")
    public Result<List<Long>> getAllDescendantIds(@PathVariable Long agentId,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = authService.getCurrentUser(userDetails.getUsername());
        // 先校验权限
        agentService.getAgentByIdWithPermission(agentId, currentUser.getRoleType(), currentUser.getAgentId());
        List<Long> ids = agentService.getAllDescendantIds(agentId);
        return Result.success(ids);
    }
}
