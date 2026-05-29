package com.musicplatform.controller;

import com.musicplatform.entity.Notification;
import com.musicplatform.entity.User;
import com.musicplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            Long currentUserId = userService.getCurrentUserId();
            Map<String, Object> profile = userService.getUserProfile(id, currentUserId);
            return ResponseEntity.ok(successResponse(profile));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        try {
            User user = userService.updateProfile(id, updates);
            return ResponseEntity.ok(successResponse(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        Page<User> followers = userService.getFollowers(id, PageRequest.of(page, size));
        return ResponseEntity.ok(successResponse(followers));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        Page<User> following = userService.getFollowing(id, PageRequest.of(page, size));
        return ResponseEntity.ok(successResponse(following));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<?> followUser(@PathVariable Long id) {
        try {
            Long currentUserId = userService.getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(errorResponse("请先登录"));
            }
            userService.followUser(currentUserId, id);
            return ResponseEntity.ok(successResponse("关注成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id) {
        try {
            Long currentUserId = userService.getCurrentUserId();
            if (currentUserId == null) {
                return ResponseEntity.badRequest().body(errorResponse("请先登录"));
            }
            userService.unfollowUser(currentUserId, id);
            return ResponseEntity.ok(successResponse("取消关注成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String keyword,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        Page<User> users = userService.searchUsers(keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(successResponse(users));
    }

    @GetMapping("/me/notifications")
    public ResponseEntity<?> getNotifications(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        Page<Notification> notifications = userService.getNotifications(currentUserId, PageRequest.of(page, size));
        return ResponseEntity.ok(successResponse(notifications));
    }

    @GetMapping("/me/notifications/unread-count")
    public ResponseEntity<?> getUnreadNotificationCount() {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.ok(successResponse(0));
        }
        long count = userService.getUnreadNotificationCount(currentUserId);
        return ResponseEntity.ok(successResponse(count));
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        try {
            userService.markNotificationAsRead(id);
            return ResponseEntity.ok(successResponse("已标记为已读"));
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
