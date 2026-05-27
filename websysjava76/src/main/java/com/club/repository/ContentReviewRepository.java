package com.club.repository;

import com.club.entity.ContentReview;
import com.club.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 内容审查数据访问层
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface ContentReviewRepository extends JpaRepository<ContentReview, Long>, JpaSpecificationExecutor<ContentReview> {

    /**
     * 根据活动ID查询审查记录
     */
    List<ContentReview> findByActivityId(Long activityId);

    /**
     * 根据状态查询审查记录
     */
    List<ContentReview> findByStatus(ReviewStatus status);

    /**
     * 统计待审核数量
     */
    long countByStatus(ReviewStatus status);

    /**
     * 根据活动ID和字段名查询
     */
    ContentReview findByActivityIdAndFieldName(Long activityId, String fieldName);

    /**
     * 查询待审核的审查记录
     */
    @Query("SELECT c FROM ContentReview c WHERE c.status = 'PENDING' ORDER BY c.createTime DESC")
    List<ContentReview> findPendingReviews();
}
