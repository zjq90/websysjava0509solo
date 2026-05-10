package com.platform.management.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.management.dto.ReconciliationDTO;
import com.platform.management.entity.PaymentRecord;
import com.platform.management.entity.Reconciliation;
import com.platform.management.entity.ReconciliationDetail;
import com.platform.management.mapper.PaymentRecordMapper;
import com.platform.management.mapper.ReconciliationDetailMapper;
import com.platform.management.mapper.ReconciliationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对账管理Service
 * 
 * @author platform
 * @version 1.0.0
 */
@Service
public class ReconciliationService extends ServiceImpl<ReconciliationMapper, Reconciliation> {

    @Autowired
    private PaymentRecordMapper paymentRecordMapper;

    @Autowired
    private ReconciliationDetailMapper reconciliationDetailMapper;

    /**
     * 分页查询对账记录列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param reconDate 对账日期（可选）
     * @param payChannel 支付渠道（可选）
     * @param reconType 对账类型（可选）
     * @param status 对账状态（可选）
     * @return 分页结果
     */
    public Page<Reconciliation> getPage(Integer pageNum, Integer pageSize, LocalDate reconDate,
                                        String payChannel, String reconType, String status) {
        Page<Reconciliation> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Reconciliation> wrapper = new LambdaQueryWrapper<>();
        
        if (reconDate != null) {
            wrapper.eq(Reconciliation::getReconDate, reconDate);
        }
        if (StringUtils.hasText(payChannel)) {
            wrapper.eq(Reconciliation::getPayChannel, payChannel);
        }
        if (StringUtils.hasText(reconType)) {
            wrapper.eq(Reconciliation::getReconType, reconType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Reconciliation::getStatus, status);
        }
        wrapper.orderByDesc(Reconciliation::getReconTime);
        
        return this.page(page, wrapper);
    }

    /**
     * 执行对账
     * 
     * @param dto 对账参数
     * @return 对账结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Reconciliation executeReconciliation(ReconciliationDTO dto) {
        LocalDate reconDate = dto.getReconDate();
        String payChannel = dto.getPayChannel();
        String reconType = dto.getReconType();
        
        LambdaQueryWrapper<PaymentRecord> paymentWrapper = new LambdaQueryWrapper<>();
        paymentWrapper.eq(PaymentRecord::getPayChannel, payChannel)
                     .eq(PaymentRecord::getPayStatus, "SUCCESS");
        
        if ("DAILY".equals(reconType)) {
            LocalDateTime startOfDay = reconDate.atStartOfDay();
            LocalDateTime endOfDay = reconDate.plusDays(1).atStartOfDay();
            paymentWrapper.between(PaymentRecord::getPayTime, startOfDay, endOfDay);
        } else if ("MONTHLY".equals(reconType)) {
            LocalDate firstDayOfMonth = reconDate.withDayOfMonth(1);
            LocalDate lastDayOfMonth = reconDate.withDayOfMonth(reconDate.lengthOfMonth());
            LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
            LocalDateTime endOfMonth = lastDayOfMonth.plusDays(1).atStartOfDay();
            paymentWrapper.between(PaymentRecord::getPayTime, startOfMonth, endOfMonth);
        }
        
        List<PaymentRecord> paymentRecords = paymentRecordMapper.selectList(paymentWrapper);
        
        BigDecimal systemAmount = paymentRecords.stream()
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int systemCount = paymentRecords.size();
        
        Map<String, List<PaymentRecord>> orderPaymentMap = paymentRecords.stream()
                .collect(Collectors.groupingBy(PaymentRecord::getOrderNo));
        
        BigDecimal thirdAmount = systemAmount;
        int thirdCount = systemCount;
        List<ReconciliationDetail> diffDetails = new ArrayList<>();
        
        for (Map.Entry<String, List<PaymentRecord>> entry : orderPaymentMap.entrySet()) {
            String orderNo = entry.getKey();
            List<PaymentRecord> records = entry.getValue();
            if (records.size() > 1) {
                ReconciliationDetail detail = new ReconciliationDetail();
                detail.setOrderNo(orderNo);
                detail.setDiffType("DUPLICATE_PAY");
                detail.setSystemAmount(records.get(0).getAmount());
                detail.setThirdAmount(records.stream()
                        .map(PaymentRecord::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                detail.setStatus("PENDING");
                detail.setRemark("该订单存在" + records.size() + "条支付记录");
                diffDetails.add(detail);
                
                BigDecimal extraAmount = records.stream()
                        .skip(1)
                        .map(PaymentRecord::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                thirdAmount = thirdAmount.add(extraAmount);
                thirdCount = thirdCount + (records.size() - 1);
            }
        }
        
        Reconciliation reconciliation = new Reconciliation();
        reconciliation.setReconDate(reconDate);
        reconciliation.setPayChannel(payChannel);
        reconciliation.setReconType(reconType);
        reconciliation.setSystemAmount(systemAmount);
        reconciliation.setThirdAmount(thirdAmount);
        reconciliation.setDiffAmount(thirdAmount.subtract(systemAmount));
        reconciliation.setSystemCount(systemCount);
        reconciliation.setThirdCount(thirdCount);
        reconciliation.setDiffCount(diffDetails.size());
        reconciliation.setStatus(diffDetails.isEmpty() ? "MATCHED" : "UNMATCHED");
        reconciliation.setRemark(diffDetails.isEmpty() ? "对账一致" : "存在" + diffDetails.size() + "条差异记录");
        reconciliation.setReconTime(LocalDateTime.now());
        
        this.save(reconciliation);
        
        for (ReconciliationDetail detail : diffDetails) {
            detail.setReconId(reconciliation.getId());
            reconciliationDetailMapper.insert(detail);
        }
        
        return reconciliation;
    }

    /**
     * 获取对账差异明细
     * 
     * @param reconId 对账记录ID
     * @return 差异明细列表
     */
    public List<ReconciliationDetail> getReconciliationDetails(Long reconId) {
        LambdaQueryWrapper<ReconciliationDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReconciliationDetail::getReconId, reconId);
        return reconciliationDetailMapper.selectList(wrapper);
    }

    /**
     * 删除对账记录
     * 
     * @param id 对账记录ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReconciliation(Long id) {
        LambdaQueryWrapper<ReconciliationDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(ReconciliationDetail::getReconId, id);
        reconciliationDetailMapper.delete(detailWrapper);
        return this.removeById(id);
    }

    /**
     * 批量删除对账记录
     * 
     * @param ids ID列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteReconciliation(id);
        }
        return true;
    }
}
