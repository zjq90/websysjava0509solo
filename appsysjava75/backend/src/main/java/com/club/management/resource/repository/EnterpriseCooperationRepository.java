package com.club.management.resource.repository;

import com.club.management.resource.entity.EnterpriseCooperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 校企对接Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface EnterpriseCooperationRepository extends JpaRepository<EnterpriseCooperation, Long>, JpaSpecificationExecutor<EnterpriseCooperation> {

    Page<EnterpriseCooperation> findByTypeAndStatusOrderByIsTopDescCreateTimeDesc(Integer type, Integer status, Pageable pageable);

    Page<EnterpriseCooperation> findByStatusOrderByIsTopDescCreateTimeDesc(Integer status, Pageable pageable);

    @Modifying
    @Query("UPDATE EnterpriseCooperation e SET e.viewCount = e.viewCount + 1 WHERE e.id = :id")
    int increaseViewCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE EnterpriseCooperation e SET e.applyCount = e.applyCount + 1 WHERE e.id = :id")
    int increaseApplyCount(@Param("id") Long id);
}
