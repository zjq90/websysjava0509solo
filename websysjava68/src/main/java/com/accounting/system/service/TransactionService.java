package com.accounting.system.service;

import com.accounting.system.dto.TransactionDTO;
import com.accounting.system.dto.TransactionQueryDTO;
import com.accounting.system.entity.Transaction;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 交易记录Service接口
 */
public interface TransactionService extends IService<Transaction> {

    /**
     * 分页查询交易记录
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 交易记录分页列表
     */
    IPage<Transaction> pageList(Page<Transaction> page, TransactionQueryDTO query);

    /**
     * 查询所有交易记录
     *
     * @param query 查询条件
     * @return 交易记录列表
     */
    List<Transaction> listAll(TransactionQueryDTO query);

    /**
     * 根据ID获取交易记录详情
     *
     * @param id 交易记录ID
     * @return 交易记录详情
     */
    Transaction getDetailById(Long id);

    /**
     * 新增交易记录
     *
     * @param dto 交易记录请求DTO
     * @return 新增的交易记录
     */
    Transaction addTransaction(TransactionDTO dto);

    /**
     * 更新交易记录
     *
     * @param dto 交易记录请求DTO
     * @return 更新后的交易记录
     */
    Transaction updateTransaction(TransactionDTO dto);

    /**
     * 删除交易记录
     *
     * @param id 交易记录ID
     */
    void deleteTransaction(Long id);

    /**
     * 应用分类规则自动匹配分类
     *
     * @param dto 交易记录请求DTO
     * @return 匹配到的分类ID（如果没有匹配规则返回null）
     */
    Long applyCategoryRule(TransactionDTO dto);
}
