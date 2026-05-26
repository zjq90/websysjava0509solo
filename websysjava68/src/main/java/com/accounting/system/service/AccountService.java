package com.accounting.system.service;

import com.accounting.system.dto.AccountDTO;
import com.accounting.system.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户Service接口
 */
public interface AccountService extends IService<Account> {

    /**
     * 查询所有账户列表
     *
     * @return 账户列表
     */
    List<Account> listAll();

    /**
     * 根据ID获取账户详情
     *
     * @param id 账户ID
     * @return 账户详情
     */
    Account getDetailById(Long id);

    /**
     * 新增账户
     *
     * @param dto 账户请求DTO
     * @return 新增的账户
     */
    Account addAccount(AccountDTO dto);

    /**
     * 更新账户
     *
     * @param dto 账户请求DTO
     * @return 更新后的账户
     */
    Account updateAccount(AccountDTO dto);

    /**
     * 删除账户
     *
     * @param id 账户ID
     */
    void deleteAccount(Long id);

    /**
     * 更新账户余额
     *
     * @param accountId 账户ID
     * @param amount    变动金额（正数增加，负数减少）
     */
    void updateBalance(Long accountId, BigDecimal amount);
}
