package com.accounting.repository;

import com.accounting.entity.Bill;
import com.accounting.enums.BillType;
import com.accounting.enums.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByDeletedFalseOrderByTransactionTimeDesc();

    List<Bill> findByTransactionTimeBetweenAndDeletedFalseOrderByTransactionTimeDesc(
            LocalDateTime start, LocalDateTime end);

    List<Bill> findBySyncStatusInAndDeletedFalse(List<SyncStatus> syncStatuses);

    Optional<Bill> findByClientIdAndDeletedFalse(String clientId);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Bill b " +
           "WHERE b.type = :type AND b.transactionTime BETWEEN :start AND :end AND b.deleted = false")
    BigDecimal calculateTotalByTypeAndDateRange(
            @Param("type") BillType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Bill b " +
           "WHERE b.type = :type AND b.category.id = :categoryId " +
           "AND b.transactionTime BETWEEN :start AND :end AND b.deleted = false")
    BigDecimal calculateTotalByTypeAndCategoryAndDateRange(
            @Param("type") BillType type,
            @Param("categoryId") Long categoryId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT AVG(b.amount) FROM Bill b " +
           "WHERE b.type = 'EXPENSE' AND b.transactionTime BETWEEN :start AND :end AND b.deleted = false")
    BigDecimal calculateAverageExpense(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<Bill> findByTransactionTimeAfterAndDeletedFalseOrderByTransactionTimeDesc(LocalDateTime dateTime);

    boolean existsByClientId(String clientId);
}
