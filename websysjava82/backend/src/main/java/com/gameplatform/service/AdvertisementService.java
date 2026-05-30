package com.gameplatform.service;

import com.gameplatform.entity.Advertisement;
import com.gameplatform.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementService {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }

    public List<Advertisement> findByAdSlotId(Long adSlotId) {
        return advertisementRepository.findByAdSlotId(adSlotId);
    }

    public List<Advertisement> findActiveByAdSlotId(Long adSlotId) {
        return advertisementRepository.findByAdSlotIdAndActive(adSlotId, true);
    }

    public List<Advertisement> findAllActive() {
        return advertisementRepository.findByActive(true);
    }

    public Optional<Advertisement> findById(Long id) {
        return advertisementRepository.findById(id);
    }

    public Advertisement save(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    public void deleteById(Long id) {
        advertisementRepository.deleteById(id);
    }

    public Advertisement update(Long id, Advertisement advertisement) {
        advertisement.setId(id);
        return advertisementRepository.save(advertisement);
    }

    public Advertisement toggleActive(Long id) {
        Optional<Advertisement> opt = advertisementRepository.findById(id);
        if (opt.isPresent()) {
            Advertisement ad = opt.get();
            ad.setActive(!ad.getActive());
            return advertisementRepository.save(ad);
        }
        return null;
    }
}
