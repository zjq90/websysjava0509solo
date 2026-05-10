package com.appsys.repository;

import com.appsys.entity.LogisticsStatus;
import com.appsys.entity.LogisticsTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 物流跟踪数据访问接口
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Repository
public interface LogisticsTrackingRepository extends JpaRepository<LogisticsTracking, Long> {

    /**
     * 根据订单ID查询物流信息
     * @param orderId 订单ID
     * @return 物流信息
     */
    Optional<LogisticsTracking> findByOrderId(Long orderId);

    /**
     * 根据快递单号查询
     * @param trackingNo 快递单号
     * @return 物流信息
     */
    Optional<LogisticsTracking> findByTrackingNo(String trackingNo);

    /**
     * 根据物流状态查询
     * @param status 物流状态
     * @return 物流信息列表
     */
    List<LogisticsTracking> findByStatus(LogisticsStatus status);
}
