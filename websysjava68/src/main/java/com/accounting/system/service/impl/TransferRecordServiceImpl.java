package com.accounting.system.service.impl;

import com.accounting.system.common.BusinessException;
import com.accounting.system.common.ResultCode;
import com.accounting.system.dto.TransferRecordDTO;
import com.accounting.system.entity.TransferRecord;
import com.accounting.system.mapper.TransferRecordMapper;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.TransferRecordService;
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

/**
 * 转账记录Service实现类
 */
@Service
public class TransferRecordServiceImpl extends ServiceImpl<TransferRecordMapper, TransferRecord>
        implements TransferRecordService {

    @Autowired
    private AccountService accountService;

    @Override
    public IPage<TransferRecord> pageList(Page<TransferRecord> page) {
        return baseMapper.selectPageWithDetail(page);
    }

    @Override
    public List<TransferRecord> listAll() {
        return baseMapper.selectPageWithDetail(new Page<>(1, Integer.MAX_VALUE)).getRecords();
    }

    @Override
    public TransferRecord getDetailById(Long id) {
        TransferRecord transfer = getById(id);
        if (transfer == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return transfer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransferRecord addTransfer(TransferRecordDTO dto) {
        // 验证转出和转入账户不能相同
        if (dto.getFromAccountId().equals(dto.getToAccountId())) {
            throw new BusinessException("转出账户和转入账户不能相同");
        }

        // 验证账户是否存在
        accountService.getDetailById(dto.getFromAccountId());
        accountService.getDetailById(dto.getToAccountId());

        // 自动填充转账时间
        if (dto.getTransferTime() == null) {
            dto.setTransferTime(LocalDateTime.now());
        }

        TransferRecord transfer = new TransferRecord();
        BeanUtils.copyProperties(dto, transfer);
        if (transfer.getTransferFee() == null) {
            transfer.setTransferFee(BigDecimal.ZERO);
        }
        save(transfer);

        // 更新转出账户余额（减少）
        BigDecimal totalAmount = transfer.getAmount().add(transfer.getTransferFee());
        accountService.updateBalance(transfer.getFromAccountId(), totalAmount.negate());

        // 更新转入账户余额（增加）
        accountService.updateBalance(transfer.getToAccountId(), transfer.getAmount());

        return getById(transfer.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransfer(Long id) {
        TransferRecord existTransfer = getById(id);
        if (existTransfer == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        // 恢复转出账户余额（增加）
        BigDecimal totalAmount = existTransfer.getAmount().add(existTransfer.getTransferFee());
        accountService.updateBalance(existTransfer.getFromAccountId(), totalAmount);

        // 恢复转入账户余额（减少）
        accountService.updateBalance(existTransfer.getToAccountId(), existTransfer.getAmount().negate());

        removeById(id);
    }
}
