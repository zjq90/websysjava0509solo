package com.musicplatform.controller;

import com.musicplatform.service.ArtistService;
import com.musicplatform.service.PaymentService;
import com.musicplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/artist")
@CrossOrigin(origins = "*")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getArtistDashboard() {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Map<String, Object> dashboard = artistService.getArtistDashboard(currentUserId);
            return ResponseEntity.ok(successResponse(dashboard));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/stats/music")
    public ResponseEntity<?> getMusicStats(@RequestParam(defaultValue = "30") int days) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        Map<String, Object> stats = artistService.getMusicStats(currentUserId, days);
        return ResponseEntity.ok(successResponse(stats));
    }

    @GetMapping("/stats/fans")
    public ResponseEntity<?> getFanStats() {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        Map<String, Object> stats = artistService.getFanStats(currentUserId);
        return ResponseEntity.ok(successResponse(stats));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getArtistOrders(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        return ResponseEntity.ok(successResponse(paymentService.getArtistOrders(currentUserId, page, size)));
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
