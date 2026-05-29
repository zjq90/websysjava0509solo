package com.musicplatform.service;

import com.musicplatform.entity.Follow;
import com.musicplatform.entity.Post;
import com.musicplatform.entity.User;
import com.musicplatform.repository.FollowRepository;
import com.musicplatform.repository.PostRepository;
import com.musicplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Transactional
    public Post createPost(Post post, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        post.setUser(user);
        return postRepository.save(post);
    }

    public Page<Post> getPublicPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findAllPublicPosts(pageable);
    }

    public Page<Post> getFollowingPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        List<Long> followingIds = followRepository.findFollowingByFollowerId(userId, Pageable.unpaged())
            .getContent()
            .stream()
            .map(User::getId)
            .collect(Collectors.toList());
        
        followingIds.add(userId);
        
        return postRepository.findFollowingPosts(followingIds, pageable);
    }

    public Page<Post> getUserPosts(Long userId, int page, int size, boolean isOwner, boolean isFollowing) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        if (isOwner) {
            return postRepository.findByUser_Id(userId, pageable);
        } else {
            return postRepository.findUserPublicPosts(userId, pageable);
        }
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("动态不存在"));
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限删除");
        }
        postRepository.delete(post);
    }

    public Map<String, Object> shareToPlatform(Long postId, String platform) {
        Post post = getPostById(postId);
        String shareUrl = "https://musicplatform.com/post/" + postId;
        
        return Map.of(
            "success", true,
            "shareUrl", shareUrl,
            "platform", platform,
            "title", post.getContent() != null ? post.getContent().substring(0, Math.min(50, post.getContent().length())) : "分享动态"
        );
    }
}
