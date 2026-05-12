package com.hospital.service;

import com.hospital.entity.SysConfig;
import com.hospital.repository.SysConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置服务类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class SysConfigService {

    @Autowired
    private SysConfigRepository configRepository;

    /**
     * 分页查询配置列表
     */
    public Page<SysConfig> findAll(Pageable pageable) {
        return configRepository.findAll(pageable);
    }

    /**
     * 查询所有配置
     */
    public List<SysConfig> findAll() {
        return configRepository.findAll();
    }

    /**
     * 根据ID查询配置
     */
    public Optional<SysConfig> findById(Long id) {
        return configRepository.findById(id);
    }

    /**
     * 根据配置键查询配置
     */
    public Optional<SysConfig> findByConfigKey(String configKey) {
        return configRepository.findByConfigKey(configKey);
    }

    /**
     * 保存配置
     */
    public SysConfig save(SysConfig config) {
        return configRepository.save(config);
    }

    /**
     * 删除配置
     */
    public void deleteById(Long id) {
        configRepository.deleteById(id);
    }

    /**
     * 判断配置键是否存在
     */
    public boolean existsByConfigKey(String configKey) {
        return configRepository.existsByConfigKey(configKey);
    }

    /**
     * 判断配置键是否存在（排除指定ID）
     */
    public boolean existsByConfigKeyAndIdNot(String configKey, Long id) {
        return configRepository.existsByConfigKeyAndIdNot(configKey, id);
    }
}
