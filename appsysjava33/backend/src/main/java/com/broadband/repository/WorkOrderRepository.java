package com.broadband.repository;

import com.broadband.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工单数据访问层
 * 
 * @author broadband
 * @version 1.0.0
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<WorkOrder> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, Integer status);

    List<WorkOrder> findByUserIdAndTypeOrderByCreateTimeDesc(Long userId, Integer type);

    WorkOrder findByOrderNo(String orderNo);
}
