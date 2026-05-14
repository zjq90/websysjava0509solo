package com.photostudio.repository;

import com.photostudio.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * 根据订单编号查询
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 根据客户ID查询订单
     */
    List<Order> findByCustomerIdOrderByCreateTimeDesc(Long customerId);

    /**
     * 根据订单状态查询
     */
    List<Order> findByStatusOrderByCreateTimeDesc(String status);

    /**
     * 根据当前阶段查询
     */
    List<Order> findByCurrentStageOrderByCreateTimeDesc(Integer currentStage);

    /**
     * 根据订单来源查询
     */
    List<Order> findByChannelSourceOrderByCreateTimeDesc(String channelSource);

    /**
     * 根据手机号查询客户订单
     */
    List<Order> findByCustomerPhoneOrderByCreateTimeDesc(String customerPhone);

    /**
     * 根据第三方平台订单号查询
     */
    Optional<Order> findByChannelOrderNo(String channelOrderNo);

    /**
     * 查询今日新增订单数量
     */
    @Query(value = "SELECT COUNT(*) FROM orders WHERE DATE(create_time) = CURDATE()", nativeQuery = true)
    Long countTodayOrders();

    /**
     * 查询本月订单总金额
     */
    @Query(value = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE MONTH(create_time) = MONTH(CURDATE()) AND YEAR(create_time) = YEAR(CURDATE())", nativeQuery = true)
    java.math.BigDecimal sumMonthTotalAmount();
}
