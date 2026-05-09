package com.sales.repository;

import com.sales.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单项数据访问层接口
 * 继承JpaRepository，提供基础的增删改查功能
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * 按订单ID查找所有订单项
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 按产品ID查找订单项
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * 删除某个订单的所有订单项
     */
    void deleteByOrderId(Long orderId);
}
