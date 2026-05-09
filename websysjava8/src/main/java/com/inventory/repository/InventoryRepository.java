package com.inventory.repository;

import com.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 库存Repository接口
 * 核心库存管理接口
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * 根据库存类型和位置ID查询所有库存
     */
    List<Inventory> findByInventoryTypeAndLocationId(Integer inventoryType, Long locationId);

    /**
     * 根据库存类型、位置ID和批次ID查询唯一库存
     */
    Inventory findByInventoryTypeAndLocationIdAndBatchId(Integer inventoryType, Long locationId, Long batchId);

    /**
     * 根据批次ID查询所有库存
     */
    List<Inventory> findByBatchId(Long batchId);

    /**
     * 根据状态查询库存列表
     */
    List<Inventory> findByStatus(Integer status);

    /**
     * 查询某个位置某个品种的所有库存（按先进先出排序）
     * 先进先出：按入库日期升序排列
     */
    @Query("SELECT i FROM Inventory i WHERE i.inventoryType = :inventoryType AND i.locationId = :locationId AND i.batchId IN (SELECT sb.id FROM SeedBatch sb WHERE sb.varietyId = :varietyId) AND i.availableQuantity > 0 AND i.status <> 3 ORDER BY i.inboundDate ASC")
    List<Inventory> findInventoryFIFO(@Param("inventoryType") Integer inventoryType, @Param("locationId") Long locationId, @Param("varietyId") Long varietyId);

    /**
     * 查询所有位置的近效期库存
     * @param warningDate 警告日期（当前日期 + 警告天数）
     * @param currentDate 当前日期
     */
    @Query("SELECT i FROM Inventory i WHERE i.batchId IN (SELECT sb.id FROM SeedBatch sb WHERE sb.expiryDate <= :warningDate AND sb.expiryDate > :currentDate) AND i.availableQuantity > 0")
    List<Inventory> findAllNearExpiryInventory(@Param("warningDate") LocalDate warningDate, @Param("currentDate") LocalDate currentDate);

    /**
     * 查询近效期库存
     * @param inventoryType 库存类型（1-仓库，2-门店）
     * @param locationId 位置ID
     * @param warningDate 警告日期（当前日期 + 警告天数）
     * @param currentDate 当前日期
     */
    @Query("SELECT i FROM Inventory i WHERE i.inventoryType = :inventoryType AND i.locationId = :locationId AND i.batchId IN (SELECT sb.id FROM SeedBatch sb WHERE sb.expiryDate <= :warningDate AND sb.expiryDate > :currentDate) AND i.availableQuantity > 0")
    List<Inventory> findNearExpiryInventory(@Param("inventoryType") Integer inventoryType, @Param("locationId") Long locationId, @Param("warningDate") LocalDate warningDate, @Param("currentDate") LocalDate currentDate);

    /**
     * 查询已过期库存
     */
    @Query("SELECT i FROM Inventory i WHERE i.batchId IN (SELECT sb.id FROM SeedBatch sb WHERE sb.expiryDate <= :currentDate) AND i.availableQuantity > 0")
    List<Inventory> findExpiredInventory(@Param("currentDate") LocalDate currentDate);

    /**
     * 查询低库存预警（可用数量低于最低阈值）
     */
    @Query("SELECT i FROM Inventory i WHERE i.availableQuantity <= i.minStockWarning AND i.minStockWarning IS NOT NULL AND i.status = 0")
    List<Inventory> findLowStockInventory();

    /**
     * 查询高库存预警（可用数量高于最高阈值）
     */
    @Query("SELECT i FROM Inventory i WHERE i.availableQuantity >= i.maxStockWarning AND i.maxStockWarning IS NOT NULL AND i.status = 0")
    List<Inventory> findHighStockInventory();

    /**
     * 统计某个位置的总库存数量
     */
    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM Inventory i WHERE i.inventoryType = :inventoryType AND i.locationId = :locationId")
    Integer sumQuantityByLocation(@Param("inventoryType") Integer inventoryType, @Param("locationId") Long locationId);

    /**
     * 统计某个位置的可用库存数量
     */
    @Query("SELECT COALESCE(SUM(i.availableQuantity), 0) FROM Inventory i WHERE i.inventoryType = :inventoryType AND i.locationId = :locationId")
    Integer sumAvailableQuantityByLocation(@Param("inventoryType") Integer inventoryType, @Param("locationId") Long locationId);
}
