package com.sales.repository;

import com.sales.entity.Logistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 物流数据访问层接口
 * 继承JpaRepository，提供基础的增删改查功能
 */
@Repository
public interface LogisticsRepository extends JpaRepository<Logistics, Long> {

    /**
     * 按订单ID查找物流信息
     */
    List<Logistics> findByOrderIdOrderByCreateTimeDesc(Long orderId);

    /**
     * 按物流单号查找
     */
    Optional<Logistics> findByTrackingNo(String trackingNo);
}
