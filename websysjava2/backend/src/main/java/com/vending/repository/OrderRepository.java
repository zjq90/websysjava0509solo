package com.vending.repository;

import com.vending.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问接口
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNo(String orderNo);
    
    Page<Order> findByMachineId(Long machineId, Pageable pageable);
    
    Page<Order> findByPaymentStatus(String paymentStatus, Pageable pageable);
    
    Page<Order> findByPickupStatus(String pickupStatus, Pageable pageable);
    
    Page<Order> findByPaymentStatusAndPickupStatus(String paymentStatus, String pickupStatus, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.createTime BETWEEN ?1 AND ?2")
    List<Order> findOrdersBetweenDates(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = 'PAID'")
    Long countPaidOrders();
    
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.paymentStatus = 'PAID'")
    java.math.BigDecimal sumPaidAmount();
}
