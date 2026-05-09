package com.production.service;

import com.production.entity.ProcessParameter;
import com.production.repository.ProcessParameterRepository;
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
 * 工艺参数业务逻辑层
 */
@Service
@Transactional
public class ProcessParameterService {

    @Autowired
    private ProcessParameterRepository processParameterRepository;

    /**
     * 分页查询所有参数记录
     */
    public Page<ProcessParameter> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return processParameterRepository.findAll(pageable);
    }

    /**
     * 根据加工记录ID分页查询
     */
    public Page<ProcessParameter> findByRecordIdPage(Long recordId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        if (recordId == null) {
            return processParameterRepository.findAll(pageable);
        }
        return processParameterRepository.findByProcessingRecordId(recordId, pageable);
    }

    /**
     * 查询所有参数记录
     */
    public List<ProcessParameter> findAll() {
        return processParameterRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    /**
     * 根据ID查询
     */
    public Optional<ProcessParameter> findById(Long id) {
        return processParameterRepository.findById(id);
    }

    /**
     * 保存参数记录
     */
    public ProcessParameter save(ProcessParameter parameter) {
        // 检查参数值是否在标准范围内
        parameter.checkValueStatus();
        return processParameterRepository.save(parameter);
    }

    /**
     * 删除参数记录
     */
    public void deleteById(Long id) {
        processParameterRepository.deleteById(id);
    }

    /**
     * 根据加工记录ID查询参数
     */
    public List<ProcessParameter> findByRecordId(Long recordId) {
        return processParameterRepository.findByProcessingRecordIdOrderByRecordTime(recordId);
    }

    /**
     * 查询异常参数
     */
    public List<ProcessParameter> findAllAbnormal() {
        return processParameterRepository.findAllAbnormal();
    }

    /**
     * 查询指定时间范围内的参数
     */
    public List<ProcessParameter> findByTimeRange(Long recordId, LocalDateTime startTime, LocalDateTime endTime) {
        return processParameterRepository.findByTimeRange(recordId, startTime, endTime);
    }
}