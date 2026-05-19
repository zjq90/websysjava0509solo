package com.pethospital.repository;

import com.pethospital.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 疾病数据访问层
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    
    /**
     * 根据宠物类型和状态查询疾病
     * 
     * @param petType 宠物类型
     * @param status 状态
     * @return 疾病列表
     */
    List<Disease> findByPetTypeAndStatus(String petType, Integer status);
    
    /**
     * 根据关键词搜索疾病
     * 
     * @param keyword 关键词
     * @param status 状态
     * @return 疾病列表
     */
    @Query("SELECT d FROM Disease d WHERE d.status = ?2 AND (d.name LIKE CONCAT('%', ?1, '%') OR d.symptoms LIKE CONCAT('%', ?1, '%') OR d.keywords LIKE CONCAT('%', ?1, '%'))")
    List<Disease> searchByKeyword(String keyword, Integer status);
    
    /**
     * 查询所有启用的疾病
     * 
     * @param status 状态
     * @return 疾病列表
     */
    List<Disease> findByStatus(Integer status);
}
