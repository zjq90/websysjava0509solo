package com.petclinic.repository;

import com.petclinic.entity.InsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 保险理赔Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {

    /**
     * 根据理赔状态查询
     */
    List<InsuranceClaim> findByStatus(String status);

    /**
     * 根据问诊ID查询
     */
    List<InsuranceClaim> findByConsultationId(Long consultationId);

    /**
     * 根据问诊编号查询
     */
    List<InsuranceClaim> findByConsultationNo(String consultationNo);

    /**
     * 根据理赔编号查询
     */
    InsuranceClaim findByClaimNo(String claimNo);

    /**
     * 根据宠物名称查询
     */
    List<InsuranceClaim> findByPetNameContaining(String petName);

    /**
     * 根据保险公司查询
     */
    List<InsuranceClaim> findByInsuranceCompanyContaining(String insuranceCompany);
}
