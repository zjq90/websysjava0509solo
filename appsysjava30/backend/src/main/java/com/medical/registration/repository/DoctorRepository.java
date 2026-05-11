package com.medical.registration.repository;

import com.medical.registration.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    List<Doctor> findByDeptCodeAndStatusOrderBySortOrderAsc(String deptCode, Integer status);
    
    List<Doctor> findByStatusOrderBySortOrderAsc(Integer status);
}
