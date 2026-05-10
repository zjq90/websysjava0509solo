package com.management.platform.repository;

import com.management.platform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问层
 * 提供订单的增删改查和统计查询功能
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 按订单编号查询订单
     * @param orderNo 订单编号
     * @return 订单对象
     */
    Order findByOrderNo(String orderNo);

    /**
     * 统计指定用户的订单数量
     * @param userId 用户ID
     * @return 订单数量
     */
    long countByUserId(Long userId);

    /**
     * 按小时统计购买分布（用于热力图）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 小时和订单数列表
     */
    @Query("SELECT HOUR(o.purchaseTime), COUNT(o) FROM Order o " +
           "WHERE o.purchaseTime BETWEEN :startTime AND :endTime " +
           "GROUP BY HOUR(o.purchaseTime) ORDER BY HOUR(o.purchaseTime)")
    List<Object[]> countOrdersByHour(@Param("startTime") LocalDateTime startTime, 
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 按商品ID统计销售数量
     * @param productId 商品ID
     * @return 总销售数量
     */
    @Query("SELECT COALESCE(SUM(o.quantity), 0) FROM Order o WHERE o.productId = :productId AND o.status = 'completed'")
    Integer sumQuantityByProductId(@Param("productId") Long productId);

    /**
     * 按商品ID统计销售金额
     * @param productId 商品ID
     * @return 总销售金额
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.productId = :productId AND o.status = 'completed'")
    BigDecimal sumAmountByProductId(@Param("productId") Long productId);

    /**
     * 查询指定时间范围内的订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<Order> findByPurchaseTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计总销售额
     * @return 总销售金额
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'completed'")
    BigDecimal sumTotalAmount();

    /**
     * 统计总订单数
     * @return 总订单数
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'completed'")
    long countCompletedOrders();
}
