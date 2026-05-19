package com.petclinic.repository;

import com.petclinic.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班Repository
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * 根据医生ID和日期查询排班
     */
    List<Schedule> findByDoctorIdAndScheduleDate(Long doctorId, LocalDate scheduleDate);

    /**
     * 根据科室ID和日期查询排班
     */
    List<Schedule> findByDepartmentIdAndScheduleDate(Long departmentId, LocalDate scheduleDate);

    /**
     * 根据日期范围查询排班
     */
    List<Schedule> findByScheduleDateBetween(LocalDate startDate, LocalDate endDate);
}
