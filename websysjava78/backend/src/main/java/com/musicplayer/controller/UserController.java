package com.musicplayer.controller;

import com.musicplayer.entity.User;
import com.musicplayer.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "用户未登录"));
        }
        User user = userService.getUserById(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("nickname", user.getNickname());
        response.put("email", user.getEmail());
        response.put("avatar", user.getAvatar());
        response.put("role", user.getRole());
        response.put("remainingDownloads", userService.getRemainingDownloads(user));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(HttpServletRequest request, @RequestBody User userDetails) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "用户未登录"));
        }
        User updated = userService.updateUser(userId, userDetails);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/{id}/upgrade")
    public ResponseEntity<?> upgradeToPremium(@PathVariable Long id) {
        User user = userService.upgradeToPremium(id);
        return ResponseEntity.ok(Map.of(
                "message", "升级成功",
                "role", user.getRole()
        ));
    }

    @GetMapping("/me/downloads/remaining")
    public ResponseEntity<?> getRemainingDownloads(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "用户未登录"));
        }
        User user = userService.getUserById(userId);
        int remaining = userService.getRemainingDownloads(user);
        return ResponseEntity.ok(Map.of(
                "remaining", remaining,
                "role", user.getRole(),
                "limit", user.getRole() == User.UserRole.PREMIUM ? 100 : 5
        ));
    }
}
