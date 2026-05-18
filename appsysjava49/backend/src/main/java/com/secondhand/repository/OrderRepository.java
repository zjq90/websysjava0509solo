package com.secondhand.repository;

import com.secondhand.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByOrderNo(String orderNo);

    List<Order> findByBuyerIdOrderByCreateTimeDesc(Long buyerId);

    List<Order> findBySellerIdOrderByCreateTimeDesc(Long sellerId);

    List<Order> findByBuyerIdAndStatus(Long buyerId, Integer status);

    List<Order> findBySellerIdAndStatus(Long sellerId, Integer status);
}
