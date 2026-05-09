package com.agricultural.repository;

import com.agricultural.entity.Order;
import com.agricultural.entity.enums.OrderStatus;
import com.agricultural.entity.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问层
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单编号查找
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 根据订单类型查询
     */
    List<Order> findByOrderTypeOrderByOrderDateDesc(OrderType orderType);

    /**
     * 根据订单状态查询
     */
    List<Order> findByOrderStatusOrderByOrderDateDesc(OrderStatus orderStatus);

    /**
     * 根据客户ID查询
     */
    List<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId);

    /**
     * 根据订单类型和状态查询
     */
    List<Order> findByOrderTypeAndOrderStatusOrderByOrderDateDesc(OrderType orderType, OrderStatus status);

    /**
     * 根据日期范围查询
     */
    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(LocalDate startDate, LocalDate endDate);

    /**
     * 检查订单编号是否存在
     */
    boolean existsByOrderNo(String orderNo);

    /**
     * 统计待处理订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = 'PENDING'")
    long countPendingOrders();

    /**
     * 计算指定类型订单的总金额
     */
    @Query("SELECT COALESCE(SUM(o.netAmount), 0) FROM Order o WHERE o.orderType = :orderType AND o.orderStatus <> 'CANCELLED'")
    BigDecimal sumNetAmountByOrderType(@Param("orderType") OrderType orderType);

    /**
     * 按日期范围统计销售金额
     */
    @Query("SELECT COALESCE(SUM(o.netAmount), 0) FROM Order o WHERE o.orderType = 'SALES' AND o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal sumSalesAmountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 查询最大的订单编号（用于生成新编号）
     */
    @Query("SELECT MAX(o.orderNo) FROM Order o WHERE o.orderNo LIKE :prefix%")
    String findMaxOrderNoByPrefix(@Param("prefix") String prefix);
}
