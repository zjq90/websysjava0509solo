package com.appsys.repository;

import com.appsys.entity.OrderStatus;
import com.appsys.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 销售订单数据访问接口
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    /**
     * 根据订单编号查询
     * @param orderNo 订单编号
     * @return 订单
     */
    Optional<SalesOrder> findByOrderNo(String orderNo);

    /**
     * 根据合同编号查询
     * @param contractNo 合同编号
     * @return 订单
     */
    Optional<SalesOrder> findByContractNo(String contractNo);

    /**
     * 根据客户ID查询订单列表
     * @param customerId 客户ID
     * @return 订单列表
     */
    List<SalesOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    /**
     * 根据订单状态查询
     * @param status 订单状态
     * @return 订单列表
     */
    List<SalesOrder> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    /**
     * 查询所有订单按创建时间倒序
     * @return 订单列表
     */
    @Query("SELECT o FROM SalesOrder o ORDER BY o.createdAt DESC")
    List<SalesOrder> findAllOrderByCreatedAtDesc();

    /**
     * 查询客户的历史订单
     * @param customerId 客户ID
     * @return 订单列表
     */
    @Query("SELECT o FROM SalesOrder o WHERE o.customerId = :customerId ORDER BY o.createdAt DESC")
    List<SalesOrder> findCustomerHistoryOrders(@Param("customerId") Long customerId);

    /**
     * 统计某个状态的订单数量
     * @param status 订单状态
     * @return 数量
     */
    long countByStatus(OrderStatus status);

    /**
     * 根据业务员ID查询订单
     * @param salespersonId 业务员ID
     * @return 订单列表
     */
    List<SalesOrder> findBySalespersonIdOrderByCreatedAtDesc(Long salespersonId);
}
