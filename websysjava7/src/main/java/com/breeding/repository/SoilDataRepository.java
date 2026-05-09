package com.breeding.repository;

import com.breeding.entity.SoilData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 土壤数据访问接口
 */
@Repository
public interface SoilDataRepository extends JpaRepository<SoilData, Long> {

    List<SoilData> findByLocation(String location);

    List<SoilData> findBySoilType(String soilType);

    List<SoilData> findByExperimentId(Long experimentId);

    List<SoilData> findByLocationAndRecordDateBetween(String location, LocalDate startDate, LocalDate endDate);

    @Query("SELECT AVG(s.phValue) FROM SoilData s WHERE s.location = ?1")
    Double getAveragePhValue(String location);

    @Query("SELECT AVG(s.organicMatter) FROM SoilData s WHERE s.location = ?1")
    Double getAverageOrganicMatter(String location);
}
