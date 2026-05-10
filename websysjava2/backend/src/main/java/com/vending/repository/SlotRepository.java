package com.vending.repository;

import com.vending.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 货道数据访问接口
 */
@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    
    List<Slot> findByMachineId(Long machineId);
    
    Optional<Slot> findByMachineIdAndSlotNumber(Long machineId, Integer slotNumber);
    
    List<Slot> findByProductId(Long productId);
    
    @Query("SELECT s FROM Slot s WHERE s.machine.id = ?1 AND s.currentStock <= s.product.stockThreshold")
    List<Slot> findLowStockSlotsByMachineId(Long machineId);
    
    @Query("SELECT s FROM Slot s WHERE s.currentStock <= s.product.stockThreshold")
    List<Slot> findAllLowStockSlots();
    
    @Query("SELECT COALESCE(SUM(s.currentStock), 0) FROM Slot s WHERE s.product.id = ?1")
    Integer getTotalStockByProductId(Long productId);
    
    @Query("SELECT s FROM Slot s WHERE s.machine.id = ?1 AND s.product.id = ?2")
    List<Slot> findByMachineIdAndProductId(Long machineId, Long productId);
}
