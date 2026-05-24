package com.bikeshare.repository;

import com.bikeshare.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付记录数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {

    List<PaymentRecord> findByUserId(Long userId);

    List<PaymentRecord> findByPaymentType(String paymentType);

    @Query("SELECT p.paymentType, COALESCE(SUM(p.amount), 0) FROM PaymentRecord p WHERE p.createTime >= :startTime GROUP BY p.paymentType")
    List<Object[]> sumAmountByType(LocalDateTime startTime);
}
