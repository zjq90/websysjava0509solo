package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单明细数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * 根据订单ID查询明细
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 根据订单ID列表查询明细
     */
    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}
