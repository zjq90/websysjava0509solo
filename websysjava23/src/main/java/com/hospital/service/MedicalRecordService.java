package com.hospital.service;

import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.entity.Staff;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 病历Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private StaffRepository staffRepository;

    public List<MedicalRecord> findAll() {
        List<MedicalRecord> list = medicalRecordRepository.findAll();
        for (MedicalRecord record : list) {
            fillRecordInfo(record);
        }
        return list;
    }

    public Optional<MedicalRecord> findById(Long id) {
        Optional<MedicalRecord> optional = medicalRecordRepository.findById(id);
        optional.ifPresent(this::fillRecordInfo);
        return optional;
    }

    public List<MedicalRecord> findByHospitalizationId(Long hospitalizationId) {
        List<MedicalRecord> list = medicalRecordRepository.findByHospitalizationId(hospitalizationId);
        for (MedicalRecord record : list) {
            fillRecordInfo(record);
        }
        return list;
    }

    public List<MedicalRecord> findByPatientId(Long patientId) {
        List<MedicalRecord> list = medicalRecordRepository.findByPatientId(patientId);
        for (MedicalRecord record : list) {
            fillRecordInfo(record);
        }
        return list;
    }

    public List<MedicalRecord> findByHospitalizationIdAndRecordType(Long hospitalizationId, String recordType) {
        List<MedicalRecord> list = medicalRecordRepository.findByHospitalizationIdAndRecordType(hospitalizationId, recordType);
        for (MedicalRecord record : list) {
            fillRecordInfo(record);
        }
        return list;
    }

    @Transactional
    public MedicalRecord save(MedicalRecord record) {
        if (record.getId() == null) {
            String recordNo = "R" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            record.setRecordNo(recordNo);
        }
        MedicalRecord saved = medicalRecordRepository.save(record);
        fillRecordInfo(saved);
        return saved;
    }

    @Transactional
    public MedicalRecord submit(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id).orElse(null);
        if (record == null) {
            return null;
        }
        record.setStatus("已提交");
        MedicalRecord saved = medicalRecordRepository.save(record);
        fillRecordInfo(saved);
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        medicalRecordRepository.deleteById(id);
    }

    private void fillRecordInfo(MedicalRecord record) {
        if (record.getPatientId() != null) {
            Patient patient = patientRepository.findById(record.getPatientId()).orElse(null);
            if (patient != null) {
                record.setPatientName(patient.getName());
            }
        }
        if (record.getDoctorId() != null) {
            Staff doctor = staffRepository.findById(record.getDoctorId()).orElse(null);
            if (doctor != null) {
                record.setDoctorName(doctor.getName());
            }
        }
    }
}
