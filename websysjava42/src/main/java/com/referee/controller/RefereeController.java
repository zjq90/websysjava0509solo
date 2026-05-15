package com.referee.controller;

import com.referee.common.Result;
import com.referee.entity.Referee;
import com.referee.service.RefereeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 裁判控制器
 *
 * @author Referee System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/referee")
@Tag(name = "裁判管理", description = "裁判信息的增删改查接口")
@CrossOrigin(origins = "*")
public class RefereeController {

    @Autowired
    private RefereeService refereeService;

    /**
     * 获取所有裁判列表
     */
    @GetMapping
    @Operation(summary = "获取所有裁判", description = "获取系统中所有裁判的列表")
    public Result<List<Referee>> getAllReferees() {
        return refereeService.getAllReferees();
    }

    /**
     * 根据ID获取裁判信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取裁判信息", description = "根据ID获取裁判详细信息")
    public Result<Referee> getRefereeById(
            @Parameter(description = "裁判ID") @PathVariable Long id) {
        return refereeService.getRefereeById(id);
    }

    /**
     * 新增裁判
     */
    @PostMapping
    @Operation(summary = "新增裁判", description = "添加新的裁判信息，并自动创建登录账号")
    public Result<Referee> addReferee(@RequestBody Referee referee) {
        return refereeService.addReferee(referee);
    }

    /**
     * 更新裁判信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新裁判", description = "更新裁判信息")
    public Result<Referee> updateReferee(
            @Parameter(description = "裁判ID") @PathVariable Long id,
            @RequestBody Referee referee) {
        return refereeService.updateReferee(id, referee);
    }

    /**
     * 删除裁判
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除裁判", description = "删除裁判信息（逻辑删除）")
    public Result<String> deleteReferee(
            @Parameter(description = "裁判ID") @PathVariable Long id) {
        return refereeService.deleteReferee(id);
    }

    /**
     * 获取裁判长列表
     */
    @GetMapping("/chief")
    @Operation(summary = "获取裁判长列表", description = "获取所有在职的裁判长信息")
    public Result<List<Referee>> getChiefReferees() {
        return refereeService.getChiefReferees();
    }

    /**
     * 搜索裁判
     */
    @GetMapping("/search")
    @Operation(summary = "搜索裁判", description = "根据关键词搜索裁判信息")
    public Result<List<Referee>> searchReferees(
            @Parameter(description = "搜索关键词") @RequestParam String keyword) {
        return refereeService.searchReferees(keyword);
    }
}
