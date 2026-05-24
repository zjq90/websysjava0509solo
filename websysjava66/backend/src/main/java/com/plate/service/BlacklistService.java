package com.plate.service;

import com.plate.entity.Blacklist;
import com.plate.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlacklistService {
    @Autowired
    private BlacklistRepository blacklistRepository;

    public List<Blacklist> getAllActiveBlacklists() {
        return blacklistRepository.findByIsActiveTrue();
    }

    public List<Blacklist> getAllBlacklists() {
        return blacklistRepository.findAll();
    }

    public Optional<Blacklist> getBlacklistById(Long id) {
        return blacklistRepository.findById(id);
    }

    public Blacklist addToBlacklist(Blacklist blacklist) {
        if (blacklistRepository.existsByPlateNumberAndIsActiveTrue(blacklist.getPlateNumber())) {
            throw new RuntimeException("该车牌号已在黑名单中");
        }
        return blacklistRepository.save(blacklist);
    }

    public Blacklist removeFromBlacklist(Long id, String reason) {
        Optional<Blacklist> optional = blacklistRepository.findById(id);
        if (optional.isPresent()) {
            Blacklist blacklist = optional.get();
            blacklist.setIsActive(false);
            blacklist.setRemoveReason(reason);
            return blacklistRepository.save(blacklist);
        }
        return null;
    }

    public long getActiveBlacklistCount() {
        return blacklistRepository.countByIsActiveTrue();
    }

    public boolean isBlacklisted(String plateNumber) {
        return blacklistRepository.existsByPlateNumberAndIsActiveTrue(plateNumber);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void expireBlacklist() {
        blacklistRepository.expireBlacklist(LocalDateTime.now());
    }
}
