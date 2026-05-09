package com.inventory.repository;

import com.inventory.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByTransferCode(String transferCode);
    List<Transfer> findByStatus(String status);
    List<Transfer> findByFromWarehouseId(Long warehouseId);
    List<Transfer> findByToWarehouseId(Long warehouseId);
    List<Transfer> findByFromStoreId(Long storeId);
    List<Transfer> findByToStoreId(Long storeId);
}
