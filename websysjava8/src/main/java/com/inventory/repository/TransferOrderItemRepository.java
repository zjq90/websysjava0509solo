package com.inventory.repository;

import com.inventory.entity.TransferOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调拨单明细Repository接口
 */
@Repository
public interface TransferOrderItemRepository extends JpaRepository<TransferOrderItem, Long> {

    /**
     * 根据调拨单ID查询明细列表
     */
    List<TransferOrderItem> findByOrderId(Long orderId);

    /**
     * 根据批次ID查询所有调拨明细
     */
    List<TransferOrderItem> findByBatchId(Long batchId);

    /**
     * 统计某个调拨单的总调拨数量
     */
    Integer countByOrderId(Long orderId);
}
