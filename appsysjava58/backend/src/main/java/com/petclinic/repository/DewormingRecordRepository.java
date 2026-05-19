package com.petclinic.repository;

import com.petclinic.entity.DewormingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 驱虫记录数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface DewormingRecordRepository extends JpaRepository<DewormingRecord, Long> {

    List<DewormingRecord> findByPetIdAndDeletedFalseOrderByDewormingDateDesc(Long petId);

    List<DewormingRecord> findByPetIdAndCompletedAndDeletedFalseOrderByDewormingDateDesc(Long petId, Boolean completed);

    @Query("SELECT d FROM DewormingRecord d WHERE d.nextDewormingDate BETWEEN :startDate AND :endDate AND d.deleted = false")
    List<DewormingRecord> findUpcomingReminders(LocalDate startDate, LocalDate endDate);

    List<DewormingRecord> findByReminderSentFalseAndNextDewormingDateBeforeAndDeletedFalse(LocalDate date);
}