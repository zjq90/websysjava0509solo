package com.psyconsult.repository;

import com.psyconsult.entity.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    List<EmergencyContact> findByEnabledTrueOrderBySortOrderAsc();
    List<EmergencyContact> findByIsHotlineTrueAndEnabledTrueOrderBySortOrderAsc();
}
