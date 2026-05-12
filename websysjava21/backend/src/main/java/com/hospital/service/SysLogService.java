package com.hospital.service;

import com.hospital.entity.SysLog;
import com.hospital.repository.SysLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 系统日志服务类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class SysLogService {

    @Autowired
    private SysLogRepository logRepository;

    /**
     * 分页查询日志列表
     */
    public Page<SysLog> findAll(Pageable pageable) {
        return logRepository.findAll(pageable);
    }

    /**
     * 根据ID查询日志
     */
    public Optional<SysLog> findById(Long id) {
        return logRepository.findById(id);
    }

    /**
     * 保存日志
     */
    public SysLog save(SysLog log) {
        return logRepository.save(log);
    }

    /**
     * 删除日志
     */
    public void deleteById(Long id) {
        logRepository.deleteById(id);
    }

    /**
     * 批量删除日志
     */
    public void deleteAllById(Iterable<Long> ids) {
        logRepository.deleteAllById(ids);
    }
}
