package com.musicplatform.controller;

import com.musicplatform.entity.Post;
import com.musicplatform.service.PostService;
import com.musicplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getPublicPosts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        Page<Post> posts = postService.getPublicPosts(page, size);
        return ResponseEntity.ok(successResponse(posts));
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowingPosts(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        Page<Post> posts = postService.getFollowingPosts(currentUserId, page, size);
        return ResponseEntity.ok(successResponse(posts));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPosts(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = userService.getCurrentUserId();
        boolean isOwner = currentUserId != null && currentUserId.equals(userId);
        boolean isFollowing = currentUserId != null;
        Page<Post> posts = postService.getUserPosts(userId, page, size, isOwner, isFollowing);
        return ResponseEntity.ok(successResponse(posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);
            return ResponseEntity.ok(successResponse(post));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        Post created = postService.createPost(post, currentUserId);
        return ResponseEntity.ok(successResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            postService.deletePost(id, currentUserId);
            return ResponseEntity.ok(successResponse("删除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<?> sharePost(@PathVariable Long id, @RequestParam String platform) {
        try {
            Map<String, Object> result = postService.shareToPlatform(id, platform);
            return ResponseEntity.ok(successResponse(result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    private Map<String, Object> successResponse(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        return result;
    }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
