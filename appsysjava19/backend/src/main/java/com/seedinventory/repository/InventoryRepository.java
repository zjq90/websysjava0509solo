package com.seedinventory.repository;

import com.seedinventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 库存数据访问层
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    /**
     * 根据批次号查询
     */
    Optional<Inventory> findByBatchNo(String batchNo);
    
    /**
     * 判断批次号是否存在
     */
    boolean existsByBatchNo(String batchNo);
    
    /**
     * 根据仓库ID查询库存列表
     */
    List<Inventory> findByWarehouseId(Long warehouseId);
    
    /**
     * 根据仓库ID和种子ID查询
     */
    List<Inventory> findByWarehouseIdAndSeedId(Long warehouseId, Long seedId);
    
    /**
     * 根据状态查询
     */
    List<Inventory> findByStatus(String status);
    
    /**
     * 查询近效期库存（保质期在指定天数内）
     */
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate BETWEEN :today AND :targetDate AND i.status <> 'EXPIRED'")
    List<Inventory> findNearExpiry(@Param("today") LocalDate today, @Param("targetDate") LocalDate targetDate);
    
    /**
     * 查询已过期库存
     */
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate < :today AND i.status <> 'EXPIRED'")
    List<Inventory> findExpired(@Param("today") LocalDate today);
    
    /**
     * 查询某仓库的库存统计
     */
    @Query("SELECT COUNT(i), SUM(i.quantity) FROM Inventory i WHERE i.warehouseId = :warehouseId")
    List<Object[]> countInventoryByWarehouse(@Param("warehouseId") Long warehouseId);
}
