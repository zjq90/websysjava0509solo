package com.accounting.system.mapper;

import com.accounting.system.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 账户Mapper接口
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    /**
     * 更新账户余额
     *
     * @param accountId 账户ID
     * @param amount    变动金额（正数增加，负数减少）
     */
    @Update("UPDATE account SET balance = balance + #{amount} WHERE id = #{accountId} AND deleted = 0")
    int updateBalance(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
}
