package com.secondhand.service;

import com.secondhand.entity.Order;
import com.secondhand.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> findAll(String orderNo, String status, Boolean isAbnormal, 
                                Long buyerId, Long sellerId, Pageable pageable) {
        Specification<Order> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (orderNo != null && !orderNo.isEmpty()) {
                predicates.add(cb.like(root.get("orderNo"), "%" + orderNo + "%"));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (isAbnormal != null) {
                predicates.add(cb.equal(root.get("isAbnormal"), isAbnormal));
            }
            if (buyerId != null) {
                predicates.add(cb.equal(root.get("buyerId"), buyerId));
            }
            if (sellerId != null) {
                predicates.add(cb.equal(root.get("sellerId"), sellerId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return orderRepository.findAll(spec, pageable);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    @Transactional
    public Order save(Order order) {
        if (order.getId() == null) {
            order.setCreateTime(LocalDateTime.now());
            order.setOrderNo(generateOrderNo());
        }
        order.setUpdateTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis();
    }

    @Transactional
    public int batchShip(List<Long> ids) {
        int count = orderRepository.batchUpdateStatus(ids, "SHIPPED");
        for (Long id : ids) {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setShipTime(LocalDateTime.now());
                orderRepository.save(order);
            }
        }
        return count;
    }

    @Transactional
    public int batchRefund(List<Long> ids) {
        return orderRepository.batchUpdateStatus(ids, "REFUNDED");
    }

    @Transactional
    public int markAsAbnormal(Long id, String reason) {
        return orderRepository.markAsAbnormal(id, reason);
    }

    @Transactional
    public Order updateLogistics(Long id, String logisticsCompany, String trackingNumber) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setLogisticsCompany(logisticsCompany);
            order.setTrackingNumber(trackingNumber);
            order.setUpdateTime(LocalDateTime.now());
            return orderRepository.save(order);
        }
        return null;
    }

}