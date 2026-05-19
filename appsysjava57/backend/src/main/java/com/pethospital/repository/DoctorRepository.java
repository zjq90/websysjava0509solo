package com.pethospital.repository;

import com.pethospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医生数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByDepartmentAndAvailableTrue(String department);

    List<Doctor> findByAvailableTrueOrderByRatingDesc();
}
