package com.accounting.system.service.impl;

import com.accounting.system.common.BusinessException;
import com.accounting.system.common.ResultCode;
import com.accounting.system.dto.AccountDTO;
import com.accounting.system.entity.Account;
import com.accounting.system.mapper.AccountMapper;
import com.accounting.system.service.AccountService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户Service实现类
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
        implements AccountService {

    @Override
    public List<Account> listAll() {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Account::getSortOrder);
        return list(wrapper);
    }

    @Override
    public Account getDetailById(Long id) {
        Account account = getById(id);
        if (account == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Account addAccount(AccountDTO dto) {
        Account account = new Account();
        BeanUtils.copyProperties(dto, account);
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        if (account.getCurrency() == null) {
            account.setCurrency("CNY");
        }
        save(account);
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Account updateAccount(AccountDTO dto) {
        Account existAccount = getById(dto.getId());
        if (existAccount == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        BeanUtils.copyProperties(dto, existAccount);
        updateById(existAccount);
        return existAccount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAccount(Long id) {
        Account existAccount = getById(id);
        if (existAccount == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBalance(Long accountId, BigDecimal amount) {
        Account account = getById(accountId);
        if (account == null) {
            throw new BusinessException("账户不存在");
        }
        baseMapper.updateBalance(accountId, amount);
    }
}
