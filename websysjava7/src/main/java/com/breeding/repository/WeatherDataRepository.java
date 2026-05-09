package com.breeding.repository;

import com.breeding.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 气象数据访问接口
 */
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    List<WeatherData> findByLocation(String location);

    List<WeatherData> findByRecordDateBetween(LocalDate startDate, LocalDate endDate);

    List<WeatherData> findByLocationAndRecordDateBetween(String location, LocalDate startDate, LocalDate endDate);

    List<WeatherData> findByExperimentId(Long experimentId);

    @Query("SELECT AVG(w.avgTemperature) FROM WeatherData w WHERE w.location = ?1 AND w.recordDate BETWEEN ?2 AND ?3")
    Double getAverageTemperature(String location, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(w.rainfall) FROM WeatherData w WHERE w.location = ?1 AND w.recordDate BETWEEN ?2 AND ?3")
    Double getTotalRainfall(String location, LocalDate startDate, LocalDate endDate);

    @Query("SELECT AVG(w.sunshineHours) FROM WeatherData w WHERE w.location = ?1 AND w.recordDate BETWEEN ?2 AND ?3")
    Double getAverageSunshineHours(String location, LocalDate startDate, LocalDate endDate);
}
