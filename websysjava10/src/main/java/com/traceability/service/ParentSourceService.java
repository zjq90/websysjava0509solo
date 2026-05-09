package com.traceability.service;

import com.traceability.entity.ParentSource;
import com.traceability.repository.ParentSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 亲本来源业务逻辑服务类
 */
@Service
@Transactional
public class ParentSourceService {

    @Autowired
    private ParentSourceRepository parentSourceRepository;

    public List<ParentSource> findAll() {
        return parentSourceRepository.findAll();
    }

    public Page<ParentSource> findAll(Pageable pageable) {
        return parentSourceRepository.findAll(pageable);
    }

    public Optional<ParentSource> findById(Long id) {
        return parentSourceRepository.findById(id);
    }

    public List<ParentSource> findByBatchNo(String batchNo) {
        return parentSourceRepository.findByBatchNo(batchNo);
    }

    public ParentSource save(ParentSource parentSource) {
        return parentSourceRepository.save(parentSource);
    }

    public ParentSource update(ParentSource parentSource) {
        return parentSourceRepository.save(parentSource);
    }

    public void deleteById(Long id) {
        parentSourceRepository.deleteById(id);
    }
}
