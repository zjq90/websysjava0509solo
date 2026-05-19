package com.petclinic.repository;

import com.petclinic.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 药品Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    /**
     * 根据状态查询药品列表
     */
    List<Medicine> findByStatus(String status);

    /**
     * 根据分类ID查询药品
     */
    List<Medicine> findByCategoryId(Long categoryId);

    /**
     * 根据药品名称模糊查询
     */
    List<Medicine> findByMedicineNameContaining(String medicineName);

    /**
     * 根据宠物类型查询
     */
    List<Medicine> findByPetTypeContaining(String petType);
}
