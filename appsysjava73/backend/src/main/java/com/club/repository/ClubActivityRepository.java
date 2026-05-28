package com.club.repository;

import com.club.entity.ClubActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团活动Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ClubActivityRepository extends JpaRepository<ClubActivity, Long>, JpaSpecificationExecutor<ClubActivity> {

    Page<ClubActivity> findByStatusAndDeletedFalseOrderByCreateTimeDesc(Integer status, Pageable pageable);

    Page<ClubActivity> findByClubIdAndDeletedFalseOrderByCreateTimeDesc(Long clubId, Pageable pageable);

    Page<ClubActivity> findByClubIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(Long clubId, Integer status, Pageable pageable);

    List<ClubActivity> findByClubIdAndDeletedFalseOrderByStartTimeDesc(Long clubId);

    List<ClubActivity> findByClubIdAndDeletedFalse(Long clubId);

    List<ClubActivity> findByClubIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(Long clubId, Integer status);

    Optional<ClubActivity> findByIdAndDeletedFalse(Long id);

    long countByClubIdAndDeletedFalse(Long clubId);

    long countByClubIdAndStatusAndDeletedFalse(Long clubId, Integer status);

    @Modifying
    @Query("UPDATE ClubActivity a SET a.viewCount = a.viewCount + 1 WHERE a.id = :activityId")
    void incrementViewCount(@Param("activityId") Long activityId);

    @Modifying
    @Query("UPDATE ClubActivity a SET a.participantCount = a.participantCount + 1 WHERE a.id = :activityId")
    void incrementParticipantCount(@Param("activityId") Long activityId);
}
