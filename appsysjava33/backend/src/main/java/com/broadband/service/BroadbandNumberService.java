package com.broadband.service;

import com.broadband.entity.BroadbandNumber;
import com.broadband.repository.BroadbandNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宽带号码服务
 * 处理宽带号码管理、选号等功能
 * 
 * @author broadband
 * @version 1.0.0
 */
@Service
public class BroadbandNumberService {

    @Autowired
    private BroadbandNumberRepository broadbandNumberRepository;

    public List<BroadbandNumber> getAvailableNumbers(String region) {
        return broadbandNumberRepository.findByStatusAndRegionOrderByNumber(1, region);
    }

    public List<BroadbandNumber> getFancyNumbers(String region) {
        return broadbandNumberRepository.findByStatusAndTypeAndRegionOrderByNumber(1, 2, region);
    }

    public List<BroadbandNumber> searchNumbers(String pattern) {
        return broadbandNumberRepository.findByNumberPattern(pattern);
    }

    public BroadbandNumber lockNumber(Long numberId, Long userId) {
        BroadbandNumber number = broadbandNumberRepository.findById(numberId)
                .orElseThrow(() -> new RuntimeException("号码不存在"));
        if (number.getStatus() != 1) {
            throw new RuntimeException("该号码已被占用");
        }
        number.setStatus(2);
        number.setLockUserId(userId);
        return broadbandNumberRepository.save(number);
    }

    public BroadbandNumber unlockNumber(Long numberId) {
        BroadbandNumber number = broadbandNumberRepository.findById(numberId)
                .orElseThrow(() -> new RuntimeException("号码不存在"));
        number.setStatus(1);
        number.setLockUserId(null);
        return broadbandNumberRepository.save(number);
    }
}
