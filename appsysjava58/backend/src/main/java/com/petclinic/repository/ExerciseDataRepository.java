package com.petclinic.repository;

import com.petclinic.entity.ExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 运动数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface ExerciseDataRepository extends JpaRepository<ExerciseData, Long> {

    List<ExerciseData> findByPetIdAndDeletedFalseOrderByRecordTimeDesc(Long petId);

    List<ExerciseData> findByPetIdAndRecordTimeBetweenAndDeletedFalseOrderByRecordTime(Long petId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT e FROM ExerciseData e WHERE e.pet.id = :petId AND FUNCTION('DATE', e.recordTime) = FUNCTION('DATE', :date) AND e.deleted = false ORDER BY e.recordTime DESC")
    List<ExerciseData> findByPetIdAndDate(Long petId, LocalDateTime date);

    List<ExerciseData> findByDeviceIdAndDeletedFalseOrderByRecordTimeDesc(String deviceId);
}