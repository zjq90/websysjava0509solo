package com.hospital.service;

import com.hospital.entity.FeeRecord;
import com.hospital.entity.Patient;
import com.hospital.entity.Staff;
import com.hospital.repository.FeeRecordRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 费用记录Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class FeeRecordService {

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private StaffRepository staffRepository;

    public List<FeeRecord> findAll() {
        List<FeeRecord> list = feeRecordRepository.findAll();
        for (FeeRecord fee : list) {
            fillFeeInfo(fee);
        }
        return list;
    }

    public Optional<FeeRecord> findById(Long id) {
        Optional<FeeRecord> optional = feeRecordRepository.findById(id);
        optional.ifPresent(this::fillFeeInfo);
        return optional;
    }

    public List<FeeRecord> findByHospitalizationId(Long hospitalizationId) {
        List<FeeRecord> list = feeRecordRepository.findByHospitalizationId(hospitalizationId);
        for (FeeRecord fee : list) {
            fillFeeInfo(fee);
        }
        return list;
    }

    public List<FeeRecord> findByPatientId(Long patientId) {
        List<FeeRecord> list = feeRecordRepository.findByPatientId(patientId);
        for (FeeRecord fee : list) {
            fillFeeInfo(fee);
        }
        return list;
    }

    public BigDecimal sumAmountByHospitalizationId(Long hospitalizationId) {
        return feeRecordRepository.sumAmountByHospitalizationId(hospitalizationId);
    }

    public BigDecimal sumPaidAmountByHospitalizationId(Long hospitalizationId) {
        return feeRecordRepository.sumPaidAmountByHospitalizationId(hospitalizationId);
    }

    @Transactional
    public FeeRecord save(FeeRecord feeRecord) {
        if (feeRecord.getId() == null) {
            String feeNo = "F" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            feeRecord.setFeeNo(feeNo);
        }
        if (feeRecord.getPrice() != null && feeRecord.getQuantity() != null) {
            feeRecord.setAmount(feeRecord.getPrice().multiply(new BigDecimal(feeRecord.getQuantity())));
        }
        FeeRecord saved = feeRecordRepository.save(feeRecord);
        fillFeeInfo(saved);
        return saved;
    }

    @Transactional
    public FeeRecord pay(Long id) {
        FeeRecord feeRecord = feeRecordRepository.findById(id).orElse(null);
        if (feeRecord == null) {
            return null;
        }
        feeRecord.setPaymentStatus("已缴费");
        feeRecord.setPaymentTime(LocalDateTime.now());
        FeeRecord saved = feeRecordRepository.save(feeRecord);
        fillFeeInfo(saved);
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        feeRecordRepository.deleteById(id);
    }

    private void fillFeeInfo(FeeRecord fee) {
        if (fee.getPatientId() != null) {
            Patient patient = patientRepository.findById(fee.getPatientId()).orElse(null);
            if (patient != null) {
                fee.setPatientName(patient.getName());
            }
        }
        if (fee.getOperatorId() != null) {
            Staff operator = staffRepository.findById(fee.getOperatorId()).orElse(null);
            if (operator != null) {
                fee.setOperatorName(operator.getName());
            }
        }
    }
}
