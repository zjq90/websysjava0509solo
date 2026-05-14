package com.appsys.repository;

import com.appsys.entity.CheckInRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRecordRepository extends JpaRepository<CheckInRecord, Long> {

    Optional<CheckInRecord> findByUserIdAndCheckInDate(Long userId, LocalDate checkInDate);

    List<CheckInRecord> findByUserIdOrderByCheckInDateDesc(Long userId);

    @Query("SELECT COUNT(c) FROM CheckInRecord c WHERE c.userId = :userId AND c.checkInDate BETWEEN :startDate AND :endDate")
    long countByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
