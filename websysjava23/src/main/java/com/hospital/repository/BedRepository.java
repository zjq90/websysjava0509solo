package com.hospital.repository;

import com.hospital.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 床位Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {

    Bed findByBedNo(String bedNo);

    List<Bed> findByWardName(String wardName);

    List<Bed> findByStatus(String status);

    List<Bed> findByDepartment(String department);

    @Query("SELECT b FROM Bed b WHERE b.status = '空闲'")
    List<Bed> findAllAvailable();

    @Query("SELECT COUNT(b) FROM Bed b WHERE b.status = ?1")
    long countByStatus(String status);
}
