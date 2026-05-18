package com.culturalrelic.service;

import com.culturalrelic.entity.DigitalTwin;
import com.culturalrelic.repository.DigitalTwinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 数字分身业务逻辑层
 */
@Service
public class DigitalTwinService {

    @Autowired
    private DigitalTwinRepository digitalTwinRepository;

    public DigitalTwin save(DigitalTwin twin) {
        return digitalTwinRepository.save(twin);
    }

    public Optional<DigitalTwin> findById(Long id) {
        return digitalTwinRepository.findById(id);
    }

    public Page<DigitalTwin> findAll(Pageable pageable) {
        return digitalTwinRepository.findAll(pageable);
    }

    public List<DigitalTwin> findAll() {
        return digitalTwinRepository.findAll();
    }

    public DigitalTwin update(DigitalTwin twin) {
        return digitalTwinRepository.save(twin);
    }

    @Transactional
    public boolean delete(Long id) {
        int rows = digitalTwinRepository.logicDelete(id);
        return rows > 0;
    }

    public List<DigitalTwin> findByRelicId(Long relicId) {
        return digitalTwinRepository.findByRelicId(relicId);
    }
}
