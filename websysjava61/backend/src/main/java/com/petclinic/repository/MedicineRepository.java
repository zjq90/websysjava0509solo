package com.petclinic.repository;

import com.petclinic.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 药品Repository
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    /**
     * 根据编码查询药品
     */
    Medicine findByCode(String code);

    /**
     * 查询库存预警的药品（库存小于预警数量）
     */
    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity <= m.warningQuantity AND m.status = 1")
    List<Medicine> findWarningMedicines();

    /**
     * 根据分类查询药品
     */
    List<Medicine> findByCategory(String category);

    /**
     * 根据状态查询药品
     */
    List<Medicine> findByStatus(Integer status);
}
