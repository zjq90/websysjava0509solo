package com.flowerstore.backend.service;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.*;
import com.flowerstore.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 订单服务类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberLevelRepository memberLevelRepository;

    @Autowired
    private PointRecordRepository pointRecordRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    /**
     * 创建订单
     */
    @Transactional
    public Result<Map<String, Object>> createOrder(Long userId, List<Map<String, Object>> items, 
            Long addressId, Long couponId, String remark) {
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Result.error("用户不存在");
        }
        User user = userOpt.get();

        if (items == null || items.isEmpty()) {
            return Result.error("订单项不能为空");
        }

        String orderNo = "ORD" + System.currentTimeMillis() + new Random().nextInt(1000);
        
        long totalAmount = 0;
        long discountAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            Integer quantity = Integer.valueOf(item.get("quantity").toString());

            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return Result.error("产品不存在: " + productId);
            }
            Product product = productOpt.get();

            if (product.getStock() < quantity) {
                return Result.error("库存不足: " + product.getName());
            }

            long itemAmount = product.getPrice() * quantity;
            totalAmount += itemAmount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(quantity);
            orderItem.setSubtotal(itemAmount);
            orderItems.add(orderItem);

            product.setStock(product.getStock() - quantity);
            product.setSales(product.getSales() + quantity);
            productRepository.save(product);
        }

        if (couponId != null) {
            List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponId(userId, couponId);
            if (!userCoupons.isEmpty()) {
                UserCoupon userCoupon = userCoupons.get(0);
                if (userCoupon.getStatus() == 0) {
                    Optional<Coupon> couponOpt = couponRepository.findById(couponId);
                    if (couponOpt.isPresent()) {
                        Coupon coupon = couponOpt.get();
                        if (coupon.getType() == 1 && totalAmount >= coupon.getMinAmount()) {
                            discountAmount = coupon.getValue();
                        } else if (coupon.getType() == 2 && coupon.getDiscountRate() != null) {
                            discountAmount = totalAmount * (100 - coupon.getDiscountRate()) / 100;
                        }
                        userCoupon.setStatus(1);
                        userCoupon.setUseTime(LocalDateTime.now());
                        userCouponRepository.save(userCoupon);
                    }
                }
            }
        }

        MemberLevel memberLevel = memberLevelRepository.findById(user.getMemberLevelId()).orElse(null);
        if (memberLevel != null && memberLevel.getDiscountRate() != null) {
            long memberDiscount = totalAmount * (100 - memberLevel.getDiscountRate()) / 100;
            discountAmount += memberDiscount;
        }

        long payAmount = totalAmount - discountAmount;
        if (payAmount < 0) payAmount = 0;

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPayAmount(payAmount);
        order.setStatus(1);
        order.setRemark(remark);
        order.setPayTime(LocalDateTime.now());
        order = orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemRepository.save(item);
        }

        int pointsEarned = (int) (payAmount / 100);
        user.setCurrentPoints(user.getCurrentPoints() + pointsEarned);
        user.setTotalConsume(user.getTotalConsume() + payAmount);
        
        checkAndUpgradeMemberLevel(user);
        userRepository.save(user);

        addPointRecord(userId, pointsEarned, 1, "消费获得积分", order.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", orderItems);
        result.put("pointsEarned", pointsEarned);

        return Result.success("订单创建成功", result);
    }

    /**
     * 检查并升级会员等级
     */
    private void checkAndUpgradeMemberLevel(User user) {
        List<MemberLevel> levels = memberLevelRepository.findByStatusOrderByUpgradeAmountDesc(1);
        for (MemberLevel level : levels) {
            if (user.getTotalConsume() >= level.getUpgradeAmount() && 
                !user.getMemberLevelId().equals(level.getId())) {
                user.setMemberLevelId(level.getId());
                break;
            }
        }
    }

    /**
     * 获取用户订单列表
     */
    public Result<List<Order>> getUserOrders(Long userId, Integer status) {
        List<Order> orders;
        if (status != null) {
            orders = orderRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, status);
        } else {
            orders = orderRepository.findByUserIdOrderByCreateTimeDesc(userId);
        }
        return Result.success(orders);
    }

    /**
     * 获取订单详情
     */
    public Result<Map<String, Object>> getOrderDetail(Long userId, Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Result.error("订单不存在");
        }
        Order order = orderOpt.get();
        
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权访问该订单");
        }

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        
        return Result.success(result);
    }

    /**
     * 取消订单
     */
    @Transactional
    public Result<String> cancelOrder(Long userId, Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Result.error("订单不存在");
        }
        Order order = orderOpt.get();
        
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作该订单");
        }
        
        if (order.getStatus() != 1) {
            return Result.error("当前订单状态无法取消");
        }

        order.setStatus(6);
        order.setCancelTime(LocalDateTime.now());
        orderRepository.save(order);

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                product.setStock(product.getStock() + item.getQuantity());
                product.setSales(product.getSales() - item.getQuantity());
                productRepository.save(product);
            }
        }

        return Result.success("订单取消成功");
    }

    /**
     * 确认收货
     */
    @Transactional
    public Result<String> confirmReceipt(Long userId, Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Result.error("订单不存在");
        }
        Order order = orderOpt.get();
        
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作该订单");
        }
        
        if (order.getStatus() != 3) {
            return Result.error("当前订单状态无法确认收货");
        }

        order.setStatus(4);
        order.setConfirmTime(LocalDateTime.now());
        orderRepository.save(order);

        return Result.success("确认收货成功");
    }

    /**
     * 添加积分记录
     */
    private void addPointRecord(Long userId, Integer points, Integer type, String description, Long orderId) {
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(type);
        record.setDescription(description);
        record.setOrderId(orderId);
        pointRecordRepository.save(record);
    }
}
