package com.hospital.repository.medicine;

import com.hospital.entity.medicine.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long>, JpaSpecificationExecutor<Medicine> {

    Optional<Medicine> findByMedicineCode(String medicineCode);

    List<Medicine> findByStatus(Integer status);

    List<Medicine> findByCategory(String category);

    boolean existsByMedicineCode(String medicineCode);
}
