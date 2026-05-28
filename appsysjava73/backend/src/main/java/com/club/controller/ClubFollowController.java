package com.club.controller;

import com.club.common.PageResult;
import com.club.common.Result;
import com.club.entity.Club;
import com.club.service.ClubFollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 社团关注控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/clubs")
@Tag(name = "社团关注", description = "社团关注、取消关注相关接口")
public class ClubFollowController {

    @Autowired
    private ClubFollowService clubFollowService;

    /**
     * 关注社团
     */
    @PostMapping("/{clubId}/follow")
    @Operation(summary = "关注社团", description = "关注指定社团，关注后可接收该社团的活动通知")
    public Result<Void> followClub(@PathVariable Long clubId, @RequestAttribute("userId") Long userId) {
        clubFollowService.followClub(clubId, userId);
        return Result.success();
    }

    /**
     * 取消关注社团
     */
    @DeleteMapping("/{clubId}/follow")
    @Operation(summary = "取消关注社团", description = "取消关注指定社团")
    public Result<Void> unfollowClub(@PathVariable Long clubId, @RequestAttribute("userId") Long userId) {
        clubFollowService.unfollowClub(clubId, userId);
        return Result.success();
    }

    /**
     * 获取我关注的社团列表
     */
    @GetMapping("/my-followed")
    @Operation(summary = "获取我关注的社团", description = "获取当前用户关注的社团列表")
    public Result<PageResult<Club>> getMyFollowedClubs(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestAttribute("userId") Long userId) {
        PageResult<Club> page = clubFollowService.getMyFollowedClubs(pageNum, pageSize, userId);
        return Result.success(page);
    }

    /**
     * 检查是否已关注社团
     */
    @GetMapping("/{clubId}/follow/check")
    @Operation(summary = "检查关注状态", description = "检查当前用户是否已关注指定社团")
    public Result<Boolean> checkFollowStatus(@PathVariable Long clubId, @RequestAttribute("userId") Long userId) {
        boolean followed = clubFollowService.checkFollowStatus(clubId, userId);
        return Result.success(followed);
    }

    /**
     * 获取社团关注数量
     */
    @GetMapping("/{clubId}/follow/count")
    @Operation(summary = "获取社团关注数", description = "获取指定社团的关注人数")
    public Result<Long> getFollowCount(@PathVariable Long clubId) {
        long count = clubFollowService.getFollowCount(clubId);
        return Result.success(count);
    }
}
