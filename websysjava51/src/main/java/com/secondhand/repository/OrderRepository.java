package com.secondhand.repository;

import com.secondhand.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Order findByOrderNo(String orderNo);

    List<Order> findByBuyerId(Long buyerId);

    List<Order> findBySellerId(Long sellerId);

    List<Order> findByStatus(String status);

    List<Order> findByIsAbnormalTrue();

    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id IN :ids")
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    @Modifying
    @Query("UPDATE Order o SET o.isAbnormal = true, o.abnormalReason = :reason WHERE o.id = :id")
    int markAsAbnormal(@Param("id") Long id, @Param("reason") String reason);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createTime BETWEEN :start AND :end")
    Long countByCreateTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.createTime BETWEEN :start AND :end AND o.status NOT IN ('CANCELLED', 'REFUNDED')")
    java.math.BigDecimal sumTotalAmountByCreateTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}