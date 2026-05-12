package com.hospital.repository.medicine;

import com.hospital.entity.medicine.MedicineInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineInventoryRepository extends JpaRepository<MedicineInventory, Long>, JpaSpecificationExecutor<MedicineInventory> {

    List<MedicineInventory> findByMedicineId(Long medicineId);

    Optional<MedicineInventory> findByMedicineIdAndBatchNumber(Long medicineId, String batchNumber);

    List<MedicineInventory> findByInventoryStatus(Integer inventoryStatus);

    @Query("SELECT m FROM MedicineInventory m WHERE m.expiryDate <= :warningDate")
    List<MedicineInventory> findNearExpiryMedicines(LocalDate warningDate);

    @Query("SELECT m FROM MedicineInventory m WHERE m.lastOutTime < :date OR m.lastOutTime IS NULL")
    List<MedicineInventory> findSlowMovingMedicines(LocalDate date);

    List<MedicineInventory> findByWarehouseLocation(String warehouseLocation);
}
