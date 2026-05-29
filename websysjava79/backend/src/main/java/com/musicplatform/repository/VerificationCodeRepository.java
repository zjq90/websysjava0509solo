package com.musicplatform.repository;

import com.musicplatform.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByTargetAndTypeAndCodeAndUsedFalse(
            String target, VerificationCode.CodeType type, String code);

    void deleteByTargetAndType(String target, VerificationCode.CodeType type);
}
