package com.hospital.finance.service;

import com.hospital.finance.entity.InsuranceSettlement;
import com.hospital.finance.repository.InsuranceSettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 医保结算服务层
 */
@Service
public class InsuranceService {

    @Autowired
    private InsuranceSettlementRepository settlementRepository;

    /**
     * 生成结算单号
     */
    private String generateSettlementNo() {
        return "INS" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 创建医保结算
     */
    @Transactional
    public InsuranceSettlement createSettlement(InsuranceSettlement settlement) {
        settlement.setSettlementNo(generateSettlementNo());
        settlement.setStatus("待结算");
        settlement.setSettlementTime(LocalDateTime.now());
        return settlementRepository.save(settlement);
    }

    /**
     * 完成医保结算
     */
    @Transactional
    public InsuranceSettlement completeSettlement(Long settlementId, String operator, String insuranceResponse) {
        Optional<InsuranceSettlement> opt = settlementRepository.findById(settlementId);
        if (opt.isPresent()) {
            InsuranceSettlement settlement = opt.get();
            settlement.setStatus("已结算");
            settlement.setOperator(operator);
            settlement.setInsuranceResponse(insuranceResponse);
            settlement.setSettlementTime(LocalDateTime.now());
            return settlementRepository.save(settlement);
        }
        return null;
    }

    /**
     * 撤销结算
     */
    @Transactional
    public InsuranceSettlement cancelSettlement(Long settlementId, String remark) {
        Optional<InsuranceSettlement> opt = settlementRepository.findById(settlementId);
        if (opt.isPresent()) {
            InsuranceSettlement settlement = opt.get();
            settlement.setStatus("已撤销");
            settlement.setRemark(remark);
            return settlementRepository.save(settlement);
        }
        return null;
    }

    /**
     * 根据ID查询
     */
    public Optional<InsuranceSettlement> findById(Long id) {
        return settlementRepository.findById(id);
    }

    /**
     * 查询所有
     */
    public List<InsuranceSettlement> findAll() {
        return settlementRepository.findAll();
    }

    /**
     * 根据患者ID查询
     */
    public List<InsuranceSettlement> findByPatientId(Long patientId) {
        return settlementRepository.findByPatientId(patientId);
    }

    /**
     * 根据状态查询
     */
    public List<InsuranceSettlement> findByStatus(String status) {
        return settlementRepository.findByStatus(status);
    }
}