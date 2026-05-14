package com.photostudio.repository;

import com.photostudio.entity.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报销Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, Long> {
    
    List<Reimbursement> findByStatus(String status);
    
    List<Reimbursement> findByEmployeeId(Long employeeId);
    
    List<Reimbursement> findByEmployeeIdAndStatus(Long employeeId, String status);
}
