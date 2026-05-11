package com.medical.appointment.repository;

import com.medical.appointment.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {
    
    Optional<SmsCode> findFirstByPhoneAndTypeAndIsUsedFalseOrderByCreatedAtDesc(String phone, SmsCode.CodeType type);
    
    @Modifying
    @Query("DELETE FROM SmsCode s WHERE s.expireAt < :now")
    int deleteExpiredCodes(LocalDateTime now);
    
    @Query("SELECT COUNT(s) FROM SmsCode s WHERE s.phone = :phone AND s.type = :type AND s.createdAt > :since")
    long countByPhoneAndTypeAndCreatedAtAfter(String phone, SmsCode.CodeType type, LocalDateTime since);
}
