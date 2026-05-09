package com.inventory.repository;

import com.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByWarehouseId(Long warehouseId);
    List<Inventory> findByStoreId(Long storeId);
    List<Inventory> findBySeedBatchId(Long batchId);
    
    @Query("SELECT i FROM Inventory i WHERE i.warehouse.id = :warehouseId AND i.seedBatch.id = :batchId")
    Optional<Inventory> findByWarehouseAndBatch(@Param("warehouseId") Long warehouseId, @Param("batchId") Long batchId);
    
    @Query("SELECT i FROM Inventory i WHERE i.store.id = :storeId AND i.seedBatch.id = :batchId")
    Optional<Inventory> findByStoreAndBatch(@Param("storeId") Long storeId, @Param("batchId") Long batchId);
    
    @Query("SELECT i FROM Inventory i WHERE i.seedBatch.expiryDate BETWEEN :startDate AND :endDate")
    List<Inventory> findExpiringInventory(@Param("startDate") java.time.LocalDate startDate, @Param("endDate") java.time.LocalDate endDate);
}
