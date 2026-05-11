package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findByDoctorIdAndScheduleDateOrderByStartTimeAsc(Long doctorId, LocalDate scheduleDate);
    
    @Query("SELECT s FROM Schedule s WHERE s.status = 1 " +
           "AND s.scheduleDate = :scheduleDate " +
           "AND (:deptId IS NULL OR s.deptId = :deptId) " +
           "AND (:doctorId IS NULL OR s.doctorId = :doctorId) " +
           "AND (:timePeriod IS NULL OR s.timePeriod = :timePeriod) " +
           "ORDER BY s.startTime ASC")
    List<Schedule> findByDateAndConditions(@Param("scheduleDate") LocalDate scheduleDate,
                                            @Param("deptId") Long deptId,
                                            @Param("doctorId") Long doctorId,
                                            @Param("timePeriod") String timePeriod);
    
    @Query("SELECT DISTINCT s.scheduleDate FROM Schedule s " +
           "WHERE s.status = 1 AND s.scheduleDate >= CURRENT_DATE " +
           "ORDER BY s.scheduleDate ASC")
    List<LocalDate> findAvailableDates();
}
