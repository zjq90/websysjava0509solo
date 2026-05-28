package com.club.repository;

import com.club.entity.ActivityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 活动照片Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ActivityPhotoRepository extends JpaRepository<ActivityPhoto, Long>, JpaSpecificationExecutor<ActivityPhoto> {

    List<ActivityPhoto> findByClubIdAndDeletedFalseOrderBySortOrderAsc(Long clubId);

    List<ActivityPhoto> findByActivityIdAndDeletedFalseOrderBySortOrderAsc(Long activityId);

    Optional<ActivityPhoto> findByIdAndDeletedFalse(Long id);
}
