package com.pethospital.repository;

import com.pethospital.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 药品数据访问层
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    /**
     * 根据分类和状态查询药品
     * 
     * @param category 分类
     * @param status 状态
     * @return 药品列表
     */
    List<Medicine> findByCategoryAndStatus(String category, Integer status);
    
    /**
     * 根据关键词搜索药品
     * 
     * @param keyword 关键词
     * @param status 状态
     * @return 药品列表
     */
    @Query("SELECT m FROM Medicine m WHERE m.status = ?2 AND (m.name LIKE CONCAT('%', ?1, '%') OR m.genericName LIKE CONCAT('%', ?1, '%') OR m.indication LIKE CONCAT('%', ?1, '%') OR m.keywords LIKE CONCAT('%', ?1, '%'))")
    List<Medicine> searchByKeyword(String keyword, Integer status);
    
    /**
     * 查询所有启用的药品
     * 
     * @param status 状态
     * @return 药品列表
     */
    List<Medicine> findByStatus(Integer status);
}
