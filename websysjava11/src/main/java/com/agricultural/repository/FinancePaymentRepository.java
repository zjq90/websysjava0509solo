package com.agricultural.repository;

import com.agricultural.entity.FinancePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 收付款记录数据访问层
 */
@Repository
public interface FinancePaymentRepository extends JpaRepository<FinancePayment, Long> {

    /**
     * 根据付款单号查找
     */
    Optional<FinancePayment> findByPaymentNo(String paymentNo);

    /**
     * 根据财务ID查询所有收付款记录
     */
    List<FinancePayment> findByFinanceIdOrderByPaymentDateDesc(Long financeId);

    /**
     * 计算指定财务ID的已收/已付总金额
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM FinancePayment p WHERE p.finance.id = :financeId")
    BigDecimal sumAmountByFinanceId(@Param("financeId") Long financeId);

    /**
     * 检查付款单号是否存在
     */
    boolean existsByPaymentNo(String paymentNo);
}
