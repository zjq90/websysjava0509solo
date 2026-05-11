package com.hospital.appointment.repository;

import com.hospital.appointment.entity.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {
    
    List<ScheduleSlot> findByScheduleIdOrderBySlotIndexAsc(Long scheduleId);
    
    @Query("SELECT s FROM ScheduleSlot s WHERE s.scheduleId = :scheduleId " +
           "AND s.status IN ('available', 'locked') ORDER BY s.slotIndex ASC")
    List<ScheduleSlot> findAvailableSlots(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT s FROM ScheduleSlot s WHERE s.status = 'locked' " +
           "AND s.lockExpireTime < :now")
    List<ScheduleSlot> findExpiredLocks(@Param("now") LocalDateTime now);
    
    Optional<ScheduleSlot> findByScheduleIdAndSlotIndex(Long scheduleId, Integer slotIndex);
}
