package com.sales.repository;

import com.sales.entity.OrderStatus;
import com.sales.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问层接口
 * 继承JpaRepository，提供基础的增删改查功能
 */
@Repository
public interface OrderRepository extends JpaRepository<SalesOrder, Long> {

    /**
     * 根据订单编号查找订单
     */
    Optional<SalesOrder> findByOrderNo(String orderNo);

    /**
     * 按订单状态查找
     */
    List<SalesOrder> findByStatus(OrderStatus status);

    /**
     * 按客户ID查找订单
     */
    List<SalesOrder> findByCustomerIdOrderByCreateTimeDesc(Long customerId);

    /**
     * 按创建时间范围查找
     */
    List<SalesOrder> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 查找所有订单，按创建时间倒序
     */
    List<SalesOrder> findAllByOrderByCreateTimeDesc();

    /**
     * 按状态和客户ID查找
     */
    List<SalesOrder> findByStatusAndCustomerId(OrderStatus status, Long customerId);

    /**
     * 分页查询所有订单，按创建时间倒序
     */
    Page<SalesOrder> findAllByOrderByCreateTimeDesc(Pageable pageable);

    /**
     * 按状态分页查询
     */
    Page<SalesOrder> findByStatusOrderByCreateTimeDesc(OrderStatus status, Pageable pageable);

    /**
     * 按客户ID分页查询
     */
    Page<SalesOrder> findByCustomerIdOrderByCreateTimeDesc(Long customerId, Pageable pageable);
}
