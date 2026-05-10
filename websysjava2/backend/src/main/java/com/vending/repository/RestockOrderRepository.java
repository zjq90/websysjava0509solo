package com.vending.repository;

import com.vending.entity.RestockOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 补货单数据访问接口
 */
@Repository
public interface RestockOrderRepository extends JpaRepository<RestockOrder, Long> {
    
    Optional<RestockOrder> findByRestockNo(String restockNo);
    
    List<RestockOrder> findByMachineId(Long machineId);
    
    List<RestockOrder> findByStatus(String status);
    
    @Query("SELECT r FROM RestockOrder r WHERE r.notified = false")
    List<RestockOrder> findUnnotifiedOrders();
    
    @Query("SELECT r FROM RestockOrder r WHERE r.machine.id = ?1 AND r.status = 'PENDING'")
    Optional<RestockOrder> findPendingByMachineId(Long machineId);
    
    @Query("SELECT COUNT(r) FROM RestockOrder r WHERE r.status = 'PENDING'")
    Long countPendingOrders();
}
