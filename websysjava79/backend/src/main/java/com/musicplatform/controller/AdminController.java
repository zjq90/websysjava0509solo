package com.musicplatform.controller;

import com.musicplatform.entity.Music;
import com.musicplatform.entity.User;
import com.musicplatform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(successResponse(stats));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        Page<User> users = adminService.getAllUsers(page, size);
        return ResponseEntity.ok(successResponse(users));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        try {
            adminService.updateUserRole(id, User.Role.valueOf(role));
            return ResponseEntity.ok(successResponse("角色更新成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable Long id, @RequestParam boolean blocked) {
        try {
            adminService.blockUser(id, blocked);
            return ResponseEntity.ok(successResponse(blocked ? "已封禁用户" : "已解封用户"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/music/pending")
    public ResponseEntity<?> getPendingMusic(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        Page<Music> music = adminService.getPendingMusic(page, size);
        return ResponseEntity.ok(successResponse(music));
    }

    @PutMapping("/music/{id}/approve")
    public ResponseEntity<?> approveMusic(@PathVariable Long id) {
        try {
            adminService.approveMusic(id);
            return ResponseEntity.ok(successResponse("音乐已通过审核"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PutMapping("/music/{id}/reject")
    public ResponseEntity<?> rejectMusic(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : null;
            adminService.rejectMusic(id, reason);
            return ResponseEntity.ok(successResponse("音乐已拒绝"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PutMapping("/music/{id}/remove")
    public ResponseEntity<?> removeMusic(@PathVariable Long id) {
        try {
            adminService.removeMusic(id);
            return ResponseEntity.ok(successResponse("音乐已下架"));
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
