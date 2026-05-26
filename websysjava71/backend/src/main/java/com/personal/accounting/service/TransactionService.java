package com.personal.accounting.service;

import com.personal.accounting.dto.TransactionDTO;
import com.personal.accounting.entity.Account;
import com.personal.accounting.entity.Category;
import com.personal.accounting.entity.Transaction;
import com.personal.accounting.entity.enums.TransactionType;
import com.personal.accounting.repository.AccountRepository;
import com.personal.accounting.repository.CategoryRepository;
import com.personal.accounting.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易记录服务类
 * 提供交易记录的CRUD和批量操作功能
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    /**
     * 分页查询交易记录
     */
    @Transactional(readOnly = true)
    public Page<TransactionDTO> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(this::convertToDTO);
    }

    /**
     * 根据时间范围查询交易记录
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionTimeBetweenOrderByTransactionTimeDesc(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询交易记录
     */
    @Transactional(readOnly = true)
    public TransactionDTO findById(Long id) {
        return transactionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("交易记录不存在: " + id));
    }

    /**
     * 创建交易记录
     */
    @Transactional
    public TransactionDTO create(TransactionDTO dto) {
        log.debug("创建交易记录: {}", dto);
        
        Transaction transaction = new Transaction();
        updateTransactionFromDTO(transaction, dto);
        
        // 更新账户余额
        updateAccountBalance(transaction, null);
        
        transaction = transactionRepository.save(transaction);
        log.info("交易记录创建成功: {}", transaction.getId());
        
        return convertToDTO(transaction);
    }

    /**
     * 更新交易记录
     */
    @Transactional
    public TransactionDTO update(Long id, TransactionDTO dto) {
        log.debug("更新交易记录: {}", id);
        
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("交易记录不存在: " + id));
        
        BigDecimal oldAmount = transaction.getAmount();
        TransactionType oldType = transaction.getType();
        Account oldAccount = transaction.getAccount();
        
        updateTransactionFromDTO(transaction, dto);
        
        // 如果金额或账户有变化，更新账户余额
        if (oldAmount.compareTo(transaction.getAmount()) != 0 
                || oldType != transaction.getType()
                || !oldAccount.getId().equals(transaction.getAccount().getId())) {
            // 先撤销旧的影响
            revertAccountBalance(oldType, oldAmount, oldAccount);
            // 应用新的影响
            updateAccountBalance(transaction, null);
        }
        
        transaction = transactionRepository.save(transaction);
        log.info("交易记录更新成功: {}", transaction.getId());
        
        return convertToDTO(transaction);
    }

    /**
     * 删除交易记录
     */
    @Transactional
    public void delete(Long id) {
        log.debug("删除交易记录: {}", id);
        
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("交易记录不存在: " + id));
        
        // 恢复账户余额
        revertAccountBalance(transaction.getType(), transaction.getAmount(), transaction.getAccount());
        
        transactionRepository.delete(transaction);
        log.info("交易记录删除成功: {}", id);
    }

    /**
     * 批量删除交易记录
     */
    @Transactional
    public void batchDelete(List<Long> ids) {
        log.debug("批量删除交易记录: {}", ids);
        
        for (Long id : ids) {
            Transaction transaction = transactionRepository.findById(id).orElse(null);
            if (transaction != null) {
                revertAccountBalance(transaction.getType(), transaction.getAmount(), transaction.getAccount());
                transactionRepository.delete(transaction);
            }
        }
        
        log.info("批量删除完成，共删除 {} 条记录", ids.size());
    }

    /**
     * 批量更新分类
     */
    @Transactional
    public int batchUpdateCategory(List<Long> ids, Long categoryId) {
        log.debug("批量更新分类: ids={}, categoryId={}", ids, categoryId);
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + categoryId));
        
        int count = transactionRepository.updateCategoryByIds(ids, categoryId);
        log.info("批量更新分类完成，共更新 {} 条记录", count);
        
        return count;
    }

    /**
     * 批量更新标签
     */
    @Transactional
    public int batchUpdateTags(List<Long> ids, String tags) {
        log.debug("批量更新标签: ids={}, tags={}", ids, tags);
        
        int count = transactionRepository.updateTagsByIds(ids, tags);
        log.info("批量更新标签完成，共更新 {} 条记录", count);
        
        return count;
    }

    /**
     * 复制交易记录
     */
    @Transactional
    public TransactionDTO copy(Long id) {
        log.debug("复制交易记录: {}", id);
        
        Transaction original = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("交易记录不存在: " + id));
        
        Transaction copy = new Transaction();
        copy.setType(original.getType());
        copy.setAmount(original.getAmount());
        copy.setCategory(original.getCategory());
        copy.setAccount(original.getAccount());
        copy.setTransactionTime(LocalDateTime.now());
        copy.setDescription(original.getDescription());
        copy.setNotes(original.getNotes());
        copy.setTags(original.getTags());
        copy.setMerchant(original.getMerchant());
        copy.setLocation(original.getLocation());
        copy.setIsAbnormal(false);
        
        // 更新账户余额
        updateAccountBalance(copy, null);
        
        copy = transactionRepository.save(copy);
        log.info("交易记录复制成功: {}", copy.getId());
        
        return convertToDTO(copy);
    }

    /**
     * 更新账户余额
     */
    private void updateAccountBalance(Transaction transaction, BigDecimal overrideAmount) {
        Account account = transaction.getAccount();
        BigDecimal amount = overrideAmount != null ? overrideAmount : transaction.getAmount();
        
        if (transaction.getType() == TransactionType.INCOME) {
            account.setCurrentBalance(account.getCurrentBalance().add(amount));
        } else {
            account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
        }
        
        accountRepository.save(account);
    }

    /**
     * 撤销账户余额变更
     */
    private void revertAccountBalance(TransactionType type, BigDecimal amount, Account account) {
        if (type == TransactionType.INCOME) {
            account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
        } else {
            account.setCurrentBalance(account.getCurrentBalance().add(amount));
        }
        accountRepository.save(account);
    }

    /**
     * 从DTO更新实体
     */
    private void updateTransactionFromDTO(Transaction transaction, TransactionDTO dto) {
        transaction.setType(TransactionType.valueOf(dto.getType()));
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionTime(dto.getTransactionTime() != null ? dto.getTransactionTime() : LocalDateTime.now());
        transaction.setDescription(dto.getDescription());
        transaction.setNotes(dto.getNotes());
        transaction.setTags(dto.getTags());
        transaction.setMerchant(dto.getMerchant());
        transaction.setLocation(dto.getLocation());
        
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + dto.getCategoryId()));
            transaction.setCategory(category);
        }
        
        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("账户不存在: " + dto.getAccountId()));
            transaction.setAccount(account);
        }
    }

    /**
     * 转换为DTO
     */
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setType(transaction.getType().name());
        dto.setAmount(transaction.getAmount());
        dto.setCategoryId(transaction.getCategory().getId());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setCategoryColor(transaction.getCategory().getColor());
        dto.setAccountId(transaction.getAccount().getId());
        dto.setAccountName(transaction.getAccount().getName());
        dto.setTransactionTime(transaction.getTransactionTime());
        dto.setDescription(transaction.getDescription());
        dto.setNotes(transaction.getNotes());
        dto.setTags(transaction.getTags());
        dto.setMerchant(transaction.getMerchant());
        dto.setLocation(transaction.getLocation());
        dto.setIsAbnormal(transaction.getIsAbnormal());
        dto.setAbnormalReason(transaction.getAbnormalReason());
        return dto;
    }
}
