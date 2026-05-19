package com.pethospital.repository;

import com.pethospital.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByDoctorId(Long doctorId);
    List<Holiday> findByDoctorIdAndHolidayDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);
}
