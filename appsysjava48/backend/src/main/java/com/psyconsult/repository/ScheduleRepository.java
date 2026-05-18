package com.psyconsult.repository;

import com.psyconsult.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByCounselorIdAndDate(Long counselorId, LocalDate date);

    List<Schedule> findByCounselorIdAndDateBetween(Long counselorId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT s FROM Schedule s WHERE s.counselorId = ?1 AND s.date = ?2 AND s.status = 1 AND s.isAvailable = true")
    List<Schedule> findAvailableSchedules(Long counselorId, LocalDate date);

    @Query("SELECT s FROM Schedule s WHERE s.counselorId = ?1 AND s.date = ?2 AND s.startTime < ?3 AND s.endTime > ?4 AND s.status = 1")
    List<Schedule> findConflictingSchedules(Long counselorId, LocalDate date, LocalTime endTime, LocalTime startTime);
}
