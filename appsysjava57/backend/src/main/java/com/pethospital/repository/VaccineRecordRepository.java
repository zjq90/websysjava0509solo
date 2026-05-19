package com.pethospital.repository;

import com.pethospital.entity.VaccineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 疫苗记录数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface VaccineRecordRepository extends JpaRepository<VaccineRecord, Long> {

    List<VaccineRecord> findByPetIdOrderByVaccinationDateDesc(Long petId);

    @Query("SELECT v FROM VaccineRecord v WHERE v.expiryDate BETWEEN :startDate AND :endDate AND v.reminderSent = false")
    List<VaccineRecord> findExpiringVaccines(LocalDate startDate, LocalDate endDate);
}
