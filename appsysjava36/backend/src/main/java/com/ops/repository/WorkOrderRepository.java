package com.ops.repository;

import com.ops.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单数据访问接口
 * 
 * @author ops-admin
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>, JpaSpecificationExecutor<WorkOrder> {

    /**
     * 根据工单编号查找
     */
    WorkOrder findByOrderNo(String orderNo);

    /**
     * 根据状态统计工单数量
     */
    long countByStatus(String status);

    /**
     * 根据装维人员ID和状态查找工单
     */
    List<WorkOrder> findByTechnicianIdAndStatus(Long technicianId, String status);

    /**
     * 查找待分配的工单
     */
    List<WorkOrder> findByStatusOrderByCreateTimeDesc(String status);

    /**
     * 统计指定时间段内的工单数量
     */
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.createTime BETWEEN ?1 AND ?2")
    long countByCreateTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 计算平均响应时长
     */
    @Query("SELECT AVG(w.responseDuration) FROM WorkOrder w WHERE w.responseDuration IS NOT NULL AND w.createTime BETWEEN ?1 AND ?2")
    Double avgResponseDuration(LocalDateTime start, LocalDateTime end);

    /**
     * 计算一次修复率
     */
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.isFirstFix = true AND w.status = 'COMPLETED' AND w.completeTime BETWEEN ?1 AND ?2")
    long countFirstFix(LocalDateTime start, LocalDateTime end);

    /**
     * 计算平均满意度
     */
    @Query("SELECT AVG(w.satisfactionScore) FROM WorkOrder w WHERE w.satisfactionScore IS NOT NULL AND w.completeTime BETWEEN ?1 AND ?2")
    Double avgSatisfactionScore(LocalDateTime start, LocalDateTime end);
}
