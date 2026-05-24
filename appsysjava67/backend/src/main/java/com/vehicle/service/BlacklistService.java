package com.vehicle.service;

import com.vehicle.entity.Blacklist;
import com.vehicle.repository.BlacklistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;

    public BlacklistService(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    public List<Blacklist> getAllBlacklist() {
        return blacklistRepository.findAll();
    }

    public Blacklist addBlacklist(Blacklist blacklist) {
        return blacklistRepository.save(blacklist);
    }

    public Optional<Blacklist> findByPlateNumber(String plateNumber) {
        return blacklistRepository.findByPlateNumberAndEnabledTrue(plateNumber);
    }

    public Blacklist disableBlacklist(Long id) {
        Optional<Blacklist> opt = blacklistRepository.findById(id);
        if (opt.isPresent()) {
            Blacklist blacklist = opt.get();
            blacklist.setEnabled(false);
            return blacklistRepository.save(blacklist);
        }
        return null;
    }
}
