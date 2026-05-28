package com.club.management.repository;

import com.club.management.entity.Activity;
import com.club.management.entity.enums.ActivityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动数据访问接口
 * 
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /**
     * 根据社团ID分页查询活动
     */
    Page<Activity> findByClubIdAndDeletedFalse(Long clubId, Pageable pageable);

    /**
     * 根据社团ID和状态查询活动
     */
    Page<Activity> findByClubIdAndStatusAndDeletedFalse(Long clubId, ActivityStatus status, Pageable pageable);

    /**
     * 根据状态分页查询活动
     */
    Page<Activity> findByStatusAndDeletedFalse(ActivityStatus status, Pageable pageable);

    /**
     * 根据状态列表查询活动
     */
    Page<Activity> findByStatusInAndDeletedFalse(List<ActivityStatus> statuses, Pageable pageable);

    /**
     * 查询需要更新状态的活动（定时任务使用）
     */
    @Query("SELECT a FROM Activity a WHERE a.deleted = false AND a.status IN (:currentStatuses) " +
           "AND :currentTime BETWEEN a.registrationStartTime AND a.registrationEndTime")
    List<Activity> findActivitiesToOpenRegistration(
            @Param("currentStatuses") List<ActivityStatus> currentStatuses,
            @Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询需要关闭报名的活动
     */
    @Query("SELECT a FROM Activity a WHERE a.deleted = false AND a.status = :currentStatus " +
           "AND :currentTime > a.registrationEndTime")
    List<Activity> findActivitiesToCloseRegistration(
            @Param("currentStatus") ActivityStatus currentStatus,
            @Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询需要开始的活动
     */
    @Query("SELECT a FROM Activity a WHERE a.deleted = false AND a.status = :currentStatus " +
           "AND :currentTime >= a.startTime")
    List<Activity> findActivitiesToStart(
            @Param("currentStatus") ActivityStatus currentStatus,
            @Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询需要结束的活动
     */
    @Query("SELECT a FROM Activity a WHERE a.deleted = false AND a.status = :currentStatus " +
           "AND :currentTime > a.endTime")
    List<Activity> findActivitiesToComplete(
            @Param("currentStatus") ActivityStatus currentStatus,
            @Param("currentTime") LocalDateTime currentTime);

    /**
     * 根据社团ID和归档状态分页查询活动
     */
    Page<Activity> findByClubIdAndArchivedAndDeletedFalse(Long clubId, Boolean archived, Pageable pageable);

    /**
     * 查询社团活动总数
     */
    long countByClubIdAndDeletedFalse(Long clubId);

    /**
     * 模糊搜索活动
     */
    @Query("SELECT a FROM Activity a WHERE a.deleted = false AND a.archived = :archived " +
           "AND (:keyword IS NULL OR a.name LIKE CONCAT('%', :keyword, '%') OR a.description LIKE CONCAT('%', :keyword, '%'))")
    Page<Activity> searchActivities(
            @Param("keyword") String keyword,
            @Param("archived") Boolean archived,
            Pageable pageable);
}
