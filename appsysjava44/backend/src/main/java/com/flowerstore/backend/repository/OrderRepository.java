package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * 根据用户ID查询订单列表
     */
    List<Order> findByUserIdOrderByCreateTimeDesc(Long userId);

    /**
     * 根据用户ID和订单状态查询
     */
    List<Order> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, Integer status);

    /**
     * 根据订单号查询
     */
    Order findByOrderNo(String orderNo);
}
