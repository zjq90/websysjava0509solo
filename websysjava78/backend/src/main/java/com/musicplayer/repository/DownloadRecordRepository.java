package com.musicplayer.repository;

import com.musicplayer.entity.DownloadRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DownloadRecordRepository extends JpaRepository<DownloadRecord, Long> {

    Page<DownloadRecord> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(d) FROM DownloadRecord d WHERE d.user.id = :userId AND d.downloadedAt >= :startOfDay")
    long countTodayDownloadsByUser(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay);

    List<DownloadRecord> findByUserIdAndMusicId(Long userId, Long musicId);
}
