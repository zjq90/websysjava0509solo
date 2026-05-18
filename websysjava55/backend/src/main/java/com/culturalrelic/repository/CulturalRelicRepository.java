package com.culturalrelic.repository;

import com.culturalrelic.entity.CulturalRelic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文物数据访问层
 */
@Repository
public interface CulturalRelicRepository extends JpaRepository<CulturalRelic, Long>, JpaSpecificationExecutor<CulturalRelic> {

    /**
     * 根据文物编号查询
     */
    CulturalRelic findByRelicNo(String relicNo);

    /**
     * 根据类别查询
     */
    List<CulturalRelic> findByCategory(String category);

    /**
     * 根据朝代查询
     */
    List<CulturalRelic> findByDynasty(String dynasty);

    /**
     * 根据状态查询
     */
    List<CulturalRelic> findByStatus(Integer status);

    /**
     * 逻辑删除
     */
    @Modifying
    @Query("UPDATE CulturalRelic r SET r.deleted = 1 WHERE r.id = :id")
    int logicDelete(@Param("id") Long id);

    /**
     * 根据名称模糊查询
     */
    @Query("SELECT r FROM CulturalRelic r WHERE r.name LIKE %:keyword% AND r.deleted = 0")
    List<CulturalRelic> searchByName(@Param("keyword") String keyword);
}
