package com.club.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.Result;
import com.club.management.entity.SysUser;
import com.club.management.service.SysUserService;
import com.club.management.vo.SysUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取当前登录用户信息
     */
    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info")
    public Result<SysUserVO> getCurrentUser() {
        SysUserVO userVO = sysUserService.getCurrentUser();
        return Result.success(userVO);
    }

    /**
     * 获取用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/{id}")
    public Result<SysUserVO> getUserById(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long id) {
        SysUserVO userVO = sysUserService.getUserById(id);
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation("更新用户信息")
    @PutMapping("/update")
    public Result<SysUserVO> updateUser(@RequestBody SysUser user) {
        SysUserVO userVO = sysUserService.updateUser(user);
        return Result.success("更新成功", userVO);
    }

    /**
     * 分页查询用户列表
     */
    @ApiOperation("分页查询用户列表")
    @GetMapping("/page")
    public Result<PageResult<SysUserVO>> getUserPage(
            @ApiParam(value = "页码", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("搜索关键词")
            @RequestParam(required = false) String keyword) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        PageResult<SysUserVO> pageResult = sysUserService.getUserPage(page, keyword);
        return Result.success(pageResult);
    }
}
