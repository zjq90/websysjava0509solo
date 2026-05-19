package com.pethospital.repository;

import com.pethospital.entity.VaccineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VaccineRecordRepository extends JpaRepository<VaccineRecord, Long> {
    List<VaccineRecord> findByPetId(Long petId);
}
