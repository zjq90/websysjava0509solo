package com.inventory.repository;

import com.inventory.entity.StockTakeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 盘点单明细Repository接口
 */
@Repository
public interface StockTakeItemRepository extends JpaRepository<StockTakeItem, Long> {

    /**
     * 根据盘点单ID查询明细列表
     */
    List<StockTakeItem> findByTakeId(Long takeId);

    /**
     * 根据库存ID查询所有盘点明细
     */
    List<StockTakeItem> findByInventoryId(Long inventoryId);

    /**
     * 根据批次ID查询所有盘点明细
     */
    List<StockTakeItem> findByBatchId(Long batchId);

    /**
     * 统计某个盘点单的明细数量
     */
    Integer countByTakeId(Long takeId);
}
