package com.personal.accounting.repository;

import com.personal.accounting.entity.Transaction;
import com.personal.accounting.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易记录数据访问接口
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * 根据时间范围查询交易记录
     */
    List<Transaction> findByTransactionTimeBetweenOrderByTransactionTimeDesc(
            LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据类型和时间范围统计金额
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type AND t.transactionTime BETWEEN :startTime AND :endTime")
    BigDecimal sumAmountByTypeAndTimeBetween(
            @Param("type") TransactionType type,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据分类ID和时间范围统计支出金额
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE' AND t.category.id = :categoryId AND t.transactionTime BETWEEN :startTime AND :endTime")
    BigDecimal sumExpenseByCategoryAndTimeBetween(
            @Param("categoryId") Long categoryId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 查询异常交易
     */
    List<Transaction> findByIsAbnormalTrueOrderByTransactionTimeDesc();

    /**
     * 根据账户ID查询交易记录
     */
    List<Transaction> findByAccountIdOrderByTransactionTimeDesc(Long accountId);

    /**
     * 按分类统计支出
     */
    @Query("SELECT t.category.id, t.category.name, t.category.color, COALESCE(SUM(t.amount), 0) " +
           "FROM Transaction t WHERE t.type = 'EXPENSE' AND t.transactionTime BETWEEN :startTime AND :endTime " +
           "GROUP BY t.category.id, t.category.name, t.category.color " +
           "ORDER BY SUM(t.amount) DESC")
    List<Object[]> sumExpenseGroupByCategory(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 按月统计收支
     */
    @Query("SELECT FUNCTION('YEAR', t.transactionTime), FUNCTION('MONTH', t.transactionTime), t.type, COALESCE(SUM(t.amount), 0) " +
           "FROM Transaction t WHERE t.transactionTime BETWEEN :startTime AND :endTime " +
           "GROUP BY FUNCTION('YEAR', t.transactionTime), FUNCTION('MONTH', t.transactionTime), t.type " +
           "ORDER BY FUNCTION('YEAR', t.transactionTime), FUNCTION('MONTH', t.transactionTime)")
    List<Object[]> sumByMonth(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 按小时统计支出分布
     */
    @Query("SELECT FUNCTION('HOUR', t.transactionTime), COALESCE(SUM(t.amount), 0) " +
           "FROM Transaction t WHERE t.type = 'EXPENSE' AND t.transactionTime BETWEEN :startTime AND :endTime " +
           "GROUP BY FUNCTION('HOUR', t.transactionTime) " +
           "ORDER BY FUNCTION('HOUR', t.transactionTime)")
    List<Object[]> sumExpenseByHour(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 批量更新分类
     */
    @Query("UPDATE Transaction t SET t.category.id = :categoryId WHERE t.id IN :ids")
    int updateCategoryByIds(@Param("ids") List<Long> ids, @Param("categoryId") Long categoryId);

    /**
     * 批量更新标签
     */
    @Query("UPDATE Transaction t SET t.tags = :tags WHERE t.id IN :ids")
    int updateTagsByIds(@Param("ids") List<Long> ids, @Param("tags") String tags);
}
