package com.hospital.repository;

import com.hospital.entity.MedicalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医嘱Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface MedicalOrderRepository extends JpaRepository<MedicalOrder, Long> {

    MedicalOrder findByOrderNo(String orderNo);

    List<MedicalOrder> findByHospitalizationId(Long hospitalizationId);

    List<MedicalOrder> findByPatientId(Long patientId);

    List<MedicalOrder> findByStatus(String status);

    List<MedicalOrder> findByOrderType(String orderType);

    @Query("SELECT m FROM MedicalOrder m WHERE m.hospitalizationId = ?1 AND m.status = '待执行'")
    List<MedicalOrder> findPendingOrdersByHospitalizationId(Long hospitalizationId);

    List<MedicalOrder> findByCategory(String category);
}
