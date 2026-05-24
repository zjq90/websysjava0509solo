package com.plate.repository;

import com.plate.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Optional<Blacklist> findByPlateNumberAndIsActiveTrue(String plateNumber);
    List<Blacklist> findByIsActiveTrue();
    long countByIsActiveTrue();
    boolean existsByPlateNumberAndIsActiveTrue(String plateNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Blacklist b SET b.isActive = false, b.removeReason = '自动过期' WHERE b.expireAt <= :now AND b.isActive = true")
    int expireBlacklist(LocalDateTime now);
}
