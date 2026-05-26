package com.accounting.service;

import com.accounting.entity.Account;
import com.accounting.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Cacheable(value = "accounts", key = "'all'")
    public List<Account> getAllAccounts() {
        log.info("获取全部账户列表");
        return accountRepository.findAllByOrderBySortOrderAsc();
    }

    @Cacheable(value = "accounts", key = "#id")
    public Account getAccountById(Long id) {
        log.info("根据ID获取账户: {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("账户不存在: " + id));
    }

    @Cacheable(value = "accounts", key = "'totalBalance'")
    public BigDecimal getTotalBalance() {
        log.info("计算总资产");
        return accountRepository.calculateTotalBalance();
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public Account createAccount(Account account) {
        log.info("创建账户: {}", account.getName());
        account.setId(null);
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public Account updateAccount(Long id, Account account) {
        log.info("更新账户: {}", id);
        Account existing = getAccountById(id);
        existing.setName(account.getName());
        existing.setIcon(account.getIcon());
        existing.setBalance(account.getBalance());
        existing.setType(account.getType());
        existing.setRemark(account.getRemark());
        existing.setSortOrder(account.getSortOrder());
        return accountRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public Account updateBalance(Long id, BigDecimal amount) {
        log.info("更新账户余额: id={}, amount={}", id, amount);
        Account account = getAccountById(id);
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public void deleteAccount(Long id) {
        log.info("删除账户: {}", id);
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("账户不存在: " + id);
        }
        accountRepository.deleteById(id);
    }
}
