package com.psyconsult.service;

import com.psyconsult.entity.EmergencyContact;
import com.psyconsult.repository.EmergencyContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyService {

    private final EmergencyContactRepository emergencyContactRepository;

    public EmergencyService(EmergencyContactRepository emergencyContactRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
    }

    public List<EmergencyContact> getAllEmergencyContacts() {
        return emergencyContactRepository.findByEnabledTrueOrderBySortOrderAsc();
    }

    public List<EmergencyContact> getHotlines() {
        return emergencyContactRepository.findByIsHotlineTrueAndEnabledTrueOrderBySortOrderAsc();
    }

    public EmergencyContact getEmergencyContactById(Long id) {
        return emergencyContactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("紧急联系人不存在"));
    }
}
