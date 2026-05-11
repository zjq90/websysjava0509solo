package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    List<Doctor> findByDeptIdAndStatusOrderBySortOrderAsc(Long deptId, Integer status);
    
    @Query("SELECT d FROM Doctor d WHERE d.status = :status " +
           "AND (:deptId IS NULL OR d.deptId = :deptId) " +
           "AND (:titleLevel IS NULL OR d.titleLevel = :titleLevel) " +
           "AND (:isExpert IS NULL OR d.isExpert = :isExpert) " +
           "ORDER BY d.sortOrder ASC")
    List<Doctor> findByConditions(@Param("status") Integer status,
                                   @Param("deptId") Long deptId,
                                   @Param("titleLevel") String titleLevel,
                                   @Param("isExpert") Integer isExpert);
    
    List<Doctor> findByStatusOrderBySortOrderAsc(Integer status);
}
