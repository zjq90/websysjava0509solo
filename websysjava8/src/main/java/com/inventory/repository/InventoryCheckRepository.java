package com.inventory.repository;

import com.inventory.entity.InventoryCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryCheckRepository extends JpaRepository<InventoryCheck, Long> {
    Optional<InventoryCheck> findByCheckCode(String checkCode);
    List<InventoryCheck> findByInventoryId(Long inventoryId);
}
