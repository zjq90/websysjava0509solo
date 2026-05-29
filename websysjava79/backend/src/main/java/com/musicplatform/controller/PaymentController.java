package com.musicplatform.controller;

import com.musicplatform.entity.Order;
import com.musicplatform.service.PaymentService;
import com.musicplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @PostMapping("/music/{musicId}")
    public ResponseEntity<?> createMusicOrder(@PathVariable Long musicId,
                                               @RequestParam String paymentMethod) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Order order = paymentService.createMusicOrder(
                currentUserId, musicId, Order.PaymentMethod.valueOf(paymentMethod));
            return ResponseEntity.ok(successResponse(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/tip/{artistId}")
    public ResponseEntity<?> createTipOrder(@PathVariable Long artistId,
                                             @RequestParam double amount,
                                             @RequestParam String paymentMethod) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Order order = paymentService.createTipOrder(
                currentUserId, artistId, amount, Order.PaymentMethod.valueOf(paymentMethod));
            return ResponseEntity.ok(successResponse(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/vip")
    public ResponseEntity<?> createVipOrder(@RequestParam int months,
                                            @RequestParam String paymentMethod) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Order order = paymentService.createVipOrder(
                currentUserId, months, Order.PaymentMethod.valueOf(paymentMethod));
            return ResponseEntity.ok(successResponse(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{orderNo}/pay")
    public ResponseEntity<?> processPayment(@PathVariable String orderNo) {
        try {
            Map<String, Object> result = paymentService.processPayment(orderNo);
            return ResponseEntity.ok(successResponse(result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        Page<Order> orders = paymentService.getUserOrders(currentUserId, page, size);
        return ResponseEntity.ok(successResponse(orders));
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
