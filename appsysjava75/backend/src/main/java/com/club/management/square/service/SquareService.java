package com.club.management.square.service;

import com.club.management.common.exception.BusinessException;
import com.club.management.common.result.PageResult;
import com.club.management.square.entity.*;
import com.club.management.square.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 广场互动Service
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SquareService {

    private final ActivityPostRepository activityPostRepository;
    private final TopicRepository topicRepository;
    private final TopicPostRepository topicPostRepository;
    private final SeniorShareRepository seniorShareRepository;
    private final CommentRepository commentRepository;
    private final LikeRecordRepository likeRecordRepository;

    public PageResult<ActivityPost> getActivityPostList(Integer pageNum, Integer pageSize) {
        log.info("获取活动圈动态列表，页码：{}，每页大小：{}", pageNum, pageSize);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ActivityPost> page = activityPostRepository.findByStatusOrderByCreateTimeDesc(0, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    public PageResult<ActivityPost> getActivityPostListByUser(Long userId, Integer pageNum, Integer pageSize) {
        log.info("获取用户活动圈动态列表，用户ID：{}", userId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ActivityPost> page = activityPostRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public ActivityPost publishActivityPost(ActivityPost post) {
        log.info("发布活动圈动态，用户ID：{}", post.getUserId());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setViewCount(0);
        post.setStatus(0);
        ActivityPost saved = activityPostRepository.save(post);
        log.info("活动圈动态发布成功，动态ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void increaseActivityPostView(Long postId) {
        activityPostRepository.increaseViewCount(postId);
    }

    public ActivityPost getActivityPostDetail(Long postId) {
        log.info("获取活动圈动态详情，动态ID：{}", postId);
        return activityPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("动态不存在"));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteActivityPost(Long postId, Long userId) {
        log.info("删除活动圈动态，动态ID：{}，用户ID：{}", postId, userId);
        ActivityPost post = activityPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("动态不存在"));
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除");
        }
        post.setStatus(1);
        activityPostRepository.save(post);
    }

    public List<Topic> getTopicList() {
        log.info("获取话题列表");
        return topicRepository.findByStatusOrderByIsTopDescFollowCountDesc(0);
    }

    public List<Topic> getHotTopicList() {
        log.info("获取热门话题列表");
        return topicRepository.findByIsHotAndStatus(1, 0);
    }

    @Transactional(rollbackFor = Exception.class)
    public Topic createTopic(Topic topic) {
        log.info("创建话题，话题名称：{}", topic.getName());
        topic.setPostCount(0);
        topic.setFollowCount(0);
        topic.setViewCount(0);
        topic.setIsHot(0);
        topic.setIsTop(0);
        topic.setStatus(0);
        Topic saved = topicRepository.save(topic);
        log.info("话题创建成功，话题ID：{}", saved.getId());
        return saved;
    }

    public Topic getTopicDetail(Long topicId) {
        log.info("获取话题详情，话题ID：{}", topicId);
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new BusinessException("话题不存在"));
    }

    public PageResult<TopicPost> getTopicPostList(Long topicId, Integer pageNum, Integer pageSize) {
        log.info("获取话题帖子列表，话题ID：{}", topicId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<TopicPost> page = topicPostRepository.findByTopicIdAndStatusOrderByIsTopDescCreateTimeDesc(topicId, 0, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public TopicPost publishTopicPost(TopicPost post) {
        log.info("发布话题帖子，话题ID：{}，用户ID：{}", post.getTopicId(), post.getUserId());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setViewCount(0);
        post.setIsEssence(0);
        post.setIsTop(0);
        post.setStatus(0);
        TopicPost saved = topicPostRepository.save(post);

        Topic topic = topicRepository.findById(post.getTopicId()).orElse(null);
        if (topic != null) {
            topic.setPostCount(topic.getPostCount() + 1);
            topicRepository.save(topic);
        }
        log.info("话题帖子发布成功，帖子ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void increaseTopicPostView(Long postId) {
        topicPostRepository.increaseViewCount(postId);
    }

    public TopicPost getTopicPostDetail(Long postId) {
        log.info("获取话题帖子详情，帖子ID：{}", postId);
        return topicPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));
    }

    public PageResult<SeniorShare> getSeniorShareList(Integer category, Integer pageNum, Integer pageSize) {
        log.info("获取学长分享列表，分类：{}", category);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<SeniorShare> page;
        if (category != null) {
            page = seniorShareRepository.findByCategoryAndStatusOrderByIsTopDescCreateTimeDesc(category, 0, pageable);
        } else {
            page = seniorShareRepository.findByStatusOrderByIsTopDescCreateTimeDesc(0, pageable);
        }
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public SeniorShare publishSeniorShare(SeniorShare share) {
        log.info("发布学长分享，标题：{}", share.getTitle());
        share.setLikeCount(0);
        share.setCommentCount(0);
        share.setFavoriteCount(0);
        share.setViewCount(0);
        share.setIsEssence(0);
        share.setIsTop(0);
        share.setStatus(0);
        SeniorShare saved = seniorShareRepository.save(share);
        log.info("学长分享发布成功，分享ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void increaseSeniorShareView(Long shareId) {
        seniorShareRepository.increaseViewCount(shareId);
    }

    public SeniorShare getSeniorShareDetail(Long shareId) {
        log.info("获取学长分享详情，分享ID：{}", shareId);
        return seniorShareRepository.findById(shareId)
                .orElseThrow(() -> new BusinessException("分享不存在"));
    }

    public PageResult<Comment> getCommentList(Integer businessType, Long businessId, Integer pageNum, Integer pageSize) {
        log.info("获取评论列表，业务类型：{}，业务ID：{}", businessType, businessId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Comment> page = commentRepository.findByBusinessTypeAndBusinessIdAndParentIdIsNullOrderByCreateTimeDesc(
                businessType, businessId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    public List<Comment> getReplyCommentList(Long parentId) {
        log.info("获取回复评论列表，父评论ID：{}", parentId);
        return commentRepository.findByParentIdOrderByCreateTimeAsc(parentId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Comment publishComment(Comment comment) {
        log.info("发布评论，业务类型：{}，业务ID：{}", comment.getBusinessType(), comment.getBusinessId());
        comment.setLikeCount(0);
        comment.setStatus(0);
        Comment saved = commentRepository.save(comment);

        if (comment.getBusinessType() == 0) {
            activityPostRepository.increaseCommentCount(comment.getBusinessId());
        } else if (comment.getBusinessType() == 1) {
            topicPostRepository.increaseCommentCount(comment.getBusinessId());
        } else if (comment.getBusinessType() == 2) {
            seniorShareRepository.increaseCommentCount(comment.getBusinessId());
        }
        log.info("评论发布成功，评论ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void toggleLike(Integer businessType, Long businessId, Long userId, String username) {
        log.info("点赞/取消点赞，业务类型：{}，业务ID：{}，用户ID：{}", businessType, businessId, userId);
        LikeRecord existing = likeRecordRepository.findByBusinessTypeAndBusinessIdAndUserId(businessType, businessId, userId);
        if (existing != null) {
            likeRecordRepository.delete(existing);
            if (businessType == 0) {
                activityPostRepository.decreaseLikeCount(businessId);
            } else if (businessType == 1) {
                topicPostRepository.decreaseLikeCount(businessId);
            } else if (businessType == 2) {
                seniorShareRepository.decreaseLikeCount(businessId);
            } else if (businessType == 3) {
                commentRepository.decreaseLikeCount(businessId);
            }
        } else {
            LikeRecord record = new LikeRecord();
            record.setBusinessType(businessType);
            record.setBusinessId(businessId);
            record.setUserId(userId);
            record.setUsername(username);
            likeRecordRepository.save(record);
            if (businessType == 0) {
                activityPostRepository.increaseLikeCount(businessId);
            } else if (businessType == 1) {
                topicPostRepository.increaseLikeCount(businessId);
            } else if (businessType == 2) {
                seniorShareRepository.increaseLikeCount(businessId);
            } else if (businessType == 3) {
                commentRepository.increaseLikeCount(businessId);
            }
        }
    }

    public boolean isLiked(Integer businessType, Long businessId, Long userId) {
        return likeRecordRepository.findByBusinessTypeAndBusinessIdAndUserId(businessType, businessId, userId) != null;
    }
}
