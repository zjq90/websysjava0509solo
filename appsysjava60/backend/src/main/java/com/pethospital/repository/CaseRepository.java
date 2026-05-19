package com.pethospital.repository;

import com.pethospital.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 案例数据访问层
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    
    /**
     * 根据医生ID查询案例
     * 
     * @param doctorId 医生ID
     * @return 案例列表
     */
    List<Case> findByDoctorId(Long doctorId);
    
    /**
     * 根据宠物类型和状态查询案例
     * 
     * @param petType 宠物类型
     * @param status 状态
     * @return 案例列表
     */
    List<Case> findByPetTypeAndStatus(String petType, Integer status);
    
    /**
     * 根据关键词搜索案例
     * 
     * @param keyword 关键词
     * @param status 状态
     * @return 案例列表
     */
    @Query("SELECT c FROM Case c WHERE c.status = ?2 AND (c.title LIKE CONCAT('%', ?1, '%') OR c.chiefComplaint LIKE CONCAT('%', ?1, '%') OR c.diagnosis LIKE CONCAT('%', ?1, '%') OR c.keywords LIKE CONCAT('%', ?1, '%'))")
    List<Case> searchByKeyword(String keyword, Integer status);
    
    /**
     * 查询所有已发布的案例
     * 
     * @param status 状态
     * @return 案例列表
     */
    List<Case> findByStatus(Integer status);
}
