package com.personal.accounting.repository;

import com.personal.accounting.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户数据访问接口
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * 查询所有启用的账户
     */
    List<Account> findByEnabledTrueOrderBySortOrderAsc();

    /**
     * 计算总资产
     */
    @Query("SELECT COALESCE(SUM(a.currentBalance), 0) FROM Account a WHERE a.enabled = true AND a.includeInTotal = true")
    BigDecimal calculateTotalAssets();

    /**
     * 根据账户名称查询
     */
    List<Account> findByNameContainingAndEnabledTrue(String name);
}
