package com.musicplatform.service;

import com.musicplatform.entity.Music;
import com.musicplatform.entity.Order;
import com.musicplatform.entity.User;
import com.musicplatform.repository.MusicRepository;
import com.musicplatform.repository.OrderRepository;
import com.musicplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Transactional
    public Order createMusicOrder(Long userId, Long musicId, Order.PaymentMethod paymentMethod) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        Music music = musicRepository.findById(musicId).orElseThrow(() -> new RuntimeException("音乐不存在"));

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUser(user);
        order.setType(Order.OrderType.SINGLE_MUSIC);
        order.setMusicId(musicId);
        order.setMusicTitle(music.getTitle());
        order.setArtistId(music.getArtist().getId());
        order.setArtistName(music.getArtistName());
        order.setAmount(music.getPrice() > 0 ? music.getPrice() : 2.0);
        order.setPaymentMethod(paymentMethod);

        return orderRepository.save(order);
    }

    @Transactional
    public Order createTipOrder(Long userId, Long artistId, double amount, Order.PaymentMethod paymentMethod) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        User artist = userRepository.findById(artistId).orElseThrow(() -> new RuntimeException("用户不存在"));

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUser(user);
        order.setType(Order.OrderType.TIP);
        order.setArtistId(artistId);
        order.setArtistName(artist.getNickname());
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);

        return orderRepository.save(order);
    }

    @Transactional
    public Order createVipOrder(Long userId, int months, Order.PaymentMethod paymentMethod) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

        double monthlyPrice = 15.0;
        double amount = monthlyPrice * months;

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUser(user);
        order.setType(Order.OrderType.VIP_SUBSCRIPTION);
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);

        return orderRepository.save(order);
    }

    @Transactional
    public Map<String, Object> processPayment(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
            .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() == Order.OrderStatus.PAID) {
            return Map.of("success", true, "message", "订单已支付");
        }

        order.setStatus(Order.OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
        order.setPaymentId("mock_payment_" + UUID.randomUUID());
        orderRepository.save(order);

        if (order.getType() == Order.OrderType.VIP_SUBSCRIPTION) {
            User user = order.getUser();
            user.setVip(true);
            user.setVipExpireTime(LocalDateTime.now().plusMonths(1));
            userRepository.save(user);
        }

        return Map.of("success", true, "message", "支付成功");
    }

    public Page<Order> getUserOrders(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Order> getArtistOrders(Long artistId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByArtistIdOrderByCreatedAtDesc(artistId, pageable);
    }

    public double getArtistTotalRevenue(Long artistId) {
        return orderRepository.calculateTotalRevenue(artistId);
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
