package com.vehicle.service;

import com.vehicle.dto.EnforcementRecordDTO;
import com.vehicle.entity.EnforcementRecord;
import com.vehicle.repository.EnforcementRecordRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnforcementRecordService {

    private final EnforcementRecordRepository recordRepository;

    public EnforcementRecordService(EnforcementRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public EnforcementRecord createRecord(EnforcementRecordDTO dto) {
        EnforcementRecord record = new EnforcementRecord();
        BeanUtils.copyProperties(dto, record);
        if (dto.getOfflineCreated() != null && dto.getOfflineCreated()) {
            record.setSynced(false);
        }
        return recordRepository.save(record);
    }

    public List<EnforcementRecord> getRecordsByPlateNumber(String plateNumber) {
        return recordRepository.findByPlateNumberOrderByEnforcementTimeDesc(plateNumber);
    }

    public List<EnforcementRecord> getRecordsByOfficer(String officerName) {
        return recordRepository.findByOfficerNameOrderByEnforcementTimeDesc(officerName);
    }

    public List<EnforcementRecord> getUnsyncedRecords() {
        return recordRepository.findBySyncedFalse();
    }

    public EnforcementRecord syncRecord(Long id) {
        Optional<EnforcementRecord> opt = recordRepository.findById(id);
        if (opt.isPresent()) {
            EnforcementRecord record = opt.get();
            record.setSynced(true);
            return recordRepository.save(record);
        }
        return null;
    }

    public Optional<EnforcementRecord> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public List<EnforcementRecord> getAllRecords() {
        return recordRepository.findAll();
    }
}
