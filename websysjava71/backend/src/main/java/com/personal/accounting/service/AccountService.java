package com.personal.accounting.service;

import com.personal.accounting.entity.Account;
import com.personal.accounting.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户服务类
 * 提供账户管理功能
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * 获取所有账户
     */
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findByEnabledTrueOrderBySortOrderAsc();
    }

    /**
     * 根据ID获取账户
     */
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("账户不存在: " + id));
    }

    /**
     * 获取总资产
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalAssets() {
        return accountRepository.calculateTotalAssets();
    }

    /**
     * 创建账户
     */
    @Transactional
    public Account create(Account account) {
        log.debug("创建账户: {}", account.getName());
        
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(account.getInitialBalance() != null ? 
                    account.getInitialBalance() : BigDecimal.ZERO);
        }
        
        Account saved = accountRepository.save(account);
        log.info("账户创建成功: {}", saved.getId());
        return saved;
    }

    /**
     * 更新账户
     */
    @Transactional
    public Account update(Long id, Account account) {
        log.debug("更新账户: {}", id);
        
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("账户不存在: " + id));
        
        existing.setName(account.getName());
        existing.setAccountType(account.getAccountType());
        existing.setCurrency(account.getCurrency());
        existing.setColor(account.getColor());
        existing.setNotes(account.getNotes());
        existing.setIncludeInTotal(account.getIncludeInTotal());
        existing.setSortOrder(account.getSortOrder());
        
        Account saved = accountRepository.save(existing);
        log.info("账户更新成功: {}", saved.getId());
        return saved;
    }

    /**
     * 删除账户（逻辑删除）
     */
    @Transactional
    public void delete(Long id) {
        log.debug("删除账户: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("账户不存在: " + id));
        
        account.setEnabled(false);
        accountRepository.save(account);
        log.info("账户已禁用: {}", id);
    }

    /**
     * 调整账户余额
     */
    @Transactional
    public Account adjustBalance(Long id, BigDecimal newBalance) {
        log.debug("调整账户 {} 余额: {}", id, newBalance);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("账户不存在: " + id));
        
        account.setCurrentBalance(newBalance);
        Account saved = accountRepository.save(account);
        log.info("账户余额已调整: {}", saved.getCurrentBalance());
        return saved;
    }
}
