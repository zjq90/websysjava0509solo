package com.club.management.square.controller;

import com.club.management.common.result.PageResult;
import com.club.management.common.result.Result;
import com.club.management.square.entity.*;
import com.club.management.square.service.SquareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 广场互动Controller
 *
 * @author club-management
 * @version 1.0.0
 */
@Tag(name = "广场互动管理", description = "活动圈、话题、学长分享、评论点赞等接口")
@RestController
@RequestMapping("/square")
@RequiredArgsConstructor
public class SquareController {

    private final SquareService squareService;

    @Operation(summary = "获取活动圈动态列表")
    @GetMapping("/activity/list")
    public Result<PageResult<ActivityPost>> getActivityPostList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(squareService.getActivityPostList(pageNum, pageSize));
    }

    @Operation(summary = "获取用户活动圈动态列表")
    @GetMapping("/activity/user/{userId}")
    public Result<PageResult<ActivityPost>> getActivityPostListByUser(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(squareService.getActivityPostListByUser(userId, pageNum, pageSize));
    }

    @Operation(summary = "发布活动圈动态")
    @PostMapping("/activity/publish")
    public Result<ActivityPost> publishActivityPost(@RequestBody ActivityPost post) {
        return Result.success(squareService.publishActivityPost(post));
    }

    @Operation(summary = "获取活动圈动态详情")
    @GetMapping("/activity/{postId}")
    public Result<ActivityPost> getActivityPostDetail(
            @Parameter(description = "动态ID") @PathVariable Long postId) {
        squareService.increaseActivityPostView(postId);
        return Result.success(squareService.getActivityPostDetail(postId));
    }

    @Operation(summary = "删除活动圈动态")
    @DeleteMapping("/activity/{postId}")
    public Result<Void> deleteActivityPost(
            @Parameter(description = "动态ID") @PathVariable Long postId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        squareService.deleteActivityPost(postId, userId);
        return Result.success();
    }

    @Operation(summary = "获取话题列表")
    @GetMapping("/topic/list")
    public Result<List<Topic>> getTopicList() {
        return Result.success(squareService.getTopicList());
    }

    @Operation(summary = "获取热门话题列表")
    @GetMapping("/topic/hot")
    public Result<List<Topic>> getHotTopicList() {
        return Result.success(squareService.getHotTopicList());
    }

    @Operation(summary = "创建话题")
    @PostMapping("/topic/create")
    public Result<Topic> createTopic(@RequestBody Topic topic) {
        return Result.success(squareService.createTopic(topic));
    }

    @Operation(summary = "获取话题详情")
    @GetMapping("/topic/{topicId}")
    public Result<Topic> getTopicDetail(
            @Parameter(description = "话题ID") @PathVariable Long topicId) {
        return Result.success(squareService.getTopicDetail(topicId));
    }

    @Operation(summary = "获取话题帖子列表")
    @GetMapping("/topic/posts/{topicId}")
    public Result<PageResult<TopicPost>> getTopicPostList(
            @Parameter(description = "话题ID") @PathVariable Long topicId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(squareService.getTopicPostList(topicId, pageNum, pageSize));
    }

    @Operation(summary = "发布话题帖子")
    @PostMapping("/topic/post/publish")
    public Result<TopicPost> publishTopicPost(@RequestBody TopicPost post) {
        return Result.success(squareService.publishTopicPost(post));
    }

    @Operation(summary = "获取话题帖子详情")
    @GetMapping("/topic/post/{postId}")
    public Result<TopicPost> getTopicPostDetail(
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        squareService.increaseTopicPostView(postId);
        return Result.success(squareService.getTopicPostDetail(postId));
    }

    @Operation(summary = "获取学长分享列表")
    @GetMapping("/senior/list")
    public Result<PageResult<SeniorShare>> getSeniorShareList(
            @Parameter(description = "分类 0-社团经验 1-就业指导 2-考研留学 3-技能学习 4-生活感悟") @RequestParam(required = false) Integer category,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(squareService.getSeniorShareList(category, pageNum, pageSize));
    }

    @Operation(summary = "发布学长分享")
    @PostMapping("/senior/publish")
    public Result<SeniorShare> publishSeniorShare(@RequestBody SeniorShare share) {
        return Result.success(squareService.publishSeniorShare(share));
    }

    @Operation(summary = "获取学长分享详情")
    @GetMapping("/senior/{shareId}")
    public Result<SeniorShare> getSeniorShareDetail(
            @Parameter(description = "分享ID") @PathVariable Long shareId) {
        squareService.increaseSeniorShareView(shareId);
        return Result.success(squareService.getSeniorShareDetail(shareId));
    }

    @Operation(summary = "获取评论列表")
    @GetMapping("/comment/list")
    public Result<PageResult<Comment>> getCommentList(
            @Parameter(description = "业务类型 0-活动圈 1-话题帖子 2-学长分享") @RequestParam Integer businessType,
            @Parameter(description = "业务ID") @RequestParam Long businessId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(squareService.getCommentList(businessType, businessId, pageNum, pageSize));
    }

    @Operation(summary = "获取回复评论列表")
    @GetMapping("/comment/reply/{parentId}")
    public Result<List<Comment>> getReplyCommentList(
            @Parameter(description = "父评论ID") @PathVariable Long parentId) {
        return Result.success(squareService.getReplyCommentList(parentId));
    }

    @Operation(summary = "发布评论")
    @PostMapping("/comment/publish")
    public Result<Comment> publishComment(@RequestBody Comment comment) {
        return Result.success(squareService.publishComment(comment));
    }

    @Operation(summary = "点赞/取消点赞")
    @PostMapping("/like/toggle")
    public Result<Void> toggleLike(@RequestBody Map<String, Object> params) {
        Integer businessType = Integer.valueOf(params.get("businessType").toString());
        Long businessId = Long.valueOf(params.get("businessId").toString());
        Long userId = Long.valueOf(params.get("userId").toString());
        String username = (String) params.get("username");
        squareService.toggleLike(businessType, businessId, userId, username);
        return Result.success();
    }

    @Operation(summary = "检查是否已点赞")
    @GetMapping("/like/check")
    public Result<Boolean> isLiked(
            @Parameter(description = "业务类型 0-活动圈 1-话题帖子 2-学长分享 3-评论") @RequestParam Integer businessType,
            @Parameter(description = "业务ID") @RequestParam Long businessId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return Result.success(squareService.isLiked(businessType, businessId, userId));
    }
}
