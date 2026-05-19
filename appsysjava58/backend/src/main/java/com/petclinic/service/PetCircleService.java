package com.petclinic.service;

import com.petclinic.dto.Result;
import com.petclinic.entity.Post;
import com.petclinic.entity.User;
import com.petclinic.repository.PostRepository;
import com.petclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 宠物圈服务类
 * 包含内容审核功能，防止传播错误医疗信息
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PetCircleService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 敏感关键词列表，用于内容审核
     */
    private static final String[] SENSITIVE_KEYWORDS = {
        "偏方", "特效药", "祖传秘方", "无需就医", "包治",
        "根治", "永不复发", "100%治愈", "癌症治愈",
        "自行用药", "代替兽医", "不打针", "不吃药"
    };

    /**
     * 发布帖子
     * 
     * @param post 帖子内容
     * @return 发布结果
     */
    @Transactional
    public Result<Post> createPost(Post post) {
        String auditResult = auditContent(post.getContent() + " " + post.getTitle());
        
        if (auditResult != null) {
            post.setStatus("REJECTED");
            post.setAuditReason(auditResult);
            postRepository.save(post);
            return Result.error("内容审核不通过: " + auditResult);
        }

        post.setStatus("PENDING");
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        
        Post saved = postRepository.save(post);
        log.info("帖子发布成功，ID: {}", saved.getId());
        return Result.success(saved);
    }

    /**
     * 内容审核
     * 检查是否包含敏感医疗关键词
     */
    private String auditContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "内容不能为空";
        }

        String lowerContent = content.toLowerCase();
        for (String keyword : SENSITIVE_KEYWORDS) {
            if (lowerContent.contains(keyword)) {
                return "包含敏感关键词: " + keyword;
            }
        }

        return null;
    }

    /**
     * 审核帖子
     */
    @Transactional
    public Result<Post> auditPost(Long postId, Long auditorId, String status, String reason) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return Result.error("帖子不存在");
        }

        Optional<User> auditorOpt = userRepository.findById(auditorId);
        if (auditorOpt.isEmpty()) {
            return Result.error("审核人不存在");
        }

        Post post = postOpt.get();
        post.setStatus(status);
        post.setAuditReason(reason);
        post.setAuditTime(LocalDateTime.now());
        post.setAuditor(auditorOpt.get());

        Post saved = postRepository.save(post);
        log.info("帖子审核完成，ID: {}, 状态: {}", postId, status);
        return Result.success(saved);
    }

    /**
     * 获取已审核通过的帖子列表
     */
    public Result<Page<Post>> getApprovedPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<Post> posts = postRepository.findByStatusAndDeletedFalse("APPROVED", pageRequest);
        return Result.success(posts);
    }

    /**
     * 获取待审核帖子列表
     */
    public Result<Page<Post>> getPendingPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdTime"));
        Page<Post> posts = postRepository.findByStatusAndDeletedFalse("PENDING", pageRequest);
        return Result.success(posts);
    }

    /**
     * 点赞帖子
     */
    @Transactional
    public Result<Post> likePost(Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return Result.error("帖子不存在");
        }

        Post post = postOpt.get();
        post.setLikeCount(post.getLikeCount() + 1);
        Post saved = postRepository.save(post);
        return Result.success(saved);
    }

    /**
     * 增加浏览量
     */
    @Transactional
    public Result<Post> incrementView(Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return Result.error("帖子不存在");
        }

        Post post = postOpt.get();
        post.setViewCount(post.getViewCount() + 1);
        Post saved = postRepository.save(post);
        return Result.success(saved);
    }

    /**
     * 根据ID获取帖子详情
     */
    public Result<Post> getPostById(Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        return postOpt.map(Result::success).orElse(Result.error("帖子不存在"));
    }
}