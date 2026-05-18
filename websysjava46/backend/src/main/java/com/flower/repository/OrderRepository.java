package com.flower.repository;

import com.flower.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问层
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * 根据订单编号查询
     */
    Order findByOrderNo(String orderNo);

    /**
     * 根据状态查询订单
     */
    List<Order> findByStatus(Integer status);

    /**
     * 根据客户ID查询订单
     */
    List<Order> findByCustomerId(Long customerId);

    /**
     * 统计指定时间范围内的订单金额
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.createTime BETWEEN :start AND :end AND o.status != 6")
    java.math.BigDecimal sumTotalAmountByCreateTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 统计指定时间范围内的订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createTime BETWEEN :start AND :end")
    Long countByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
}
