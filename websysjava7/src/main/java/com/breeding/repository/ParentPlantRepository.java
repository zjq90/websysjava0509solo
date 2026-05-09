package com.breeding.repository;

import com.breeding.entity.ParentPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 亲本植物数据访问接口
 */
@Repository
public interface ParentPlantRepository extends JpaRepository<ParentPlant, Long> {

    ParentPlant findByParentCode(String parentCode);

    List<ParentPlant> findByParentType(String parentType);

    List<ParentPlant> findByCropType(String cropType);

    List<ParentPlant> findByProjectId(Long projectId);

    @Query("SELECT p FROM ParentPlant p WHERE p.varietyName LIKE %?1% OR p.parentCode LIKE %?1%")
    List<ParentPlant> searchByKeyword(String keyword);
}
