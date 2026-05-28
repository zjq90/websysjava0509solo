package com.club.controller;

import com.club.common.PageResult;
import com.club.common.Result;
import com.club.entity.ClubPost;
import com.club.entity.ClubPostComment;
import com.club.service.ClubPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 社团讨论区控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/clubs/{clubId}/posts")
@Tag(name = "社团讨论区", description = "社团讨论区帖子、评论相关接口")
public class ClubPostController {

    @Autowired
    private ClubPostService clubPostService;

    /**
     * 发布帖子
     */
    @PostMapping
    @Operation(summary = "发布帖子", description = "社团成员发布讨论帖子")
    public Result<ClubPost> createPost(
            @PathVariable Long clubId,
            @RequestBody ClubPost post,
            @RequestAttribute("userId") Long userId) {
        post.setClubId(clubId);
        ClubPost saved = clubPostService.createPost(post, userId);
        return Result.success(saved);
    }

    /**
     * 获取帖子列表
     */
    @GetMapping
    @Operation(summary = "获取帖子列表", description = "获取社团讨论区帖子列表")
    public Result<PageResult<ClubPost>> getPostList(
            @PathVariable Long clubId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String type) {
        PageResult<ClubPost> page = clubPostService.getPostList(clubId, type, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 获取帖子详情
     */
    @GetMapping("/{postId}")
    @Operation(summary = "获取帖子详情", description = "获取帖子详细信息")
    public Result<ClubPost> getPostDetail(@PathVariable Long clubId, @PathVariable Long postId) {
        ClubPost post = clubPostService.getPostDetail(postId);
        return Result.success(post);
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/{postId}/like")
    @Operation(summary = "点赞帖子", description = "给帖子点赞")
    public Result<Void> likePost(@PathVariable Long clubId, @PathVariable Long postId) {
        clubPostService.likePost(postId);
        return Result.success();
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/{postId}")
    @Operation(summary = "删除帖子", description = "删除帖子，管理员可删除违规内容")
    public Result<Void> deletePost(
            @PathVariable Long clubId,
            @PathVariable Long postId,
            @RequestAttribute("userId") Long userId) {
        clubPostService.deletePost(postId, userId);
        return Result.success();
    }

    /**
     * 置顶帖子
     */
    @PutMapping("/{postId}/top")
    @Operation(summary = "置顶帖子", description = "管理员置顶/取消置顶帖子")
    public Result<Void> topPost(
            @PathVariable Long clubId,
            @PathVariable Long postId,
            @RequestBody Map<String, Integer> data,
            @RequestAttribute("userId") Long userId) {
        Integer top = data.get("top");
        clubPostService.topPost(postId, userId, top);
        return Result.success();
    }

    /**
     * 发布评论
     */
    @PostMapping("/{postId}/comments")
    @Operation(summary = "发布评论", description = "给帖子发表评论")
    public Result<ClubPostComment> createComment(
            @PathVariable Long clubId,
            @PathVariable Long postId,
            @RequestBody ClubPostComment comment,
            @RequestAttribute("userId") Long userId) {
        comment.setPostId(postId);
        ClubPostComment saved = clubPostService.createComment(postId, comment, userId);
        return Result.success(saved);
    }

    /**
     * 获取评论列表
     */
    @GetMapping("/{postId}/comments")
    @Operation(summary = "获取评论列表", description = "获取帖子的评论列表")
    public Result<PageResult<ClubPostComment>> getCommentList(
            @PathVariable Long clubId,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<ClubPostComment> page = clubPostService.getCommentList(postId, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "删除评论", description = "删除评论，管理员可删除违规评论")
    public Result<Void> deleteComment(
            @PathVariable Long clubId,
            @PathVariable Long commentId,
            @RequestAttribute("userId") Long userId) {
        clubPostService.deleteComment(commentId, userId);
        return Result.success();
    }
}
