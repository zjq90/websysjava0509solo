package com.traceability.service;

import com.traceability.entity.FieldPlanting;
import com.traceability.repository.FieldPlantingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 田间种植业务逻辑服务类
 */
@Service
@Transactional
public class FieldPlantingService {

    @Autowired
    private FieldPlantingRepository fieldPlantingRepository;

    public List<FieldPlanting> findAll() {
        return fieldPlantingRepository.findAll();
    }

    public Page<FieldPlanting> findAll(Pageable pageable) {
        return fieldPlantingRepository.findAll(pageable);
    }

    public Optional<FieldPlanting> findById(Long id) {
        return fieldPlantingRepository.findById(id);
    }

    public List<FieldPlanting> findByBatchNo(String batchNo) {
        return fieldPlantingRepository.findByBatchNo(batchNo);
    }

    public FieldPlanting save(FieldPlanting fieldPlanting) {
        return fieldPlantingRepository.save(fieldPlanting);
    }

    public FieldPlanting update(FieldPlanting fieldPlanting) {
        return fieldPlantingRepository.save(fieldPlanting);
    }

    public void deleteById(Long id) {
        fieldPlantingRepository.deleteById(id);
    }
}
