package com.club.management.repository;

import com.club.management.entity.ActivitySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 活动总结数据访问接口
 * 
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ActivitySummaryRepository extends JpaRepository<ActivitySummary, Long> {

    /**
     * 根据活动ID查询总结
     */
    Optional<ActivitySummary> findByActivityIdAndDeletedFalse(Long activityId);

    /**
     * 根据社团ID分页查询总结
     */
    Page<ActivitySummary> findByClubIdAndDeletedFalse(Long clubId, Pageable pageable);

    /**
     * 根据社团ID和公开状态分页查询总结
     */
    Page<ActivitySummary> findByClubIdAndIsPublicAndPublishedAndDeletedFalse(
            Long clubId, Boolean isPublic, Boolean published, Pageable pageable);

    /**
     * 检查活动是否已有总结
     */
    boolean existsByActivityIdAndDeletedFalse(Long activityId);
}
