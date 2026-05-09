package com.production.service;

import com.production.entity.ProcessingRecord;
import com.production.repository.ProcessingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 加工流程记录业务逻辑层
 */
@Service
@Transactional
public class ProcessingRecordService {

    @Autowired
    private ProcessingRecordRepository processingRecordRepository;

    /**
     * 分页查询所有记录
     */
    public Page<ProcessingRecord> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return processingRecordRepository.findAll(pageable);
    }

    /**
     * 根据生产计划ID分页查询
     */
    public Page<ProcessingRecord> findByPlanIdPage(Long planId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        if (planId == null) {
            return processingRecordRepository.findAll(pageable);
        }
        return processingRecordRepository.findByProductionPlanId(planId, pageable);
    }

    /**
     * 查询所有记录
     */
    public List<ProcessingRecord> findAll() {
        return processingRecordRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    /**
     * 根据ID查询
     */
    public Optional<ProcessingRecord> findById(Long id) {
        return processingRecordRepository.findById(id);
    }

    /**
     * 根据记录编号查询
     */
    public Optional<ProcessingRecord> findByRecordCode(String recordCode) {
        return processingRecordRepository.findByRecordCode(recordCode);
    }

    /**
     * 保存记录
     */
    public ProcessingRecord save(ProcessingRecord record) {
        if (record.getId() == null) {
            // 新增记录，检查编号是否已存在
            if (processingRecordRepository.existsByRecordCode(record.getRecordCode())) {
                throw new RuntimeException("记录编号已存在：" + record.getRecordCode());
            }
            if (record.getStatus() == null) {
                record.setStatus("PENDING");
            }
            if (record.getStartTime() == null) {
                record.setStartTime(LocalDateTime.now());
            }
        }
        record.setUpdateTime(LocalDateTime.now());
        return processingRecordRepository.save(record);
    }

    /**
     * 删除记录
     */
    public void deleteById(Long id) {
        processingRecordRepository.deleteById(id);
    }

    /**
     * 更新状态
     */
    public ProcessingRecord updateStatus(Long id, String status) {
        Optional<ProcessingRecord> recordOpt = processingRecordRepository.findById(id);
        if (recordOpt.isPresent()) {
            ProcessingRecord record = recordOpt.get();
            record.setStatus(status);
            record.setUpdateTime(LocalDateTime.now());
            if ("COMPLETED".equals(status) && record.getEndTime() == null) {
                record.setEndTime(LocalDateTime.now());
            }
            return processingRecordRepository.save(record);
        }
        return null;
    }

    /**
     * 根据生产计划ID查询工序列表
     */
    public List<ProcessingRecord> findByPlanId(Long planId) {
        return processingRecordRepository.findByProductionPlanIdOrderByProcessOrder(planId);
    }

    /**
     * 根据状态查询
     */
    public List<ProcessingRecord> findByStatus(String status) {
        return processingRecordRepository.findByStatus(status);
    }

    /**
     * 根据工序类型查询
     */
    public List<ProcessingRecord> findByProcessType(String processType) {
        return processingRecordRepository.findByProcessType(processType);
    }
}