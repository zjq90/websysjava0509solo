package com.club.repository;

import com.club.entity.FinanceRecord;
import com.club.enums.FinanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 财务记录数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long>, JpaSpecificationExecutor<FinanceRecord> {

    /**
     * 根据社团ID查询财务记录
     */
    List<FinanceRecord> findByClubId(Long clubId);

    /**
     * 根据财务类型查询记录
     */
    List<FinanceRecord> findByType(FinanceType type);

    /**
     * 查询异常财务记录
     */
    List<FinanceRecord> findByAbnormalTrue();

    /**
     * 查询标记调查的财务记录
     */
    List<FinanceRecord> findByMarkedForInvestigationTrue();

    /**
     * 查询指定日期范围内的记录
     */
    List<FinanceRecord> findByOccurDateBetween(LocalDate start, LocalDate end);

    /**
     * 统计社团总收入
     */
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.clubId = :clubId AND f.type = 'INCOME'")
    BigDecimal sumIncomeByClubId(Long clubId);

    /**
     * 统计社团总支出
     */
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.clubId = :clubId AND f.type = 'EXPENSE'")
    BigDecimal sumExpenseByClubId(Long clubId);

    /**
     * 统计全校总收入
     */
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.type = 'INCOME'")
    BigDecimal sumTotalIncome();

    /**
     * 统计全校总支出
     */
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.type = 'EXPENSE'")
    BigDecimal sumTotalExpense();

    /**
     * 统计异常记录数量
     */
    long countByAbnormalTrue();

    /**
     * 统计待调查记录数量
     */
    long countByMarkedForInvestigationTrueAndInvestigationStatus(Integer status);
}
