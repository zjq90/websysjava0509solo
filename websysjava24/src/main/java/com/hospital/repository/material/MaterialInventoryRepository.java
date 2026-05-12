package com.hospital.repository.material;

import com.hospital.entity.material.MaterialInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Long>, JpaSpecificationExecutor<MaterialInventory> {

    List<MaterialInventory> findByMaterialId(Long materialId);

    Optional<MaterialInventory> findByMaterialIdAndBatchNumber(Long materialId, String batchNumber);

    Optional<MaterialInventory> findByUniqueIdentifier(String uniqueIdentifier);

    List<MaterialInventory> findByInventoryStatus(Integer inventoryStatus);

    @Query("SELECT m FROM MaterialInventory m WHERE m.expiryDate <= :warningDate")
    List<MaterialInventory> findNearExpiryMaterials(LocalDate warningDate);

    List<MaterialInventory> findByWarehouseLocation(String warehouseLocation);
}
