package com.flower.repository;

import com.flower.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单明细数据访问层
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * 根据订单ID查询明细
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 统计热销商品
     */
    @Query("SELECT oi.productId, oi.productName, SUM(oi.quantity) as totalQty " +
           "FROM OrderItem oi JOIN oi.order o " +
           "WHERE o.createTime BETWEEN :start AND :end AND o.status != 6 " +
           "GROUP BY oi.productId, oi.productName " +
           "ORDER BY totalQty DESC")
    List<Object[]> findHotProducts(LocalDateTime start, LocalDateTime end);
}
