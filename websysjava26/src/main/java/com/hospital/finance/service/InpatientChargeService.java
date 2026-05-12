package com.hospital.finance.service;

import com.hospital.finance.entity.InpatientCharge;
import com.hospital.finance.entity.InpatientChargeDetail;
import com.hospital.finance.repository.InpatientChargeRepository;
import com.hospital.finance.repository.InpatientChargeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 住院收费服务层
 */
@Service
public class InpatientChargeService {

    @Autowired
    private InpatientChargeRepository chargeRepository;

    @Autowired
    private InpatientChargeDetailRepository detailRepository;

    /**
     * 生成收费单号
     */
    private String generateChargeNo() {
        return "IP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 计算医保报销金额
     */
    private void calculateInsurance(InpatientCharge charge, List<InpatientChargeDetail> details) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal insuranceAmount = BigDecimal.ZERO;
        BigDecimal selfPayAmount = BigDecimal.ZERO;

        for (InpatientChargeDetail detail : details) {
            totalAmount = totalAmount.add(detail.getAmount());
            if (detail.getIsInsurance() != null && detail.getIsInsurance()) {
                Double ratio = detail.getInsuranceRatio() != null ? detail.getInsuranceRatio() : 0.8;
                BigDecimal insurance = detail.getAmount().multiply(BigDecimal.valueOf(ratio));
                detail.setInsuranceAmount(insurance);
                detail.setSelfPayAmount(detail.getAmount().subtract(insurance));
                insuranceAmount = insuranceAmount.add(insurance);
            } else {
                detail.setInsuranceAmount(BigDecimal.ZERO);
                detail.setSelfPayAmount(detail.getAmount());
            }
            selfPayAmount = selfPayAmount.add(detail.getSelfPayAmount());
        }

        charge.setTotalAmount(totalAmount);
        charge.setInsuranceAmount(insuranceAmount);
        charge.setSelfPayAmount(selfPayAmount);
    }

    /**
     * 新增住院收费（记账）
     */
    @Transactional
    public InpatientCharge createCharge(InpatientCharge charge, List<InpatientChargeDetail> details) {
        String chargeNo = generateChargeNo();
        charge.setChargeNo(chargeNo);
        charge.setStatus("住院中");
        charge.setChargeTime(LocalDateTime.now());

        calculateInsurance(charge, details);

        InpatientCharge savedCharge = chargeRepository.save(charge);

        for (InpatientChargeDetail detail : details) {
            detail.setChargeId(savedCharge.getId());
            detail.setChargeNo(chargeNo);
            detailRepository.save(detail);
        }

        return savedCharge;
    }

    /**
     * 添加费用明细
     */
    @Transactional
    public InpatientChargeDetail addDetail(Long chargeId, InpatientChargeDetail detail) {
        Optional<InpatientCharge> chargeOpt = chargeRepository.findById(chargeId);
        if (chargeOpt.isPresent()) {
            InpatientCharge charge = chargeOpt.get();
            detail.setChargeId(chargeId);
            detail.setChargeNo(charge.getChargeNo());
            detail.setChargeDate(LocalDateTime.now());
            return detailRepository.save(detail);
        }
        return null;
    }

    /**
     * 完成出院结算
     */
    @Transactional
    public InpatientCharge settleCharge(Long chargeId, String paymentMethod, String operator) {
        Optional<InpatientCharge> chargeOpt = chargeRepository.findById(chargeId);
        if (chargeOpt.isPresent()) {
            InpatientCharge charge = chargeOpt.get();
            charge.setStatus("已结算");
            charge.setPaymentMethod(paymentMethod);
            charge.setOperator(operator);
            charge.setChargeTime(LocalDateTime.now());
            return chargeRepository.save(charge);
        }
        return null;
    }

    /**
     * 退费
     */
    @Transactional
    public InpatientCharge refundCharge(Long chargeId, String remark) {
        Optional<InpatientCharge> chargeOpt = chargeRepository.findById(chargeId);
        if (chargeOpt.isPresent()) {
            InpatientCharge charge = chargeOpt.get();
            charge.setStatus("已退费");
            charge.setRemark(remark);
            return chargeRepository.save(charge);
        }
        return null;
    }

    /**
     * 根据ID查询
     */
    public Optional<InpatientCharge> findById(Long id) {
        return chargeRepository.findById(id);
    }

    /**
     * 查询所有
     */
    public List<InpatientCharge> findAll() {
        return chargeRepository.findAll();
    }

    /**
     * 根据患者ID查询
     */
    public List<InpatientCharge> findByPatientId(Long patientId) {
        return chargeRepository.findByPatientId(patientId);
    }

    /**
     * 根据状态查询
     */
    public List<InpatientCharge> findByStatus(String status) {
        return chargeRepository.findByStatus(status);
    }

    /**
     * 查询收费明细
     */
    public List<InpatientChargeDetail> findDetailsByChargeId(Long chargeId) {
        return detailRepository.findByChargeId(chargeId);
    }

    /**
     * 查询指定时间范围内的收费记录
     */
    public List<InpatientCharge> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return chargeRepository.findByChargeTimeBetween(startTime, endTime);
    }
}