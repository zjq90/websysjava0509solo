package com.club.service;

import com.club.common.PageResult;
import com.club.entity.ClubMember;
import com.club.entity.ClubPost;
import com.club.entity.ClubPostComment;
import com.club.entity.User;
import com.club.entity.enums.MemberRoleEnum;
import com.club.exception.BusinessException;
import com.club.repository.ClubMemberRepository;
import com.club.repository.ClubPostCommentRepository;
import com.club.repository.ClubPostRepository;
import com.club.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 社团讨论区服务
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
public class ClubPostService {

    @Autowired
    private ClubPostRepository clubPostRepository;

    @Autowired
    private ClubPostCommentRepository clubPostCommentRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 发布帖子
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubPost createPost(ClubPost post, Long userId) {
        log.info("发布帖子 - clubId: {}, title: {}, userId: {}", post.getClubId(), post.getTitle(), userId);

        if (!clubMemberRepository.existsByClubIdAndUserIdAndDeletedFalse(post.getClubId(), userId)) {
            throw new BusinessException("只有社团成员才能发布帖子");
        }

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        post.setUserId(userId);
        post.setUsername(user.getUsername());
        post.setRealName(user.getRealName());
        post.setAvatar(user.getAvatar());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setTop(0);
        post.setEssence(0);
        post.setStatus(0);

        ClubPost saved = clubPostRepository.save(post);
        log.info("帖子发布成功 - postId: {}", saved.getId());
        return saved;
    }

    /**
     * 获取帖子列表
     */
    public PageResult<ClubPost> getPostList(Long clubId, String type, Integer pageNum, Integer pageSize) {
        log.info("获取帖子列表 - clubId: {}, type: {}, pageNum: {}, pageSize: {}", clubId, type, pageNum, pageSize);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubPost> page;

        if (type != null && !type.isEmpty()) {
            page = clubPostRepository.findByClubIdAndTypeAndStatusAndDeletedFalseOrderByTopDescCreateTimeDesc(clubId, type, 0, pageable);
        } else {
            page = clubPostRepository.findByClubIdAndStatusAndDeletedFalseOrderByTopDescCreateTimeDesc(clubId, 0, pageable);
        }

        return PageResult.of(page);
    }

    /**
     * 获取帖子详情
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubPost getPostDetail(Long postId) {
        log.info("获取帖子详情 - postId: {}", postId);

        ClubPost post = clubPostRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));

        clubPostRepository.incrementViewCount(postId);

        return post;
    }

    /**
     * 点赞帖子
     */
    @Transactional(rollbackFor = Exception.class)
    public void likePost(Long postId) {
        log.info("点赞帖子 - postId: {}", postId);
        clubPostRepository.incrementLikeCount(postId);
    }

    /**
     * 删除帖子
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long postId, Long userId) {
        log.info("删除帖子 - postId: {}, userId: {}", postId, userId);

        ClubPost post = clubPostRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));

        ClubMember member = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(post.getClubId(), userId).orElse(null);
        boolean isManager = member != null && (member.getRole() == MemberRoleEnum.PRESIDENT || member.getRole() == MemberRoleEnum.VICE_PRESIDENT);

        if (!post.getUserId().equals(userId) && !isManager) {
            throw new BusinessException("只能删除自己发布的帖子，或联系管理员删除");
        }

        post.setDeleted(true);
        post.setStatus(1);
        clubPostRepository.save(post);

        log.info("帖子删除成功 - postId: {}", postId);
    }

    /**
     * 发布评论
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubPostComment createComment(Long postId, ClubPostComment comment, Long userId) {
        log.info("发布评论 - postId: {}, userId: {}", postId, userId);

        ClubPost post = clubPostRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));

        if (!clubMemberRepository.existsByClubIdAndUserIdAndDeletedFalse(post.getClubId(), userId)) {
            throw new BusinessException("只有社团成员才能评论");
        }

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        comment.setPostId(postId);
        comment.setClubId(post.getClubId());
        comment.setUserId(userId);
        comment.setUsername(user.getUsername());
        comment.setRealName(user.getRealName());
        comment.setAvatar(user.getAvatar());
        comment.setLikeCount(0);
        comment.setStatus(0);

        ClubPostComment saved = clubPostCommentRepository.save(comment);

        clubPostRepository.incrementCommentCount(postId);

        log.info("评论发布成功 - commentId: {}", saved.getId());
        return saved;
    }

    /**
     * 获取评论列表
     */
    public PageResult<ClubPostComment> getCommentList(Long postId, Integer pageNum, Integer pageSize) {
        log.info("获取评论列表 - postId: {}, pageNum: {}, pageSize: {}", postId, pageNum, pageSize);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubPostComment> page = clubPostCommentRepository.findByPostIdAndParentIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(postId, 0L, 0, pageable);

        return PageResult.of(page);
    }

    /**
     * 删除评论
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long userId) {
        log.info("删除评论 - commentId: {}, userId: {}", commentId, userId);

        ClubPostComment comment = clubPostCommentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new BusinessException("评论不存在"));

        ClubMember member = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(comment.getClubId(), userId).orElse(null);
        boolean isManager = member != null && (member.getRole() == MemberRoleEnum.PRESIDENT || member.getRole() == MemberRoleEnum.VICE_PRESIDENT);

        if (!comment.getUserId().equals(userId) && !isManager) {
            throw new BusinessException("只能删除自己发布的评论，或联系管理员删除");
        }

        comment.setDeleted(true);
        comment.setStatus(1);
        clubPostCommentRepository.save(comment);

        log.info("评论删除成功 - commentId: {}", commentId);
    }

    /**
     * 置顶帖子
     */
    @Transactional(rollbackFor = Exception.class)
    public void topPost(Long postId, Long userId, Integer top) {
        log.info("置顶帖子 - postId: {}, userId: {}, top: {}", postId, userId, top);

        ClubPost post = clubPostRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));

        ClubMember member = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(post.getClubId(), userId)
                .orElseThrow(() -> new BusinessException("你不是该社团成员"));

        if (member.getRole() != MemberRoleEnum.PRESIDENT && member.getRole() != MemberRoleEnum.VICE_PRESIDENT) {
            throw new BusinessException("权限不足，只有管理员可以置顶帖子");
        }

        post.setTop(top);
        clubPostRepository.save(post);

        log.info("帖子置顶状态更新成功 - postId: {}, top: {}", postId, top);
    }
}
