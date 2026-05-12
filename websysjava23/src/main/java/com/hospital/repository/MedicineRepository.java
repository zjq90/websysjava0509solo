package com.hospital.repository;

import com.hospital.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 药品Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Medicine findByMedicineCode(String medicineCode);

    List<Medicine> findByGenericNameContaining(String genericName);

    List<Medicine> findByCategory(String category);

    List<Medicine> findByStatus(String status);

    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity < 10")
    List<Medicine> findLowStockMedicines();

    @Query("SELECT m FROM Medicine m WHERE m.genericName LIKE %?1% OR m.tradeName LIKE %?1% OR m.medicineCode LIKE %?1%")
    List<Medicine> search(String keyword);
}
