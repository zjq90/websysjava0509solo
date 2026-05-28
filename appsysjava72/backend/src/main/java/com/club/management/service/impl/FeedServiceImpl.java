package com.club.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.ResultCode;
import com.club.management.entity.ClubFeed;
import com.club.management.entity.FeedComment;
import com.club.management.entity.FeedLike;
import com.club.management.exception.BusinessException;
import com.club.management.mapper.ClubFeedMapper;
import com.club.management.mapper.FeedCommentMapper;
import com.club.management.mapper.FeedLikeMapper;
import com.club.management.service.FeedService;
import com.club.management.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 动态服务实现类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private ClubFeedMapper clubFeedMapper;

    @Autowired
    private FeedLikeMapper feedLikeMapper;

    @Autowired
    private FeedCommentMapper feedCommentMapper;

    @Override
    public PageResult<ClubFeed> getFeedPage(Page<ClubFeed> page, Long clubId) {
        LambdaQueryWrapper<ClubFeed> wrapper = new LambdaQueryWrapper<>();

        if (clubId != null) {
            wrapper.eq(ClubFeed::getClubId, clubId);
        }

        wrapper.orderByDesc(ClubFeed::getCreateTime);

        Page<ClubFeed> feedPage = clubFeedMapper.selectPage(page, wrapper);

        log.debug("分页查询动态列表, 社团ID: {}, 总数: {}", clubId, feedPage.getTotal());

        return new PageResult<>(feedPage.getTotal(), feedPage.getCurrent(), feedPage.getSize(), feedPage.getRecords());
    }

    @Override
    public ClubFeed getFeedDetail(Long id) {
        ClubFeed feed = clubFeedMapper.selectById(id);
        if (feed == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        log.debug("获取动态详情, 动态ID: {}", id);

        return feed;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishFeed(Long clubId, String content, String images) {
        Long userId = UserContext.getUserId();

        ClubFeed feed = new ClubFeed();
        feed.setClubId(clubId);
        feed.setUserId(userId);
        feed.setContent(content);
        feed.setImages(images);
        feed.setLikeCount(0);
        feed.setCommentCount(0);
        clubFeedMapper.insert(feed);

        log.info("发布动态成功, 用户ID: {}, 社团ID: {}, 动态ID: {}", userId, clubId, feed.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleLike(Long feedId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<FeedLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeedLike::getFeedId, feedId)
                .eq(FeedLike::getUserId, userId);
        FeedLike like = feedLikeMapper.selectOne(wrapper);

        ClubFeed feed = clubFeedMapper.selectById(feedId);
        if (feed == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (like != null) {
            feedLikeMapper.deleteById(like.getId());
            if (feed.getLikeCount() > 0) {
                feed.setLikeCount(feed.getLikeCount() - 1);
            }
            log.info("取消点赞成功, 用户ID: {}, 动态ID: {}", userId, feedId);
        } else {
            like = new FeedLike();
            like.setFeedId(feedId);
            like.setUserId(userId);
            feedLikeMapper.insert(like);
            feed.setLikeCount(feed.getLikeCount() + 1);
            log.info("点赞成功, 用户ID: {}, 动态ID: {}", userId, feedId);
        }

        clubFeedMapper.updateById(feed);
    }

    @Override
    public boolean isLiked(Long feedId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<FeedLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeedLike::getFeedId, feedId)
                .eq(FeedLike::getUserId, userId);

        return feedLikeMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(Long feedId, String content, Long parentId, Long replyToId) {
        Long userId = UserContext.getUserId();

        ClubFeed feed = clubFeedMapper.selectById(feedId);
        if (feed == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        FeedComment comment = new FeedComment();
        comment.setFeedId(feedId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setReplyToId(replyToId);
        feedCommentMapper.insert(comment);

        feed.setCommentCount(feed.getCommentCount() + 1);
        clubFeedMapper.updateById(feed);

        log.info("发表评论成功, 用户ID: {}, 动态ID: {}, 评论ID: {}", userId, feedId, comment.getId());
    }

    @Override
    public List<FeedComment> getCommentList(Long feedId) {
        LambdaQueryWrapper<FeedComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeedComment::getFeedId, feedId);
        wrapper.orderByAsc(FeedComment::getCreateTime);

        List<FeedComment> comments = feedCommentMapper.selectList(wrapper);

        log.debug("获取评论列表, 动态ID: {}, 数量: {}", feedId, comments.size());

        return comments;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        Long userId = UserContext.getUserId();

        FeedComment comment = feedCommentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        feedCommentMapper.deleteById(commentId);

        ClubFeed feed = clubFeedMapper.selectById(comment.getFeedId());
        if (feed != null && feed.getCommentCount() > 0) {
            feed.setCommentCount(feed.getCommentCount() - 1);
            clubFeedMapper.updateById(feed);
        }

        log.info("删除评论成功, 用户ID: {}, 评论ID: {}", userId, commentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFeed(Long feedId) {
        Long userId = UserContext.getUserId();

        ClubFeed feed = clubFeedMapper.selectById(feedId);
        if (feed == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (!feed.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        clubFeedMapper.deleteById(feedId);

        log.info("删除动态成功, 用户ID: {}, 动态ID: {}", userId, feedId);
    }
}
