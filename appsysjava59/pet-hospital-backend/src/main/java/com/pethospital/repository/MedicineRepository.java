package com.pethospital.repository;

import com.pethospital.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByCategory(String category);
    
    @Query("SELECT m FROM Medicine m WHERE m.name LIKE %:keyword%")
    List<Medicine> searchByName(@Param("keyword") String keyword);
}
