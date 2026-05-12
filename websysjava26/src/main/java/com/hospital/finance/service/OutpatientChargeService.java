package com.hospital.finance.service;

import com.hospital.finance.entity.OutpatientCharge;
import com.hospital.finance.entity.OutpatientChargeDetail;
import com.hospital.finance.repository.OutpatientChargeRepository;
import com.hospital.finance.repository.OutpatientChargeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 门诊收费服务层
 */
@Service
public class OutpatientChargeService {

    @Autowired
    private OutpatientChargeRepository chargeRepository;

    @Autowired
    private OutpatientChargeDetailRepository detailRepository;

    /**
     * 生成收费单号
     */
    private String generateChargeNo() {
        return "OP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 计算医保报销金额
     */
    private void calculateInsurance(OutpatientCharge charge, List<OutpatientChargeDetail> details) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal insuranceAmount = BigDecimal.ZERO;
        BigDecimal selfPayAmount = BigDecimal.ZERO;

        for (OutpatientChargeDetail detail : details) {
            totalAmount = totalAmount.add(detail.getAmount());
            if (detail.getIsInsurance() != null && detail.getIsInsurance()) {
                Double ratio = detail.getInsuranceRatio() != null ? detail.getInsuranceRatio() : 0.7;
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
     * 新增门诊收费（记账）
     */
    @Transactional
    public OutpatientCharge createCharge(OutpatientCharge charge, List<OutpatientChargeDetail> details) {
        String chargeNo = generateChargeNo();
        charge.setChargeNo(chargeNo);
        charge.setStatus("待缴费");
        charge.setChargeTime(LocalDateTime.now());

        calculateInsurance(charge, details);

        OutpatientCharge savedCharge = chargeRepository.save(charge);

        for (OutpatientChargeDetail detail : details) {
            detail.setChargeId(savedCharge.getId());
            detail.setChargeNo(chargeNo);
            detailRepository.save(detail);
        }

        return savedCharge;
    }

    /**
     * 完成收费结算
     */
    @Transactional
    public OutpatientCharge settleCharge(Long chargeId, String paymentMethod, String operator) {
        Optional<OutpatientCharge> chargeOpt = chargeRepository.findById(chargeId);
        if (chargeOpt.isPresent()) {
            OutpatientCharge charge = chargeOpt.get();
            charge.setStatus("已缴费");
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
    public OutpatientCharge refundCharge(Long chargeId, String remark) {
        Optional<OutpatientCharge> chargeOpt = chargeRepository.findById(chargeId);
        if (chargeOpt.isPresent()) {
            OutpatientCharge charge = chargeOpt.get();
            charge.setStatus("已退费");
            charge.setRemark(remark);
            return chargeRepository.save(charge);
        }
        return null;
    }

    /**
     * 根据ID查询
     */
    public Optional<OutpatientCharge> findById(Long id) {
        return chargeRepository.findById(id);
    }

    /**
     * 查询所有
     */
    public List<OutpatientCharge> findAll() {
        return chargeRepository.findAll();
    }

    /**
     * 根据患者ID查询
     */
    public List<OutpatientCharge> findByPatientId(Long patientId) {
        return chargeRepository.findByPatientId(patientId);
    }

    /**
     * 根据状态查询
     */
    public List<OutpatientCharge> findByStatus(String status) {
        return chargeRepository.findByStatus(status);
    }

    /**
     * 查询收费明细
     */
    public List<OutpatientChargeDetail> findDetailsByChargeId(Long chargeId) {
        return detailRepository.findByChargeId(chargeId);
    }

    /**
     * 查询指定时间范围内的收费记录
     */
    public List<OutpatientCharge> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return chargeRepository.findByChargeTimeBetween(startTime, endTime);
    }
}