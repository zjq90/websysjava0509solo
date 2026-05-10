package com.vending.repository;

import com.vending.entity.VendingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 售货机数据访问接口
 */
@Repository
public interface VendingMachineRepository extends JpaRepository<VendingMachine, Long> {
    
    Optional<VendingMachine> findByMachineCode(String machineCode);
    
    List<VendingMachine> findByStatus(String status);
    
    boolean existsByMachineCode(String machineCode);
}
