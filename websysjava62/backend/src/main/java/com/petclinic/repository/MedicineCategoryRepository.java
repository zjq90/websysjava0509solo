package com.petclinic.repository;

import com.petclinic.entity.MedicineCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 药品分类Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface MedicineCategoryRepository extends JpaRepository<MedicineCategory, Long> {

    /**
     * 根据状态查询分类列表
     */
    List<MedicineCategory> findByStatus(String status);

    /**
     * 根据父分类ID查询子分类
     */
    List<MedicineCategory> findByParentId(Long parentId);

    /**
     * 根据分类名称查询
     */
    List<MedicineCategory> findByCategoryNameContaining(String categoryName);
}
