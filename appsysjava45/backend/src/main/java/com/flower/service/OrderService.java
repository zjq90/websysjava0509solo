package com.flower.service;

import com.flower.dto.OrderCreateDTO;
import com.flower.entity.*;
import com.flower.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 订单服务类
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Transactional
    public Map<String, Object> createOrder(Long userId, OrderCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<Cart> cartItems = cartRepository.findByUserIdAndIdIn(userId, dto.getCartItemIds());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车商品不存在");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : cartItems) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在"));
            if (product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("商品【" + product.getName() + "】库存不足");
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
        }

        BigDecimal payAmount = totalAmount;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal freightAmount = calculateFreight(dto.getDeliveryType(), totalAmount);

        if (dto.getCouponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, dto.getCouponId())
                    .orElseThrow(() -> new RuntimeException("优惠券不存在"));
            if (!"unused".equals(userCoupon.getStatus())) {
                throw new RuntimeException("优惠券已使用");
            }
            Coupon coupon = couponRepository.findById(dto.getCouponId())
                    .orElseThrow(() -> new RuntimeException("优惠券不存在"));

            if (totalAmount.compareTo(coupon.getMinAmount()) >= 0) {
                if ("amount".equals(coupon.getType())) {
                    discountAmount = coupon.getValue();
                } else if ("discount".equals(coupon.getType())) {
                    discountAmount = totalAmount.multiply(
                            BigDecimal.ONE.subtract(coupon.getValue().divide(new BigDecimal(10)))
                    );
                }
                payAmount = payAmount.subtract(discountAmount);
            }
        }

        BigDecimal pointsAmount = BigDecimal.ZERO;
        if (dto.getPointsUsed() != null && dto.getPointsUsed() > 0) {
            if (user.getPoints() < dto.getPointsUsed()) {
                throw new RuntimeException("积分不足");
            }
            pointsAmount = new BigDecimal(dto.getPointsUsed()).divide(new BigDecimal(100));
            payAmount = payAmount.subtract(pointsAmount);
            user.setPoints(user.getPoints() - dto.getPointsUsed());
            userRepository.save(user);
        }

        payAmount = payAmount.add(freightAmount);
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }

        String orderNo = generateOrderNo();
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(payAmount);
        order.setFreightAmount(freightAmount);
        order.setDiscountAmount(discountAmount);
        order.setCouponId(dto.getCouponId());
        order.setPointsUsed(dto.getPointsUsed());
        order.setPointsAmount(pointsAmount);
        order.setStatus("pending_payment");
        order.setDeliveryType(dto.getDeliveryType());
        order.setDeliveryTime(dto.getDeliveryTime());
        order.setReceiverName(dto.getReceiverName());
        order.setReceiverPhone(dto.getReceiverPhone());
        order.setReceiverAddress(dto.getReceiverAddress());
        order.setRemark(dto.getRemark());
        order = orderRepository.save(order);

        for (Cart cart : cartItems) {
            Product product = productRepository.findById(cart.getProductId()).get();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImage());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalAmount(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            orderItemRepository.save(orderItem);

            product.setStock(product.getStock() - cart.getQuantity());
            productRepository.save(product);
        }

        cartRepository.deleteAll(cartItems);

        if (dto.getCouponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, dto.getCouponId()).get();
            userCoupon.setStatus("used");
            userCoupon.setUsedTime(LocalDateTime.now());
            userCouponRepository.save(userCoupon);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", order.getOrderNo());
        result.put("payAmount", order.getPayAmount());
        return result;
    }

    private BigDecimal calculateFreight(String deliveryType, BigDecimal totalAmount) {
        if ("express".equals(deliveryType)) {
            if (totalAmount.compareTo(new BigDecimal(99)) >= 0) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(10);
        }
        return BigDecimal.ZERO;
    }

    private String generateOrderNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "FL" + date + uuid;
    }

    public Page<Order> getOrderList(Long userId, String status, Pageable pageable) {
        if (status != null && !status.isEmpty()) {
            return orderRepository.findByUserIdAndStatus(userId, status, pageable);
        }
        return orderRepository.findByUserId(userId, pageable);
    }

    public Order getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权限查看");
        }
        return order;
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional
    public Order payOrder(Long userId, Long orderId, String payMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }
        if (!"pending_payment".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus("producing");
        order.setPayMethod(payMethod);
        order.setPayTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long userId, Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }
        if (!"pending_payment".equals(order.getStatus()) && !"producing".equals(order.getStatus())) {
            throw new RuntimeException("订单无法取消");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId()).get();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        if (order.getPointsUsed() != null && order.getPointsUsed() > 0) {
            User user = userRepository.findById(userId).get();
            user.setPoints(user.getPoints() + order.getPointsUsed());
            userRepository.save(user);
        }

        if (order.getCouponId() != null) {
            UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, order.getCouponId()).get();
            userCoupon.setStatus("unused");
            userCoupon.setUsedTime(null);
            userCouponRepository.save(userCoupon);
        }

        order.setStatus("cancelled");
        order.setCancelReason(reason);
        order.setCancelTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderAddress(Long userId, Long orderId, String receiverName,
                                    String receiverPhone, String receiverAddress) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }
        if ("delivering".equals(order.getStatus()) || "completed".equals(order.getStatus()) || "cancelled".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许修改");
        }

        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        return orderRepository.save(order);
    }

    @Transactional
    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!"producing".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus("delivering");
        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }
        if (!"delivering".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus("completed");
        order.setCompleteTime(LocalDateTime.now());

        User user = userRepository.findById(userId).get();
        int pointsEarned = order.getPayAmount().intValue();
        user.setPoints(user.getPoints() + pointsEarned);
        userRepository.save(user);

        return orderRepository.save(order);
    }
}