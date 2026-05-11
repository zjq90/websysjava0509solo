package com.medical.registration.repository;

import com.medical.registration.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findByDoctorIdAndScheduleDateAndStatus(Long doctorId, LocalDate scheduleDate, Integer status);
    
    List<Schedule> findByDeptCodeAndScheduleDateAndStatusOrderByStartTimeAsc(String deptCode, LocalDate scheduleDate, Integer status);
    
    List<Schedule> findByDoctorIdAndScheduleDateBetweenAndStatusOrderByScheduleDateAsc(Long doctorId, LocalDate startDate, LocalDate endDate, Integer status);
    
    @Modifying
    @Query("update Schedule s set s.availableCount = s.availableCount - 1 where s.id = :id and s.availableCount > 0")
    int decreaseAvailableCount(@Param("id") Long id);
    
    @Modifying
    @Query("update Schedule s set s.availableCount = s.availableCount + 1 where s.id = :id")
    int increaseAvailableCount(@Param("id") Long id);
}
