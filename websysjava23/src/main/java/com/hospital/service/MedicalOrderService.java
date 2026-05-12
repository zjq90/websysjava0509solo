package com.hospital.service;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 医嘱Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class MedicalOrderService {

    @Autowired
    private MedicalOrderRepository medicalOrderRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    public List<MedicalOrder> findAll() {
        List<MedicalOrder> list = medicalOrderRepository.findAll();
        for (MedicalOrder order : list) {
            fillOrderInfo(order);
        }
        return list;
    }

    public Optional<MedicalOrder> findById(Long id) {
        Optional<MedicalOrder> optional = medicalOrderRepository.findById(id);
        optional.ifPresent(this::fillOrderInfo);
        return optional;
    }

    public List<MedicalOrder> findByHospitalizationId(Long hospitalizationId) {
        List<MedicalOrder> list = medicalOrderRepository.findByHospitalizationId(hospitalizationId);
        for (MedicalOrder order : list) {
            fillOrderInfo(order);
        }
        return list;
    }

    public List<MedicalOrder> findByPatientId(Long patientId) {
        List<MedicalOrder> list = medicalOrderRepository.findByPatientId(patientId);
        for (MedicalOrder order : list) {
            fillOrderInfo(order);
        }
        return list;
    }

    public List<MedicalOrder> findPendingOrdersByHospitalizationId(Long hospitalizationId) {
        List<MedicalOrder> list = medicalOrderRepository.findPendingOrdersByHospitalizationId(hospitalizationId);
        for (MedicalOrder order : list) {
            fillOrderInfo(order);
        }
        return list;
    }

    public List<MedicalOrder> findByStatus(String status) {
        List<MedicalOrder> list = medicalOrderRepository.findByStatus(status);
        for (MedicalOrder order : list) {
            fillOrderInfo(order);
        }
        return list;
    }

    @Transactional
    public MedicalOrder save(MedicalOrder order) {
        if (order.getId() == null) {
            String orderNo = "O" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            order.setOrderNo(orderNo);
        }
        if (order.getPrice() != null && order.getQuantity() != null) {
            order.setTotalAmount(order.getPrice().multiply(new java.math.BigDecimal(order.getQuantity())));
        }
        MedicalOrder saved = medicalOrderRepository.save(order);
        fillOrderInfo(saved);
        return saved;
    }

    @Transactional
    public MedicalOrder executeOrder(Long id, Long nurseId) {
        MedicalOrder order = medicalOrderRepository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }
        order.setStatus("已执行");
        order.setExecuteTime(LocalDateTime.now());
        order.setExecuteNurseId(nurseId);
        MedicalOrder saved = medicalOrderRepository.save(order);

        FeeRecord feeRecord = new FeeRecord();
        feeRecord.setFeeNo("F" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        feeRecord.setHospitalizationId(order.getHospitalizationId());
        feeRecord.setPatientId(order.getPatientId());
        feeRecord.setFeeType(order.getCategory());
        feeRecord.setItemName(order.getName());
        feeRecord.setPrice(order.getPrice());
        feeRecord.setQuantity(order.getQuantity());
        feeRecord.setAmount(order.getTotalAmount());
        feeRecord.setOrderId(order.getId());
        feeRecordRepository.save(feeRecord);

        fillOrderInfo(saved);
        return saved;
    }

    @Transactional
    public MedicalOrder stopOrder(Long id) {
        MedicalOrder order = medicalOrderRepository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }
        order.setStatus("已停止");
        MedicalOrder saved = medicalOrderRepository.save(order);
        fillOrderInfo(saved);
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        medicalOrderRepository.deleteById(id);
    }

    private void fillOrderInfo(MedicalOrder order) {
        if (order.getPatientId() != null) {
            Patient patient = patientRepository.findById(order.getPatientId()).orElse(null);
            if (patient != null) {
                order.setPatientName(patient.getName());
            }
        }
        if (order.getDoctorId() != null) {
            Staff doctor = staffRepository.findById(order.getDoctorId()).orElse(null);
            if (doctor != null) {
                order.setDoctorName(doctor.getName());
            }
        }
        if (order.getExecuteNurseId() != null) {
            Staff nurse = staffRepository.findById(order.getExecuteNurseId()).orElse(null);
            if (nurse != null) {
                order.setExecuteNurseName(nurse.getName());
            }
        }
    }
}
