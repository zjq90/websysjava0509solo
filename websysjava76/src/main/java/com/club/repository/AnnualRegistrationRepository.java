package com.club.repository;

import com.club.entity.AnnualRegistration;
import com.club.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 年度注册数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface AnnualRegistrationRepository extends JpaRepository<AnnualRegistration, Long>, JpaSpecificationExecutor<AnnualRegistration> {

    /**
     * 根据社团ID查询注册记录
     */
    List<AnnualRegistration> findByClubId(Long clubId);

    /**
     * 根据状态查询注册记录
     */
    List<AnnualRegistration> findByStatus(ApprovalStatus status);

    /**
     * 根据年份查询注册记录
     */
    List<AnnualRegistration> findByRegisterYear(Integer year);

    /**
     * 根据社团ID和年份查询注册记录
     */
    AnnualRegistration findByClubIdAndRegisterYear(Long clubId, Integer year);

    /**
     * 统计各状态注册数量
     */
    long countByStatus(ApprovalStatus status);
}
