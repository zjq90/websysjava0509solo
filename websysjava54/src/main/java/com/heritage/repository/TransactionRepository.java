package com.heritage.repository;

import com.heritage.entity.Transaction;
import com.heritage.enums.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findByIsAbnormalTrue();

    List<Transaction> findByFundsFrozenTrue();

    List<Transaction> findByFreezeAuditStatus(AuditStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.buyerId = ?1 OR t.sellerId = ?1 ORDER BY t.createTime DESC")
    List<Transaction> findByUserId(Long userId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE (t.buyerId = ?1 OR t.sellerId = ?1) AND t.createTime >= ?2")
    Long countByUserIdAndCreateTimeAfter(Long userId, LocalDateTime startTime);

    List<Transaction> findByCreateTimeAfterOrderByCreateTimeDesc(LocalDateTime startTime);
}
