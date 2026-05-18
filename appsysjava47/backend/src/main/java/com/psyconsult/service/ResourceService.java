package com.psyconsult.service;

import com.psyconsult.entity.CommunityPost;
import com.psyconsult.entity.Resource;
import com.psyconsult.repository.CommunityPostRepository;
import com.psyconsult.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final CommunityPostRepository communityPostRepository;

    public ResourceService(ResourceRepository resourceRepository,
                           CommunityPostRepository communityPostRepository) {
        this.resourceRepository = resourceRepository;
        this.communityPostRepository = communityPostRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findByEnabledTrueOrderByCreateTimeDesc();
    }

    public List<Resource> getResourcesByType(String type) {
        return resourceRepository.findByTypeAndEnabledTrueOrderByCreateTimeDesc(type);
    }

    public List<Resource> getResourcesByCategory(String category) {
        return resourceRepository.findByCategoryAndEnabledTrueOrderByCreateTimeDesc(category);
    }

    public List<Resource> getPopularResources() {
        return resourceRepository.findByEnabledTrueOrderByViewCountDesc();
    }

    public Resource getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("资源不存在"));
        resource.setViewCount(resource.getViewCount() + 1);
        resourceRepository.save(resource);
        return resource;
    }

    @Transactional
    public CommunityPost createCommunityPost(Long userId, CommunityPost post) {
        post.setUserId(userId);
        post.setStatus("PENDING");
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        return communityPostRepository.save(post);
    }

    public List<CommunityPost> getApprovedPosts() {
        return communityPostRepository.findByStatusOrderByCreateTimeDesc("APPROVED");
    }

    public List<CommunityPost> getUserPosts(Long userId) {
        return communityPostRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public CommunityPost getCommunityPostById(Long id) {
        return communityPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("帖子不存在"));
    }
}
