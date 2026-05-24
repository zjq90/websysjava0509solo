package com.bike.repository;

import com.bike.entity.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 维修工单数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long>, JpaSpecificationExecutor<RepairOrder> {

    Optional<RepairOrder> findByOrderNo(String orderNo);

    Optional<RepairOrder> findByTaskNo(String taskNo);

    List<RepairOrder> findByBikeId(Long bikeId);

    List<RepairOrder> findByStaffId(Long staffId);

    List<RepairOrder> findByStatus(String status);

    List<RepairOrder> findByStaffIdAndStatus(Long staffId, String status);

    List<RepairOrder> findByStatusIn(List<String> statuses);

    long countByStatus(String status);
}
