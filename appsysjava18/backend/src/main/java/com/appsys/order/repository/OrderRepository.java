package com.appsys.order.repository;

import com.appsys.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 订单数据访问接口
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * 根据ID查询未删除的订单
     */
    Optional<Order> findByIdAndDeletedFalse(Long id);

    /**
     * 根据订单编号查询
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 检查订单编号是否存在
     */
    boolean existsByOrderNo(String orderNo);

    /**
     * 分页查询未删除的订单
     */
    Page<Order> findByDeletedFalseOrderByCreatedTimeDesc(Pageable pageable);

    /**
     * 根据订单编号或客户名称模糊查询（分页）
     */
    @Query("SELECT o FROM Order o WHERE o.deleted = false AND " +
           "(o.orderNo LIKE %:keyword% OR o.customerName LIKE %:keyword%)")
    Page<Order> searchByKeyword(String keyword, Pageable pageable);
}
