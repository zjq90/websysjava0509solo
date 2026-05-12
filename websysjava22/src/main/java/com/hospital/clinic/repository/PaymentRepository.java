package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Patient;
import com.hospital.clinic.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收费Repository
 * 收费数据访问接口
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * 根据收费单号查询
     */
    Payment findByPaymentNo(String paymentNo);

    /**
     * 根据患者查询收费记录
     */
    List<Payment> findByPatient(Patient patient);

    /**
     * 根据状态查询
     */
    List<Payment> findByStatus(String status);
}
