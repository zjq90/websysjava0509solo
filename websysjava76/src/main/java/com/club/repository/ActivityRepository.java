package com.club.repository;

import com.club.entity.Activity;
import com.club.enums.ActivityType;
import com.club.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {

    /**
     * 根据社团ID查询活动列表
     */
    List<Activity> findByClubId(Long clubId);

    /**
     * 根据活动类型查询活动列表
     */
    List<Activity> findByType(ActivityType type);

    /**
     * 根据审核状态查询活动列表
     */
    List<Activity> findByApprovalStatus(ApprovalStatus status);

    /**
     * 查询需要审批的活动
     */
    List<Activity> findByNeedApprovalTrueAndApprovalStatusIsNull();

    /**
     * 查询内容不合规的活动
     */
    List<Activity> findByContentCompliantFalse();

    /**
     * 查询指定时间范围内的活动
     */
    List<Activity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 统计各类型活动数量
     */
    @Query("SELECT a.type, COUNT(a) FROM Activity a GROUP BY a.type")
    List<Object[]> countByType();

    /**
     * 统计各状态活动数量
     */
    @Query("SELECT a.approvalStatus, COUNT(a) FROM Activity a WHERE a.needApproval = true GROUP BY a.approvalStatus")
    List<Object[]> countByApprovalStatus();

    /**
     * 统计年度活动总数
     */
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.startTime >= :startTime")
    Long countAnnualActivities(LocalDateTime startTime);

    /**
     * 统计待审批活动数量
     */
    long countByNeedApprovalTrueAndApprovalStatus(ApprovalStatus status);
    
    /**
     * 统计指定审批阶段的待审批活动数量
     */
    long countByNeedApprovalTrueAndApprovalStatusAndCurrentApprovalStage(ApprovalStatus status, Integer currentApprovalStage);
    
    /**
     * 统计指定类型的待审批活动数量
     */
    long countByNeedApprovalTrueAndApprovalStatusAndType(ApprovalStatus status, ActivityType type);
    
    /**
     * 统计指定规模的待审批活动数量
     */
    long countByNeedApprovalTrueAndApprovalStatusAndIsLargeScale(ApprovalStatus status, Boolean isLargeScale);
}
