package com.medical.registration.repository;

import com.medical.registration.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    
    Optional<Registration> findByRegistrationNo(String registrationNo);
    
    @Query("select r from Registration r where r.userId = :userId order by r.createTime desc")
    Page<Registration> findByUserIdOrderByCreateTimeDesc(@Param("userId") Long userId, Pageable pageable);
    
    @Query("select r from Registration r where r.userId = :userId and r.status = :status order by r.createTime desc")
    List<Registration> findByUserIdAndStatusOrderByCreateTimeDesc(@Param("userId") Long userId, @Param("status") String status);
    
    @Query("select r from Registration r where r.userId = :userId and r.visitDate = :visitDate and r.status in ('BOOKED','VISITING')")
    List<Registration> findTodayAppointments(@Param("userId") Long userId, @Param("visitDate") LocalDate visitDate);
    
    @Query("select count(r) > 0 from Registration r where r.userId = :userId and r.scheduleId = :scheduleId and r.status in ('BOOKED','VISITING','VISITED')")
    boolean existsByUserIdAndScheduleIdAndStatus(@Param("userId") Long userId, @Param("scheduleId") Long scheduleId);
}
