package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.CommunityPost;
import com.heritage.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社区控制器
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/community")
@Tag(name = "社区管理", description = "社区帖子相关接口")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @PostMapping("/post")
    @Operation(summary = "发布帖子", description = "发布新帖子，会进行AI内容审核")
    public Result<CommunityPost> createPost(@RequestBody CommunityPost post) {
        try {
            CommunityPost created = communityService.createPost(post);
            if (created.getAiReviewStatus() == 1) {
                return Result.success("帖子发布成功", created);
            } else {
                return Result.success("帖子已提交，AI审核中", created);
            }
        } catch (Exception e) {
            return Result.error("发布失败: " + e.getMessage());
        }
    }

    @GetMapping("/posts")
    @Operation(summary = "获取帖子列表", description = "分页获取已审核通过的帖子")
    public Result<Page<CommunityPost>> getPosts(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        Page<CommunityPost> posts = communityService.getApprovedPosts(page, size);
        return Result.success(posts);
    }

    @GetMapping("/posts/top")
    @Operation(summary = "获取置顶帖子", description = "获取置顶的帖子列表")
    public Result<List<CommunityPost>> getTopPosts() {
        List<CommunityPost> posts = communityService.getTopPosts();
        return Result.success(posts);
    }

    @GetMapping("/post/{id}")
    @Operation(summary = "获取帖子详情", description = "根据ID获取帖子详情")
    public Result<CommunityPost> getPostById(@Parameter(description = "帖子ID") @PathVariable Long id) {
        communityService.incrementViewCount(id);
        CommunityPost post = communityService.getPostById(id);
        if (post != null) {
            return Result.success(post);
        }
        return Result.error("帖子不存在");
    }

    @PostMapping("/post/{id}/like")
    @Operation(summary = "点赞帖子", description = "为帖子点赞")
    public Result<Void> likePost(@Parameter(description = "帖子ID") @PathVariable Long id) {
        communityService.likePost(id);
        return Result.success("点赞成功", null);
    }

    @GetMapping("/posts/user/{userId}")
    @Operation(summary = "获取用户帖子", description = "获取指定用户发布的所有帖子")
    public Result<List<CommunityPost>> getUserPosts(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<CommunityPost> posts = communityService.getUserPosts(userId);
        return Result.success(posts);
    }

    @GetMapping("/posts/search")
    @Operation(summary = "搜索帖子", description = "根据关键词搜索帖子")
    public Result<Page<CommunityPost>> searchPosts(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        Page<CommunityPost> posts = communityService.searchPosts(keyword, page, size);
        return Result.success(posts);
    }

    @DeleteMapping("/post/{id}")
    @Operation(summary = "删除帖子", description = "删除指定帖子")
    public Result<Void> deletePost(@Parameter(description = "帖子ID") @PathVariable Long id) {
        boolean success = communityService.deletePost(id);
        if (success) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }
}
