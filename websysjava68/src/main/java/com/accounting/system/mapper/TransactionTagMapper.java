package com.accounting.system.mapper;

import com.accounting.system.entity.TransactionTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 交易标签关联Mapper接口
 */
@Mapper
public interface TransactionTagMapper extends BaseMapper<TransactionTag> {

    /**
     * 根据交易记录ID删除所有关联的标签
     *
     * @param transactionId 交易记录ID
     */
    @Delete("DELETE FROM transaction_tag WHERE transaction_id = #{transactionId}")
    int deleteByTransactionId(@Param("transactionId") Long transactionId);
}
