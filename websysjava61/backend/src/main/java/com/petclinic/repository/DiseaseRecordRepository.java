package com.petclinic.repository;

import com.petclinic.entity.DiseaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 疾病记录Repository
 */
@Repository
public interface DiseaseRecordRepository extends JpaRepository<DiseaseRecord, Long> {

    /**
     * 根据宠物类型查询疾病记录
     */
    List<DiseaseRecord> findByPetType(String petType);

    /**
     * 根据宠物类型和年龄范围查询
     */
    List<DiseaseRecord> findByPetTypeAndPetAgeBetween(String petType, Integer minAge, Integer maxAge);

    /**
     * 根据时间范围查询
     */
    List<DiseaseRecord> findByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询疾病热力图数据（按疾病名称和宠物类型分组统计）
     */
    @Query("SELECT d.diseaseName, d.petType, d.petAge, COUNT(d) FROM DiseaseRecord d " +
           "WHERE d.recordTime BETWEEN :startTime AND :endTime " +
           "GROUP BY d.diseaseName, d.petType, d.petAge")
    List<Object[]> findDiseaseHeatmapData(LocalDateTime startTime, LocalDateTime endTime);
}
