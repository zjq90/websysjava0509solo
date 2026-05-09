package com.breeding.repository;

import com.breeding.entity.CrossCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 杂交组合数据访问接口
 */
@Repository
public interface CrossCombinationRepository extends JpaRepository<CrossCombination, Long> {

    CrossCombination findByCombinationCode(String combinationCode);

    List<CrossCombination> findByStatus(String status);

    List<CrossCombination> findByProjectId(Long projectId);

    List<CrossCombination> findByFemaleParentId(Long parentId);

    List<CrossCombination> findByMaleParentId(Long parentId);

    @Query("SELECT c FROM CrossCombination c WHERE c.combinationName LIKE %?1% OR c.combinationCode LIKE %?1%")
    List<CrossCombination> searchByKeyword(String keyword);
}
