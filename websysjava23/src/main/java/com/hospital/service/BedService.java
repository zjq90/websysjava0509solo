package com.hospital.service;

import com.hospital.entity.Bed;
import com.hospital.entity.Patient;
import com.hospital.repository.BedRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 床位Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class BedService {

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private PatientRepository patientRepository;

    public List<Bed> findAll() {
        List<Bed> list = bedRepository.findAll();
        for (Bed bed : list) {
            fillBedInfo(bed);
        }
        return list;
    }

    public Optional<Bed> findById(Long id) {
        Optional<Bed> optional = bedRepository.findById(id);
        optional.ifPresent(this::fillBedInfo);
        return optional;
    }

    public Bed findByBedNo(String bedNo) {
        Bed bed = bedRepository.findByBedNo(bedNo);
        if (bed != null) {
            fillBedInfo(bed);
        }
        return bed;
    }

    public List<Bed> findByStatus(String status) {
        List<Bed> list = bedRepository.findByStatus(status);
        for (Bed bed : list) {
            fillBedInfo(bed);
        }
        return list;
    }

    public List<Bed> findAllAvailable() {
        List<Bed> list = bedRepository.findAllAvailable();
        for (Bed bed : list) {
            fillBedInfo(bed);
        }
        return list;
    }

    public List<Bed> findByWardName(String wardName) {
        List<Bed> list = bedRepository.findByWardName(wardName);
        for (Bed bed : list) {
            fillBedInfo(bed);
        }
        return list;
    }

    public long countByStatus(String status) {
        return bedRepository.countByStatus(status);
    }

    @Transactional
    public Bed save(Bed bed) {
        return bedRepository.save(bed);
    }

    @Transactional
    public void deleteById(Long id) {
        bedRepository.deleteById(id);
    }

    private void fillBedInfo(Bed bed) {
        if (bed.getPatientId() != null) {
            Patient patient = patientRepository.findById(bed.getPatientId()).orElse(null);
            if (patient != null) {
                bed.setPatientName(patient.getName());
            }
        }
    }
}
