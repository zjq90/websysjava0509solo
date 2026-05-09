package com.traceability.service;

import com.traceability.entity.Harvest;
import com.traceability.repository.HarvestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 收获业务逻辑服务类
 */
@Service
@Transactional
public class HarvestService {

    @Autowired
    private HarvestRepository harvestRepository;

    public List<Harvest> findAll() {
        return harvestRepository.findAll();
    }

    public Page<Harvest> findAll(Pageable pageable) {
        return harvestRepository.findAll(pageable);
    }

    public Optional<Harvest> findById(Long id) {
        return harvestRepository.findById(id);
    }

    public List<Harvest> findByBatchNo(String batchNo) {
        return harvestRepository.findByBatchNo(batchNo);
    }

    public Harvest save(Harvest harvest) {
        return harvestRepository.save(harvest);
    }

    public Harvest update(Harvest harvest) {
        return harvestRepository.save(harvest);
    }

    public void deleteById(Long id) {
        harvestRepository.deleteById(id);
    }
}
