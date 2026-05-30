package com.gameplatform.service;

import com.gameplatform.entity.AdSlot;
import com.gameplatform.repository.AdSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdSlotService {
    @Autowired
    private AdSlotRepository adSlotRepository;

    public List<AdSlot> findAll() {
        return adSlotRepository.findAll();
    }

    public List<AdSlot> findAllEnabled() {
        return adSlotRepository.findByEnabled(true);
    }

    public Optional<AdSlot> findById(Long id) {
        return adSlotRepository.findById(id);
    }

    public Optional<AdSlot> findByCode(String code) {
        return adSlotRepository.findByCode(code);
    }

    public AdSlot save(AdSlot adSlot) {
        return adSlotRepository.save(adSlot);
    }

    public void deleteById(Long id) {
        adSlotRepository.deleteById(id);
    }

    public AdSlot update(Long id, AdSlot adSlot) {
        adSlot.setId(id);
        return adSlotRepository.save(adSlot);
    }
}
