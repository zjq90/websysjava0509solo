package com.club.repository;

import com.club.entity.ClubNotification;
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

@Repository
public interface ClubNotificationRepository extends JpaRepository<ClubNotification, Long>, JpaSpecificationExecutor<ClubNotification> {

    Page<ClubNotification> findByUserIdAndDeletedFalseOrderByCreateTimeDesc(Long userId, Pageable pageable);

    Page<ClubNotification> findByUserIdAndTypeAndDeletedFalseOrderByCreateTimeDesc(Long userId, String type, Pageable pageable);

    List<ClubNotification> findByUserIdAndReadAndDeletedFalseOrderByCreateTimeDesc(Long userId, Integer read);

    List<ClubNotification> findByUserIdAndClubIdAndDeletedFalseOrderByCreateTimeDesc(Long userId, Long clubId);

    Optional<ClubNotification> findByIdAndDeletedFalse(Long id);

    Optional<ClubNotification> findByIdAndUserIdAndDeletedFalse(Long id, Long userId);

    long countByUserIdAndReadAndDeletedFalse(Long userId, Integer read);

    @Modifying
    @Query("UPDATE ClubNotification n SET n.read = 1 WHERE n.userId = :userId AND n.deleted = false")
    void markAllAsRead(@Param("userId") Long userId);
}
