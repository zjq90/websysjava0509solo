package com.accounting.system.service.impl;

import com.accounting.system.common.BusinessException;
import com.accounting.system.common.ResultCode;
import com.accounting.system.dto.TransactionDTO;
import com.accounting.system.dto.TransactionQueryDTO;
import com.accounting.system.entity.CategoryRule;
import com.accounting.system.entity.Tag;
import com.accounting.system.entity.Transaction;
import com.accounting.system.entity.TransactionTag;
import com.accounting.system.mapper.TransactionMapper;
import com.accounting.system.mapper.TransactionTagMapper;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.CategoryRuleService;
import com.accounting.system.service.TagService;
import com.accounting.system.service.TransactionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 交易记录Service实现类
 */
@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction>
        implements TransactionService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TransactionTagMapper transactionTagMapper;

    @Autowired
    private CategoryRuleService categoryRuleService;

    @Override
    public IPage<Transaction> pageList(Page<Transaction> page, TransactionQueryDTO query) {
        IPage<Transaction> result = baseMapper.selectPageWithDetail(page, query);
        // 填充标签信息
        result.getRecords().forEach(transaction ->
                transaction.setTags(tagService.listByTransactionId(transaction.getId())));
        return result;
    }

    @Override
    public List<Transaction> listAll(TransactionQueryDTO query) {
        List<Transaction> list = baseMapper.selectAllWithDetail(query);
        // 填充标签信息
        list.forEach(transaction ->
                transaction.setTags(tagService.listByTransactionId(transaction.getId())));
        return list;
    }

    @Override
    public Transaction getDetailById(Long id) {
        Transaction transaction = baseMapper.selectDetailById(id);
        if (transaction == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        transaction.setTags(tagService.listByTransactionId(id));
        return transaction;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction addTransaction(TransactionDTO dto) {
        // 自动填充交易时间
        if (dto.getTransactionTime() == null) {
            dto.setTransactionTime(LocalDateTime.now());
        }

        // 应用分类规则自动匹配
        if (dto.getCategoryId() == null) {
            Long matchedCategoryId = applyCategoryRule(dto);
            if (matchedCategoryId != null) {
                dto.setCategoryId(matchedCategoryId);
            }
        }

        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(dto, transaction);
        save(transaction);

        // 更新账户余额
        updateAccountBalance(transaction, true);

        // 保存标签关联
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            saveTransactionTags(transaction.getId(), dto.getTagIds());
        }

        return getDetailById(transaction.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction updateTransaction(TransactionDTO dto) {
        Transaction existTransaction = getById(dto.getId());
        if (existTransaction == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        // 先恢复原账户余额
        updateAccountBalance(existTransaction, false);

        // 更新交易记录
        BeanUtils.copyProperties(dto, existTransaction);
        updateById(existTransaction);

        // 更新新账户余额
        updateAccountBalance(existTransaction, true);

        // 更新标签关联
        transactionTagMapper.deleteByTransactionId(dto.getId());
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            saveTransactionTags(dto.getId(), dto.getTagIds());
        }

        return getDetailById(dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransaction(Long id) {
        Transaction existTransaction = getById(id);
        if (existTransaction == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        // 恢复账户余额
        updateAccountBalance(existTransaction, false);

        // 删除标签关联
        transactionTagMapper.deleteByTransactionId(id);

        // 删除交易记录
        removeById(id);
    }

    @Override
    public Long applyCategoryRule(TransactionDTO dto) {
        List<CategoryRule> rules = categoryRuleService.listEnabledRules();

        for (CategoryRule rule : rules) {
            String matchValue = getMatchFieldValue(dto, rule.getMatchField());
            if (matchValue == null || matchValue.isEmpty()) {
                continue;
            }

            boolean matched = false;
            switch (rule.getMatchType()) {
                case "CONTAINS":
                    matched = matchValue.contains(rule.getMatchValue());
                    break;
                case "EQUALS":
                    matched = matchValue.equals(rule.getMatchValue());
                    break;
                case "REGEX":
                    matched = Pattern.matches(rule.getMatchValue(), matchValue);
                    break;
                default:
                    break;
            }

            if (matched) {
                return rule.getTargetCategoryId();
            }
        }
        return null;
    }

    /**
     * 获取匹配字段的值
     */
    private String getMatchFieldValue(TransactionDTO dto, String matchField) {
        switch (matchField) {
            case "DESCRIPTION":
                return dto.getDescription();
            case "MERCHANT":
                return dto.getMerchant();
            case "LOCATION":
                return dto.getLocation();
            default:
                return null;
        }
    }

    /**
     * 更新账户余额
     *
     * @param transaction 交易记录
     * @param isAdd       true-新增/更新时调用，false-删除/更新前调用
     */
    private void updateAccountBalance(Transaction transaction, boolean isAdd) {
        BigDecimal amount = transaction.getAmount();
        if (!isAdd) {
            amount = amount.negate();
        }
        // 收入增加余额，支出减少余额
        if ("EXPENSE".equals(transaction.getTransactionType())) {
            amount = amount.negate();
        }
        accountService.updateBalance(transaction.getAccountId(), amount);
    }

    /**
     * 保存交易标签关联
     */
    private void saveTransactionTags(Long transactionId, List<Long> tagIds) {
        tagIds.forEach(tagId -> {
            TransactionTag tt = new TransactionTag();
            tt.setTransactionId(transactionId);
            tt.setTagId(tagId);
            tt.setCreateTime(LocalDateTime.now());
            transactionTagMapper.insert(tt);
        });
    }
}
