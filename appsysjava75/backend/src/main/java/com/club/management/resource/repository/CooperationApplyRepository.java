package com.club.management.resource.repository;

import com.club.management.resource.entity.CooperationApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 校企对接申请Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface CooperationApplyRepository extends JpaRepository<CooperationApply, Long>, JpaSpecificationExecutor<CooperationApply> {

    Page<CooperationApply> findByCooperationIdOrderByCreateTimeDesc(Long cooperationId, Pageable pageable);

    Page<CooperationApply> findByApplicantIdOrderByCreateTimeDesc(Long applicantId, Pageable pageable);

    Page<CooperationApply> findByClubIdOrderByCreateTimeDesc(Long clubId, Pageable pageable);
}
