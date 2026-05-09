package com.agricultural.repository;

import com.agricultural.entity.Finance;
import com.agricultural.entity.enums.FinanceStatus;
import com.agricultural.entity.enums.FinanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 财务账款数据访问层
 */
@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {

    /**
     * 根据财务单号查找
     */
    Optional<Finance> findByFinanceNo(String financeNo);

    /**
     * 根据财务类型查询
     */
    List<Finance> findByFinanceTypeOrderByFinanceDateDesc(FinanceType financeType);

    /**
     * 根据状态查询
     */
    List<Finance> findByStatusOrderByFinanceDateDesc(FinanceStatus status);

    /**
     * 根据客户ID查询
     */
    List<Finance> findByCustomerIdOrderByFinanceDateDesc(Long customerId);

    /**
     * 根据订单ID查询
     */
    Optional<Finance> findByOrderId(Long orderId);

    /**
     * 检查财务单号是否存在
     */
    boolean existsByFinanceNo(String financeNo);

    /**
     * 统计未结清的账款数量
     */
    @Query("SELECT COUNT(f) FROM Finance f WHERE f.status <> 'SETTLED'")
    long countUnsettled();

    /**
     * 计算指定类型的剩余总金额
     */
    @Query("SELECT COALESCE(SUM(f.remainingAmount), 0) FROM Finance f WHERE f.financeType = :financeType AND f.status <> 'SETTLED'")
    BigDecimal sumRemainingAmountByType(@Param("financeType") FinanceType financeType);

    /**
     * 按日期范围查询
     */
    List<Finance> findByFinanceDateBetweenOrderByFinanceDateDesc(LocalDate startDate, LocalDate endDate);

    /**
     * 查询最大的财务单号
     */
    @Query("SELECT MAX(f.financeNo) FROM Finance f WHERE f.financeNo LIKE :prefix%")
    String findMaxFinanceNoByPrefix(@Param("prefix") String prefix);
}
