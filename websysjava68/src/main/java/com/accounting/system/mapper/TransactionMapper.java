package com.accounting.system.mapper;

import com.accounting.system.dto.TransactionQueryDTO;
import com.accounting.system.entity.Transaction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易记录Mapper接口
 */
@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

    /**
     * 分页查询交易记录（带账户、分类、标签信息）
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 交易记录列表
     */
    IPage<Transaction> selectPageWithDetail(Page<Transaction> page, @Param("query") TransactionQueryDTO query);

    /**
     * 根据ID查询交易记录详情（带账户、分类、标签信息）
     *
     * @param id 交易记录ID
     * @return 交易记录详情
     */
    Transaction selectDetailById(@Param("id") Long id);

    /**
     * 查询所有交易记录（带账户、分类、标签信息）
     *
     * @param query 查询条件
     * @return 交易记录列表
     */
    List<Transaction> selectAllWithDetail(@Param("query") TransactionQueryDTO query);
}
