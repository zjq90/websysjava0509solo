package com.club.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.Result;
import com.club.management.dto.ClubApplicationDTO;
import com.club.management.entity.Club;
import com.club.management.entity.ClubApplication;
import com.club.management.entity.ClubCategory;
import com.club.management.service.ClubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 社团控制器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Api(tags = "社团管理")
@RestController
@RequestMapping("/club")
@Validated
public class ClubController {

    @Autowired
    private ClubService clubService;

    /**
     * 获取社团分类列表
     */
    @ApiOperation("获取社团分类列表")
    @GetMapping("/categories")
    public Result<List<ClubCategory>> getCategoryList() {
        List<ClubCategory> categories = clubService.getCategoryList();
        return Result.success(categories);
    }

    /**
     * 分页查询社团列表
     */
    @ApiOperation("分页查询社团列表")
    @GetMapping("/page")
    public Result<PageResult<Club>> getClubPage(
            @ApiParam(value = "页码", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("分类ID")
            @RequestParam(required = false) Long categoryId,
            @ApiParam("搜索关键词")
            @RequestParam(required = false) String keyword) {
        Page<Club> page = new Page<>(pageNum, pageSize);
        PageResult<Club> pageResult = clubService.getClubPage(page, categoryId, keyword);
        return Result.success(pageResult);
    }

    /**
     * 获取社团详情
     */
    @ApiOperation("获取社团详情")
    @GetMapping("/{id}")
    public Result<Club> getClubDetail(
            @ApiParam(value = "社团ID", required = true)
            @PathVariable Long id) {
        Club club = clubService.getClubDetail(id);
        return Result.success(club);
    }

    /**
     * 提交入团申请
     */
    @ApiOperation("提交入团申请")
    @PostMapping("/apply")
    public Result<Void> submitApplication(@Valid @RequestBody ClubApplicationDTO applicationDTO) {
        clubService.submitApplication(applicationDTO);
        return Result.success("申请提交成功", null);
    }

    /**
     * 获取我的入团申请列表
     */
    @ApiOperation("获取我的入团申请列表")
    @GetMapping("/my-applications")
    public Result<List<ClubApplication>> getMyApplications() {
        List<ClubApplication> applications = clubService.getMyApplications();
        return Result.success(applications);
    }

    /**
     * 获取申请详情
     */
    @ApiOperation("获取申请详情")
    @GetMapping("/application/{id}")
    public Result<ClubApplication> getApplicationDetail(
            @ApiParam(value = "申请ID", required = true)
            @PathVariable Long id) {
        ClubApplication application = clubService.getApplicationDetail(id);
        return Result.success(application);
    }

    /**
     * 获取我加入的社团列表
     */
    @ApiOperation("获取我加入的社团列表")
    @GetMapping("/my-clubs")
    public Result<List<Club>> getMyClubs() {
        List<Club> clubs = clubService.getMyClubs();
        return Result.success(clubs);
    }

    /**
     * 检查是否已加入社团
     */
    @ApiOperation("检查是否已加入社团")
    @GetMapping("/check-join/{clubId}")
    public Result<Boolean> isJoinedClub(
            @ApiParam(value = "社团ID", required = true)
            @PathVariable Long clubId) {
        boolean joined = clubService.isJoinedClub(clubId);
        return Result.success(joined);
    }

    /**
     * 退出社团
     */
    @ApiOperation("退出社团")
    @PostMapping("/quit/{clubId}")
    public Result<Void> quitClub(
            @ApiParam(value = "社团ID", required = true)
            @PathVariable Long clubId) {
        clubService.quitClub(clubId);
        return Result.success("退出成功", null);
    }
}
