package com.medical.appointment.repository;

import com.medical.appointment.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    
    List<DoctorSchedule> findByDoctor_IdAndScheduleDateBetweenOrderByScheduleDateAsc(
        Long doctorId, LocalDate startDate, LocalDate endDate);
    
    List<DoctorSchedule> findByDoctor_IdAndScheduleDateOrderByTimeSlotAsc(Long doctorId, LocalDate scheduleDate);
    
    Optional<DoctorSchedule> findByDoctor_IdAndScheduleDateAndTimeSlot(
        Long doctorId, LocalDate scheduleDate, DoctorSchedule.TimeSlot timeSlot);
    
    List<DoctorSchedule> findByDoctor_IdAndIsActiveTrueOrderByScheduleDateAsc(Long doctorId);
}
