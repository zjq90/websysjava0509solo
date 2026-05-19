package com.pethospital.repository;

import com.pethospital.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 药品数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    List<Medicine> findByCategoryAndStatus(String category, String status);

    List<Medicine> findByNameContainingAndStatus(String name, String status);

    List<Medicine> findByStatus(String status);
}
