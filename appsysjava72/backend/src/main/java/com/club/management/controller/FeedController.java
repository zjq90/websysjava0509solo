package com.club.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.Result;
import com.club.management.entity.ClubFeed;
import com.club.management.entity.FeedComment;
import com.club.management.service.FeedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 动态控制器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Api(tags = "动态管理")
@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    /**
     * 分页查询动态列表
     */
    @ApiOperation("分页查询动态列表")
    @GetMapping("/page")
    public Result<PageResult<ClubFeed>> getFeedPage(
            @ApiParam(value = "页码", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("社团ID")
            @RequestParam(required = false) Long clubId) {
        Page<ClubFeed> page = new Page<>(pageNum, pageSize);
        PageResult<ClubFeed> pageResult = feedService.getFeedPage(page, clubId);
        return Result.success(pageResult);
    }

    /**
     * 获取动态详情
     */
    @ApiOperation("获取动态详情")
    @GetMapping("/{id}")
    public Result<ClubFeed> getFeedDetail(
            @ApiParam(value = "动态ID", required = true)
            @PathVariable Long id) {
        ClubFeed feed = feedService.getFeedDetail(id);
        return Result.success(feed);
    }

    /**
     * 发布动态
     */
    @ApiOperation("发布动态")
    @PostMapping("/publish")
    public Result<Void> publishFeed(
            @ApiParam(value = "社团ID", required = true)
            @RequestParam Long clubId,
            @ApiParam(value = "动态内容", required = true)
            @RequestParam String content,
            @ApiParam("图片列表，逗号分隔")
            @RequestParam(required = false) String images) {
        feedService.publishFeed(clubId, content, images);
        return Result.success("发布成功", null);
    }

    /**
     * 点赞/取消点赞
     */
    @ApiOperation("点赞/取消点赞")
    @PostMapping("/toggle-like/{feedId}")
    public Result<Void> toggleLike(
            @ApiParam(value = "动态ID", required = true)
            @PathVariable Long feedId) {
        feedService.toggleLike(feedId);
        return Result.success("操作成功", null);
    }

    /**
     * 检查是否已点赞
     */
    @ApiOperation("检查是否已点赞")
    @GetMapping("/check-like/{feedId}")
    public Result<Boolean> isLiked(
            @ApiParam(value = "动态ID", required = true)
            @PathVariable Long feedId) {
        boolean liked = feedService.isLiked(feedId);
        return Result.success(liked);
    }

    /**
     * 发表评论
     */
    @ApiOperation("发表评论")
    @PostMapping("/comment")
    public Result<Void> addComment(
            @ApiParam(value = "动态ID", required = true)
            @RequestParam Long feedId,
            @ApiParam(value = "评论内容", required = true)
            @RequestParam String content,
            @ApiParam("父评论ID")
            @RequestParam(required = false) Long parentId,
            @ApiParam("回复目标用户ID")
            @RequestParam(required = false) Long replyToId) {
        feedService.addComment(feedId, content, parentId, replyToId);
        return Result.success("评论成功", null);
    }

    /**
     * 获取评论列表
     */
    @ApiOperation("获取评论列表")
    @GetMapping("/comments/{feedId}")
    public Result<List<FeedComment>> getCommentList(
            @ApiParam(value = "动态ID", required = true)
            @PathVariable Long feedId) {
        List<FeedComment> comments = feedService.getCommentList(feedId);
        return Result.success(comments);
    }

    /**
     * 删除评论
     */
    @ApiOperation("删除评论")
    @DeleteMapping("/comment/{commentId}")
    public Result<Void> deleteComment(
            @ApiParam(value = "评论ID", required = true)
            @PathVariable Long commentId) {
        feedService.deleteComment(commentId);
        return Result.success("删除成功", null);
    }

    /**
     * 删除动态
     */
    @ApiOperation("删除动态")
    @DeleteMapping("/{feedId}")
    public Result<Void> deleteFeed(
            @ApiParam(value = "动态ID", required = true)
            @PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return Result.success("删除成功", null);
    }
}
