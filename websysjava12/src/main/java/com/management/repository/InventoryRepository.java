package com.management.repository;

import com.management.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 库存数据访问接口
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.minStock")
    List<Inventory> findLowStockItems();

    @Query("SELECT SUM(i.quantity * i.avgCostPrice) FROM Inventory i")
    java.math.BigDecimal findTotalInventoryValue();
}
