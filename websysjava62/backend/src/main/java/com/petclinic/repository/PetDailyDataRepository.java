package com.petclinic.repository;

import com.petclinic.entity.PetDailyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宠物日常数据Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface PetDailyDataRepository extends JpaRepository<PetDailyData, Long> {

    /**
     * 根据设备ID查询数据
     */
    List<PetDailyData> findByDeviceId(Long deviceId);

    /**
     * 根据宠物名称查询
     */
    List<PetDailyData> findByPetNameContaining(String petName);

    /**
     * 根据时间范围查询
     */
    List<PetDailyData> findByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据状态查询
     */
    List<PetDailyData> findByStatus(String status);

    /**
     * 查询最近N条数据
     */
    @Query(value = "SELECT * FROM pet_daily_data ORDER BY record_time DESC LIMIT ?1", nativeQuery = true)
    List<PetDailyData> findTopNRecords(int n);
}
