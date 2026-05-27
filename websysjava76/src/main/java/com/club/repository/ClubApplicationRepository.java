package com.club.repository;

import com.club.entity.ClubApplication;
import com.club.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社团成立申请数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long>, JpaSpecificationExecutor<ClubApplication> {

    /**
     * 根据状态查询申请列表
     */
    List<ClubApplication> findByStatus(ApprovalStatus status);

    /**
     * 根据申请人查询申请列表
     */
    List<ClubApplication> findByInitiatorName(String initiatorName);

    /**
     * 根据社团名称查询申请
     */
    ClubApplication findByClubName(String clubName);

    /**
     * 统计各状态申请数量
     */
    long countByStatus(ApprovalStatus status);
}
