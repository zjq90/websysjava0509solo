package com.petclinic.repository;

import com.petclinic.entity.CheckupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 体检记录数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface CheckupRecordRepository extends JpaRepository<CheckupRecord, Long> {

    List<CheckupRecord> findByPetIdAndDeletedFalseOrderByCheckupDateDesc(Long petId);

    List<CheckupRecord> findByPetIdAndCompletedAndDeletedFalseOrderByCheckupDateDesc(Long petId, Boolean completed);

    @Query("SELECT c FROM CheckupRecord c WHERE c.nextCheckupDate BETWEEN :startDate AND :endDate AND c.deleted = false")
    List<CheckupRecord> findUpcomingReminders(LocalDate startDate, LocalDate endDate);

    List<CheckupRecord> findByReminderSentFalseAndNextCheckupDateBeforeAndDeletedFalse(LocalDate date);
}