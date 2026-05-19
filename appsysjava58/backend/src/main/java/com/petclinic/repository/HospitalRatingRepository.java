package com.petclinic.repository;

import com.petclinic.entity.HospitalRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医院评分数据访问接口
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Repository
public interface HospitalRatingRepository extends JpaRepository<HospitalRating, Long> {

    List<HospitalRating> findByHospitalIdAndDeletedFalse(Long hospitalId);

    List<HospitalRating> findByHospitalIdAndDeletedFalseOrderByCreatedTimeDesc(Long hospitalId);

    List<HospitalRating> findByUserIdAndDeletedFalse(Long userId);
}