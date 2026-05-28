package com.club.repository;

import com.club.entity.ActivityParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 活动参与记录Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long>, JpaSpecificationExecutor<ActivityParticipant> {

    Page<ActivityParticipant> findByActivityIdAndDeletedFalseOrderBySignupTimeDesc(Long activityId, Pageable pageable);

    Page<ActivityParticipant> findByUserIdAndDeletedFalseOrderBySignupTimeDesc(Long userId, Pageable pageable);

    List<ActivityParticipant> findByActivityIdAndDeletedFalse(Long activityId);

    List<ActivityParticipant> findByClubIdAndUserIdAndDeletedFalse(Long clubId, Long userId);

    Optional<ActivityParticipant> findByActivityIdAndUserIdAndDeletedFalse(Long activityId, Long userId);

    boolean existsByActivityIdAndUserIdAndDeletedFalse(Long activityId, Long userId);

    long countByActivityIdAndDeletedFalse(Long activityId);

    long countByActivityIdAndCheckedInAndDeletedFalse(Long activityId, Integer checkedIn);
}
