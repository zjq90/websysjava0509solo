package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Department;
import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班Repository
 * 排班数据访问接口
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * 根据医生和日期查询排班
     */
    List<Schedule> findByDoctorAndScheduleDate(Doctor doctor, LocalDate scheduleDate);

    /**
     * 根据科室和日期查询排班
     */
    List<Schedule> findByDepartmentAndScheduleDate(Department department, LocalDate scheduleDate);

    /**
     * 根据日期查询排班
     */
    List<Schedule> findByScheduleDate(LocalDate scheduleDate);

    /**
     * 根据日期和状态查询
     */
    List<Schedule> findByScheduleDateAndStatus(LocalDate scheduleDate, Integer status);
}
