package com.accounting.system.service;

import com.accounting.system.dto.TransferRecordDTO;
import com.accounting.system.entity.TransferRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 转账记录Service接口
 */
public interface TransferRecordService extends IService<TransferRecord> {

    /**
     * 分页查询转账记录
     *
     * @param page 分页参数
     * @return 转账记录分页列表
     */
    IPage<TransferRecord> pageList(Page<TransferRecord> page);

    /**
     * 查询所有转账记录
     *
     * @return 转账记录列表
     */
    List<TransferRecord> listAll();

    /**
     * 根据ID获取转账记录详情
     *
     * @param id 转账记录ID
     * @return 转账记录详情
     */
    TransferRecord getDetailById(Long id);

    /**
     * 新增转账记录（同时更新转出和转入账户余额）
     *
     * @param dto 转账记录请求DTO
     * @return 新增的转账记录
     */
    TransferRecord addTransfer(TransferRecordDTO dto);

    /**
     * 删除转账记录（同时恢复转出和转入账户余额）
     *
     * @param id 转账记录ID
     */
    void deleteTransfer(Long id);
}
