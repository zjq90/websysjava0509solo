package com.traceability.service;

import com.traceability.entity.Processing;
import com.traceability.repository.ProcessingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 加工业务逻辑服务类
 */
@Service
@Transactional
public class ProcessingService {

    @Autowired
    private ProcessingRepository processingRepository;

    public List<Processing> findAll() {
        return processingRepository.findAll();
    }

    public Page<Processing> findAll(Pageable pageable) {
        return processingRepository.findAll(pageable);
    }

    public Optional<Processing> findById(Long id) {
        return processingRepository.findById(id);
    }

    public List<Processing> findByBatchNo(String batchNo) {
        return processingRepository.findByBatchNo(batchNo);
    }

    public Processing save(Processing processing) {
        return processingRepository.save(processing);
    }

    public Processing update(Processing processing) {
        return processingRepository.save(processing);
    }

    public void deleteById(Long id) {
        processingRepository.deleteById(id);
    }
}
