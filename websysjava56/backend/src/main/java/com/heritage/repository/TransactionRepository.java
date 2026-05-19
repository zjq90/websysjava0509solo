package com.heritage.repository;

import com.heritage.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Page<Transaction> findByHeritageId(Long heritageId, Pageable pageable);

    Page<Transaction> findByTransactionType(String transactionType, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.transactionTime BETWEEN :startTime AND :endTime AND t.deleted = false")
    List<Transaction> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT AVG(t.transactionPrice) FROM Transaction t WHERE t.heritageCode = :heritageCode AND t.deleted = false")
    BigDecimal calculateAveragePrice(@Param("heritageCode") String heritageCode);

    @Query("SELECT MAX(t.transactionPrice) FROM Transaction t WHERE t.heritageCode = :heritageCode AND t.deleted = false")
    BigDecimal findMaxPrice(@Param("heritageCode") String heritageCode);

    @Query("SELECT MIN(t.transactionPrice) FROM Transaction t WHERE t.heritageCode = :heritageCode AND t.deleted = false")
    BigDecimal findMinPrice(@Param("heritageCode") String heritageCode);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.heritageCode = :heritageCode AND t.deleted = false")
    long countByHeritageCode(@Param("heritageCode") String heritageCode);
}
