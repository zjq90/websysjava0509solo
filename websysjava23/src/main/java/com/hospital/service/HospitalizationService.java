package com.hospital.service;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 住院记录Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class HospitalizationService {

    @Autowired
    private HospitalizationRepository hospitalizationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    public List<Hospitalization> findAll() {
        List<Hospitalization> list = hospitalizationRepository.findAll();
        for (Hospitalization h : list) {
            fillHospitalizationInfo(h);
        }
        return list;
    }

    public Optional<Hospitalization> findById(Long id) {
        Optional<Hospitalization> optional = hospitalizationRepository.findById(id);
        optional.ifPresent(this::fillHospitalizationInfo);
        return optional;
    }

    public Hospitalization findByHospitalNo(String hospitalNo) {
        Hospitalization h = hospitalizationRepository.findByHospitalNo(hospitalNo);
        if (h != null) {
            fillHospitalizationInfo(h);
        }
        return h;
    }

    public List<Hospitalization> findByPatientId(Long patientId) {
        List<Hospitalization> list = hospitalizationRepository.findByPatientId(patientId);
        for (Hospitalization h : list) {
            fillHospitalizationInfo(h);
        }
        return list;
    }

    public List<Hospitalization> findByStatus(String status) {
        List<Hospitalization> list = hospitalizationRepository.findByStatus(status);
        for (Hospitalization h : list) {
            fillHospitalizationInfo(h);
        }
        return list;
    }

    public List<Hospitalization> findAllHospitalized() {
        List<Hospitalization> list = hospitalizationRepository.findAllHospitalized();
        for (Hospitalization h : list) {
            fillHospitalizationInfo(h);
        }
        return list;
    }

    @Transactional
    public Hospitalization admission(Hospitalization hospitalization) {
        if (hospitalization.getBedId() != null) {
            Bed bed = bedRepository.findById(hospitalization.getBedId()).orElse(null);
            if (bed != null && !"空闲".equals(bed.getStatus())) {
                throw new RuntimeException("床位已被占用，请选择其他床位");
            }
        }

        if (hospitalization.getPatientId() != null) {
            List<Hospitalization> existingHospitalizations = hospitalizationRepository.findByPatientId(hospitalization.getPatientId());
            for (Hospitalization h : existingHospitalizations) {
                if ("住院中".equals(h.getStatus())) {
                    throw new RuntimeException("该患者已在住院中");
                }
            }
        }

        String hospitalNo = "H" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        hospitalization.setHospitalNo(hospitalNo);
        hospitalization.setAdmissionDate(LocalDateTime.now());
        hospitalization.setStatus("住院中");

        if (hospitalization.getBedId() != null) {
            Bed bed = bedRepository.findById(hospitalization.getBedId()).orElse(null);
            if (bed != null) {
                bed.setStatus("占用");
                bed.setPatientId(hospitalization.getPatientId());
                bedRepository.save(bed);
            }
        }

        Patient patient = patientRepository.findById(hospitalization.getPatientId()).orElse(null);
        if (patient != null) {
            patient.setStatus("住院中");
            patientRepository.save(patient);
        }

        Hospitalization saved = hospitalizationRepository.save(hospitalization);
        fillHospitalizationInfo(saved);
        return saved;
    }

    @Transactional
    public Hospitalization discharge(Long id, String dischargeDiagnosis) {
        Hospitalization hospitalization = hospitalizationRepository.findById(id).orElse(null);
        if (hospitalization == null) {
            return null;
        }

        hospitalization.setDischargeDate(LocalDateTime.now());
        hospitalization.setDischargeDiagnosis(dischargeDiagnosis);
        hospitalization.setStatus("已出院");

        BigDecimal totalAmount = feeRecordRepository.sumAmountByHospitalizationId(id);
        hospitalization.setTotalAmount(totalAmount);

        if (hospitalization.getBedId() != null) {
            Bed bed = bedRepository.findById(hospitalization.getBedId()).orElse(null);
            if (bed != null) {
                bed.setStatus("空闲");
                bed.setPatientId(null);
                bedRepository.save(bed);
            }
        }

        Patient patient = patientRepository.findById(hospitalization.getPatientId()).orElse(null);
        if (patient != null) {
            patient.setStatus("正常");
            patientRepository.save(patient);
        }

        Hospitalization saved = hospitalizationRepository.save(hospitalization);
        fillHospitalizationInfo(saved);
        return saved;
    }

    @Transactional
    public Hospitalization save(Hospitalization hospitalization) {
        return hospitalizationRepository.save(hospitalization);
    }

    @Transactional
    public void deleteById(Long id) {
        hospitalizationRepository.deleteById(id);
    }

    private void fillHospitalizationInfo(Hospitalization h) {
        if (h.getPatientId() != null) {
            Patient patient = patientRepository.findById(h.getPatientId()).orElse(null);
            if (patient != null) {
                h.setPatientName(patient.getName());
            }
        }
        if (h.getBedId() != null) {
            Bed bed = bedRepository.findById(h.getBedId()).orElse(null);
            if (bed != null) {
                h.setBedNo(bed.getBedNo());
                h.setWardName(bed.getWardName());
            }
        }
        if (h.getDoctorId() != null) {
            Staff doctor = staffRepository.findById(h.getDoctorId()).orElse(null);
            if (doctor != null) {
                h.setDoctorName(doctor.getName());
            }
        }
        if (h.getNurseId() != null) {
            Staff nurse = staffRepository.findById(h.getNurseId()).orElse(null);
            if (nurse != null) {
                h.setNurseName(nurse.getName());
            }
        }
    }
}
