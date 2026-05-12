package com.hospital.service;

import com.hospital.entity.Patient;
import com.hospital.entity.Staff;
import com.hospital.entity.VitalSigns;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.StaffRepository;
import com.hospital.repository.VitalSignsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 生命体征Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class VitalSignsService {

    @Autowired
    private VitalSignsRepository vitalSignsRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private StaffRepository staffRepository;

    public List<VitalSigns> findAll() {
        List<VitalSigns> list = vitalSignsRepository.findAll();
        for (VitalSigns vs : list) {
            fillVitalSignsInfo(vs);
        }
        return list;
    }

    public Optional<VitalSigns> findById(Long id) {
        Optional<VitalSigns> optional = vitalSignsRepository.findById(id);
        optional.ifPresent(this::fillVitalSignsInfo);
        return optional;
    }

    public List<VitalSigns> findByHospitalizationId(Long hospitalizationId) {
        List<VitalSigns> list = vitalSignsRepository.findByHospitalizationIdOrderByRecordTimeDesc(hospitalizationId);
        for (VitalSigns vs : list) {
            fillVitalSignsInfo(vs);
        }
        return list;
    }

    public List<VitalSigns> findByPatientId(Long patientId) {
        List<VitalSigns> list = vitalSignsRepository.findByPatientIdOrderByRecordTimeDesc(patientId);
        for (VitalSigns vs : list) {
            fillVitalSignsInfo(vs);
        }
        return list;
    }

    @Transactional
    public VitalSigns save(VitalSigns vitalSigns) {
        VitalSigns saved = vitalSignsRepository.save(vitalSigns);
        fillVitalSignsInfo(saved);
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        vitalSignsRepository.deleteById(id);
    }

    private void fillVitalSignsInfo(VitalSigns vs) {
        if (vs.getPatientId() != null) {
            Patient patient = patientRepository.findById(vs.getPatientId()).orElse(null);
            if (patient != null) {
                vs.setPatientName(patient.getName());
            }
        }
        if (vs.getNurseId() != null) {
            Staff nurse = staffRepository.findById(vs.getNurseId()).orElse(null);
            if (nurse != null) {
                vs.setNurseName(nurse.getName());
            }
        }
    }
}
