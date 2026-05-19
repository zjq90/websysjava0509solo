package com.petclinic.repository;

import com.petclinic.entity.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 保险公司Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long> {

    /**
     * 根据状态查询保险公司列表
     */
    List<InsuranceCompany> findByStatus(String status);

    /**
     * 根据公司编码查询
     */
    Optional<InsuranceCompany> findByCompanyCode(String companyCode);

    /**
     * 根据公司名称模糊查询
     */
    List<InsuranceCompany> findByCompanyNameContaining(String companyName);

    /**
     * 查询支持自动理赔的保险公司
     */
    List<InsuranceCompany> findByAutoClaimEnabledTrueAndStatus(String status);
}
