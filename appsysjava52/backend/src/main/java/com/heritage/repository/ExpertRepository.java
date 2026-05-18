package com.heritage.repository;

import com.heritage.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专家Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {

    /**
     * 根据状态查询专家
     */
    List<Expert> findByStatus(Integer status);

    /**
     * 根据擅长类别查询专家
     */
    List<Expert> findBySpecialtiesContainingAndStatus(String category, Integer status);
}