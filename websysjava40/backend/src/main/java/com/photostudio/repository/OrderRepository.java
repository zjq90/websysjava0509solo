package com.photostudio.repository;

import com.photostudio.entity.Order;
import com.photostudio.entity.Order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问接口
 * 提供订单相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单编号查询订单
     * @param orderNo 订单编号
     * @return 订单信息
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 根据状态查询订单
     * @param status 状态
     * @return 订单列表
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * 根据客户姓名模糊查询订单
     * @param customerName 客户姓名关键词
     * @return 订单列表
     */
    List<Order> findByCustomerNameContaining(String customerName);

    /**
     * 查询指定时间范围内创建的订单
     * @param start 开始时间
     * @param end 结束时间
     * @return 订单列表
     */
    List<Order> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 查询待发货的订单
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> findByStatusOrderByCreateTimeDesc(OrderStatus status);
}
