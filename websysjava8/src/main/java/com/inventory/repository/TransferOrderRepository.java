package com.inventory.repository;

import com.inventory.entity.TransferOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 调拨单Repository接口
 */
@Repository
public interface TransferOrderRepository extends JpaRepository<TransferOrder, Long> {

    /**
     * 根据调拨单号查询
     */
    TransferOrder findByOrderNo(String orderNo);

    /**
     * 根据状态查询调拨单列表
     */
    List<TransferOrder> findByStatus(Integer status);

    /**
     * 根据调出位置查询
     */
    List<TransferOrder> findByFromLocationIdAndFromLocationType(Long fromLocationId, Integer fromLocationType);

    /**
     * 根据调入位置查询
     */
    List<TransferOrder> findByToLocationIdAndToLocationType(Long toLocationId, Integer toLocationType);

    /**
     * 根据调拨类型查询
     */
    List<TransferOrder> findByTransferType(Integer transferType);

    /**
     * 查询某个时间范围内的调拨单
     */
    @Query("SELECT t FROM TransferOrder t WHERE t.createdAt BETWEEN :startTime AND :endTime ORDER BY t.createdAt DESC")
    List<TransferOrder> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 检查单号是否存在
     */
    boolean existsByOrderNo(String orderNo);
}
