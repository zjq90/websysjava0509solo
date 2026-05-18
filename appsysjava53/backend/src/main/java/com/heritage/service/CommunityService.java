package com.heritage.service;

import com.heritage.entity.CommunityPost;
import com.heritage.repository.CommunityPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 社区帖子服务类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class CommunityService {

    @Autowired
    private CommunityPostRepository postRepository;

    @Autowired
    private AIReviewService aiReviewService;

    /**
     * 发布帖子
     */
    @Transactional(rollbackFor = Exception.class)
    public CommunityPost createPost(CommunityPost post) {
        AIReviewService.ReviewResult result = aiReviewService.reviewContent(post.getContent() + " " + post.getTitle());
        post.setAiReviewStatus(result.getStatus());
        post.setAiReviewResult(result.getMessage());
        return postRepository.save(post);
    }

    /**
     * 分页查询已审核通过的帖子
     */
    @Cacheable(value = "posts", key = "#page + '-' + #size")
    public Page<CommunityPost> getApprovedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        return postRepository.findByStatusAndAiReviewStatusOrderByCreatedTimeDesc(1, 1, pageable);
    }

    /**
     * 查询置顶帖子
     */
    public List<CommunityPost> getTopPosts() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdTime"));
        return postRepository.findByIsTopAndStatusAndAiReviewStatus(1, 1, 1, pageable).getContent();
    }

    /**
     * 根据ID查询帖子
     */
    @Cacheable(value = "post", key = "#id")
    public CommunityPost getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    /**
     * 增加浏览量
     */
    @CacheEvict(value = "post", key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        CommunityPost post = getPostById(id);
        if (post != null) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }
    }

    /**
     * 点赞帖子
     */
    @CacheEvict(value = "post", key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public void likePost(Long id) {
        CommunityPost post = getPostById(id);
        if (post != null) {
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
        }
    }

    /**
     * 查询用户的帖子列表
     */
    public List<CommunityPost> getUserPosts(Long userId) {
        return postRepository.findByUserIdOrderByCreatedTimeDesc(userId);
    }

    /**
     * 搜索帖子
     */
    public Page<CommunityPost> searchPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.searchByKeyword(keyword, pageable);
    }

    /**
     * 删除帖子
     */
    @CacheEvict(value = "post", key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePost(Long id) {
        CommunityPost post = getPostById(id);
        if (post != null) {
            post.setStatus(0);
            postRepository.save(post);
            return true;
        }
        return false;
    }
}
