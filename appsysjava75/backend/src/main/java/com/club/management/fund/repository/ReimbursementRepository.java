package com.club.management.fund.repository;

import com.club.management.fund.entity.Reimbursement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 报销申请Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, Long>, JpaSpecificationExecutor<Reimbursement> {

    Page<Reimbursement> findByClubIdOrderByCreateTimeDesc(Long clubId, Pageable pageable);

    Page<Reimbursement> findByApplicantIdOrderByCreateTimeDesc(Long applicantId, Pageable pageable);

    Page<Reimbursement> findByClubIdAndStatusOrderByCreateTimeDesc(Long clubId, Integer status, Pageable pageable);
}
