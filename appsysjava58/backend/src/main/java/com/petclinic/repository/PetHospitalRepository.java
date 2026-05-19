package com.petclinic.repository;

import com.petclinic.entity.PetHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 宠物医院数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface PetHospitalRepository extends JpaRepository<PetHospital, Long> {

    List<PetHospital> findByCityAndStatusAndDeletedFalse(String city, String status);

    List<PetHospital> findByStatusAndDeletedFalseOrderByAverageRatingDesc(String status);

    @Query("SELECT h FROM PetHospital h WHERE h.latitude BETWEEN :minLat AND :maxLat AND h.longitude BETWEEN :minLng AND :maxLng AND h.status = 'ACTIVE' AND h.deleted = false")
    List<PetHospital> findNearbyHospitals(BigDecimal minLat, BigDecimal maxLat, BigDecimal minLng, BigDecimal maxLng);

    List<PetHospital> findByNameContainingAndStatusAndDeletedFalse(String name, String status);

    List<PetHospital> findByIs24hTrueAndStatusAndDeletedFalse(String status);

    List<PetHospital> findByIsEmergencyTrueAndStatusAndDeletedFalse(String status);
}