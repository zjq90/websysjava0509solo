package com.petclinic.controller;

import com.petclinic.dto.Result;
import com.petclinic.entity.Comment;
import com.petclinic.entity.Post;
import com.petclinic.repository.CommentRepository;
import com.petclinic.service.PetCircleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物圈控制器
 * 包含内容审核功能，防止传播错误医疗信息
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/circle")
@RequiredArgsConstructor
@Tag(name = "宠物圈", description = "宠物社区相关接口，包含内容审核")
public class PetCircleController {

    private final PetCircleService petCircleService;
    private final CommentRepository commentRepository;

    @PostMapping("/post")
    @Operation(summary = "发布帖子", description = "发布养宠经验分享帖子，内容将自动审核")
    public Result<Post> createPost(@RequestBody Post post) {
        return petCircleService.createPost(post);
    }

    @PostMapping("/audit/{postId}")
    @Operation(summary = "审核帖子", description = "管理员审核帖子内容，防止传播错误医疗信息")
    public Result<Post> auditPost(
            @Parameter(description = "帖子ID") @PathVariable Long postId,
            @Parameter(description = "审核人ID") @RequestParam Long auditorId,
            @Parameter(description = "审核状态") @RequestParam String status,
            @Parameter(description = "审核原因") @RequestParam(required = false) String reason) {
        return petCircleService.auditPost(postId, auditorId, status, reason);
    }

    @GetMapping("/posts")
    @Operation(summary = "获取已审核帖子列表", description = "获取审核通过的帖子列表")
    public Result<Page<Post>> getApprovedPosts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        return petCircleService.getApprovedPosts(page, size);
    }

    @GetMapping("/posts/pending")
    @Operation(summary = "获取待审核帖子列表", description = "管理员获取待审核的帖子列表")
    public Result<Page<Post>> getPendingPosts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        return petCircleService.getPendingPosts(page, size);
    }

    @PostMapping("/like/{postId}")
    @Operation(summary = "点赞帖子", description = "为帖子点赞")
    public Result<Post> likePost(
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        return petCircleService.likePost(postId);
    }

    @PostMapping("/view/{postId}")
    @Operation(summary = "增加浏览量", description = "增加帖子浏览量")
    public Result<Post> incrementView(
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        return petCircleService.incrementView(postId);
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "获取帖子详情", description = "根据帖子ID获取详细信息")
    public Result<Post> getPostDetail(
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        return petCircleService.getPostById(postId);
    }

    @GetMapping("/comments/{postId}")
    @Operation(summary = "获取帖子评论列表", description = "获取指定帖子的所有评论")
    public Result<List<Comment>> getComments(
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndDeletedFalseOrderByCreatedTimeDesc(postId);
        return Result.success(comments);
    }

    @PostMapping("/comment")
    @Operation(summary = "发表评论", description = "对帖子发表评论")
    public Result<Comment> createComment(@RequestBody Comment comment) {
        comment.setStatus("APPROVED");
        Comment saved = commentRepository.save(comment);
        return Result.success("评论发表成功", saved);
    }
}