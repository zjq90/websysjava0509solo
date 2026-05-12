package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 药品Repository
 * 药品数据访问接口
 */
@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    /**
     * 根据药品编码查询
     */
    Drug findByDrugCode(String drugCode);

    /**
     * 根据药品名称模糊查询
     */
    List<Drug> findByDrugNameContaining(String drugName);

    /**
     * 根据药品类型查询
     */
    List<Drug> findByDrugType(String drugType);

    /**
     * 根据状态查询
     */
    List<Drug> findByStatus(Integer status);

    /**
     * 查询库存不足的药品
     */
    @Query("SELECT d FROM Drug d WHERE d.stockQuantity <= d.minStock")
    List<Drug> findLowStockDrugs();
}
