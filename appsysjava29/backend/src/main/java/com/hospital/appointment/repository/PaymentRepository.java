package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByPaymentNo(String paymentNo);
    
    Optional<Payment> findByAppointmentId(Long appointmentId);
    
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
}
