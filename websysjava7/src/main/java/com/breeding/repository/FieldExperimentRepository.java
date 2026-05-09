package com.breeding.repository;

import com.breeding.entity.FieldExperiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 田间试验数据访问接口
 */
@Repository
public interface FieldExperimentRepository extends JpaRepository<FieldExperiment, Long> {

    FieldExperiment findByExperimentCode(String experimentCode);

    List<FieldExperiment> findByStatus(String status);

    List<FieldExperiment> findByYear(Integer year);

    List<FieldExperiment> findByLocation(String location);

    List<FieldExperiment> findByProjectId(Long projectId);

    List<FieldExperiment> findByCrossCombinationId(Long combinationId);

    @Query("SELECT f FROM FieldExperiment f WHERE f.experimentName LIKE %?1% OR f.experimentCode LIKE %?1%")
    List<FieldExperiment> searchByKeyword(String keyword);

    @Query("SELECT AVG(f.yieldPerMu) FROM FieldExperiment f WHERE f.location = ?1 AND f.status = '已完成'")
    Double getAverageYieldByLocation(String location);
}
