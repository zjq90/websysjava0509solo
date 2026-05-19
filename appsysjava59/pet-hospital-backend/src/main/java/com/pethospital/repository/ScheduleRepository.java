package com.pethospital.repository;

import com.pethospital.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDoctorId(Long doctorId);
    List<Schedule> findByDoctorIdAndScheduleDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);
    List<Schedule> findByScheduleDateBetween(LocalDate startDate, LocalDate endDate);
}
