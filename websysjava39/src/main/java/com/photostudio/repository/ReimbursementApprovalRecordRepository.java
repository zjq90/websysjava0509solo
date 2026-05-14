package com.photostudio.repository;

import com.photostudio.entity.ReimbursementApprovalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报销审批记录Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface ReimbursementApprovalRecordRepository extends JpaRepository<ReimbursementApprovalRecord, Long> {
    
    /**
     * 根据报销ID查询审批记录
     */
    List<ReimbursementApprovalRecord> findByReimbursementIdOrderByCreateTimeDesc(Long reimbursementId);
}
