package com.psyconsult.repository;

import com.psyconsult.entity.EmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long> {
    List<EmotionRecord> findByUserIdOrderByCreateTimeDesc(Long userId);
    List<EmotionRecord> findByUserIdAndCreateTimeBetweenOrderByCreateTimeAsc(Long userId, LocalDateTime start, LocalDateTime end);
}
