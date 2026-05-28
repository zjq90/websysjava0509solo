package com.club.controller;

import com.club.common.PageResult;
import com.club.common.Result;
import com.club.entity.Club;
import com.club.entity.enums.ClubCategoryEnum;
import com.club.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 社团控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/clubs")
@Tag(name = "社团管理", description = "社团浏览、发现、详情等相关接口")
public class ClubController {

    @Autowired
    private ClubService clubService;

    /**
     * 获取社团分类列表
     */
    @GetMapping("/categories")
    @Operation(summary = "获取社团分类", description = "获取所有社团分类列表")
    public Result<List<Map<String, String>>> getCategories() {
        List<Map<String, String>> categories = ClubCategoryEnum.getCategoryList();
        return Result.success(categories);
    }

    /**
     * 获取社团列表（支持筛选）
     */
    @GetMapping
    @Operation(summary = "获取社团列表", description = "分页获取社团列表，支持按分类、学校、关键词筛选")
    public Result<PageResult<Club>> getClubList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "分类编码") @RequestParam(required = false) String category,
            @Parameter(description = "学校ID") @RequestParam(required = false) Long schoolId,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        PageResult<Club> page = clubService.getClubList(pageNum, pageSize, category, schoolId, keyword);
        return Result.success(page);
    }

    /**
     * 获取社团详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取社团详情", description = "根据ID获取社团详细信息")
    public Result<Map<String, Object>> getClubDetail(@PathVariable Long id) {
        Map<String, Object> detail = clubService.getClubDetail(id);
        return Result.success(detail);
    }

    /**
     * 创建社团
     */
    @PostMapping
    @Operation(summary = "创建社团", description = "创建新的社团")
    public Result<Club> createClub(@RequestBody Club club, @RequestAttribute("userId") Long userId) {
        Club saved = clubService.createClub(club, userId);
        return Result.success(saved);
    }

    /**
     * 更新社团信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新社团信息", description = "更新社团基本信息")
    public Result<Club> updateClub(@PathVariable Long id, @RequestBody Club club, @RequestAttribute("userId") Long userId) {
        Club updated = clubService.updateClub(id, club, userId);
        return Result.success(updated);
    }

    /**
     * 获取我管理的社团
     */
    @GetMapping("/my-managed")
    @Operation(summary = "获取我管理的社团", description = "获取当前用户担任管理员的社团列表")
    public Result<List<Club>> getMyManagedClubs(@RequestAttribute("userId") Long userId) {
        List<Club> clubs = clubService.getMyManagedClubs(userId);
        return Result.success(clubs);
    }

    /**
     * 获取热门社团
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门社团", description = "获取热门推荐社团列表")
    public Result<PageResult<Club>> getHotClubs(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Club> page = clubService.getHotClubs(pageNum, pageSize);
        return Result.success(page);
    }
}
