package com.club.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.entity.ClubFeed;
import com.club.management.entity.FeedComment;

import java.util.List;

/**
 * 动态服务接口
 *
 * @author club-management
 * @since 2024-01-01
 */
public interface FeedService {

    /**
     * 分页查询动态列表
     */
    PageResult<ClubFeed> getFeedPage(Page<ClubFeed> page, Long clubId);

    /**
     * 获取动态详情
     */
    ClubFeed getFeedDetail(Long id);

    /**
     * 发布动态
     */
    void publishFeed(Long clubId, String content, String images);

    /**
     * 点赞/取消点赞
     */
    void toggleLike(Long feedId);

    /**
     * 检查是否已点赞
     */
    boolean isLiked(Long feedId);

    /**
     * 发表评论
     */
    void addComment(Long feedId, String content, Long parentId, Long replyToId);

    /**
     * 获取评论列表
     */
    List<FeedComment> getCommentList(Long feedId);

    /**
     * 删除评论
     */
    void deleteComment(Long commentId);

    /**
     * 删除动态
     */
    void deleteFeed(Long feedId);
}
