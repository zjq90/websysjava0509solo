package com.websys.service;

import com.websys.common.PageResult;
import com.websys.entity.SystemConfig;
import com.websys.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 系统配置服务类
 * 
 * @author websys
 * @version 1.0.0
 */
@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 分页查询系统配置列表
     * 
     * @param configGroup 配置分组
     * @param configKey 配置键名
     * @param configName 配置名称
     * @param enabled 是否启用
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<SystemConfig> getConfigPage(String configGroup, String configKey, 
                                                   String configName, Integer enabled,
                                                   Integer current, Integer size) {
        Pageable pageable = PageRequest.of(current - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));

        Specification<SystemConfig> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(configGroup)) {
                predicates.add(criteriaBuilder.equal(root.get("configGroup"), configGroup));
            }

            if (StringUtils.hasText(configKey)) {
                predicates.add(criteriaBuilder.like(root.get("configKey"), "%" + configKey + "%"));
            }

            if (StringUtils.hasText(configName)) {
                predicates.add(criteriaBuilder.like(root.get("configName"), "%" + configName + "%"));
            }

            if (enabled != null) {
                predicates.add(criteriaBuilder.equal(root.get("enabled"), enabled));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<SystemConfig> page = systemConfigRepository.findAll(spec, pageable);
        return PageResult.of(page.getContent(), page.getTotalElements(), current, size);
    }

    /**
     * 根据ID查询配置
     * 
     * @param id 配置ID
     * @return 配置信息
     */
    public SystemConfig getConfigById(Long id) {
        return systemConfigRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("配置不存在"));
    }

    /**
     * 根据配置键名查询配置
     * 
     * @param configKey 配置键名
     * @return 配置信息
     */
    public Optional<SystemConfig> getConfigByKey(String configKey) {
        return systemConfigRepository.findByConfigKey(configKey);
    }

    /**
     * 创建系统配置
     * 
     * @param config 配置信息
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 创建后的配置
     */
    @Transactional
    public SystemConfig createConfig(SystemConfig config, Long operatorId, String operatorName) {
        if (systemConfigRepository.existsByConfigKey(config.getConfigKey())) {
            throw new RuntimeException("配置键名已存在");
        }

        if (config.getConfigType() == null) {
            config.setConfigType("STRING");
        }
        if (config.getEnabled() == null) {
            config.setEnabled(1);
        }
        if (config.getIsSystem() == null) {
            config.setIsSystem(0);
        }
        if (config.getSortOrder() == null) {
            config.setSortOrder(0);
        }
        config.setUpdateBy(operatorName);

        SystemConfig savedConfig = systemConfigRepository.save(config);

        operationLogService.saveLog(
            operatorId,
            operatorName,
            "CREATE",
            "CONFIG",
            "创建系统配置：" + savedConfig.getConfigName(),
            savedConfig.getId(),
            savedConfig.getConfigName(),
            null,
            1,
            null
        );

        return savedConfig;
    }

    /**
     * 更新系统配置
     * 
     * @param config 配置信息
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @return 更新后的配置
     */
    @Transactional
    public SystemConfig updateConfig(SystemConfig config, Long operatorId, String operatorName) {
        SystemConfig existingConfig = getConfigById(config.getId());

        if (systemConfigRepository.existsByConfigKeyAndIdNot(config.getConfigKey(), config.getId())) {
            throw new RuntimeException("配置键名已存在");
        }

        existingConfig.setConfigGroup(config.getConfigGroup());
        existingConfig.setConfigKey(config.getConfigKey());
        existingConfig.setConfigValue(config.getConfigValue());
        existingConfig.setConfigName(config.getConfigName());
        existingConfig.setDescription(config.getDescription());
        existingConfig.setConfigType(config.getConfigType());
        existingConfig.setEnabled(config.getEnabled());
        existingConfig.setSortOrder(config.getSortOrder());
        existingConfig.setUpdateBy(operatorName);

        SystemConfig savedConfig = systemConfigRepository.save(existingConfig);

        operationLogService.saveLog(
            operatorId,
            operatorName,
            "UPDATE",
            "CONFIG",
            "更新系统配置：" + savedConfig.getConfigName(),
            savedConfig.getId(),
            savedConfig.getConfigName(),
            null,
            1,
            null
        );

        return savedConfig;
    }

    /**
     * 删除系统配置
     * 
     * @param id 配置ID
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     */
    @Transactional
    public void deleteConfig(Long id, Long operatorId, String operatorName) {
        SystemConfig config = getConfigById(id);
        
        if (config.getIsSystem() == 1) {
            throw new RuntimeException("系统内置配置无法删除");
        }

        systemConfigRepository.deleteById(id);

        operationLogService.saveLog(
            operatorId,
            operatorName,
            "DELETE",
            "CONFIG",
            "删除系统配置：" + config.getConfigName(),
            config.getId(),
            config.getConfigName(),
            null,
            1,
            null
        );
    }

    /**
     * 根据配置分组获取配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    public List<SystemConfig> getConfigsByGroup(String configGroup) {
        return systemConfigRepository.findByConfigGroupOrderBySortOrderAsc(configGroup);
    }

    /**
     * 获取所有配置分组
     * 
     * @return 分组列表
     */
    public List<String> getAllConfigGroups() {
        List<String> groups = new ArrayList<>();
        groups.add("SYSTEM");
        groups.add("PAYMENT");
        groups.add("SMS");
        groups.add("OTHER");
        return groups;
    }

    /**
     * 获取所有配置类型
     * 
     * @return 类型列表
     */
    public List<String> getAllConfigTypes() {
        List<String> types = new ArrayList<>();
        types.add("STRING");
        types.add("NUMBER");
        types.add("BOOLEAN");
        types.add("JSON");
        return types;
    }
}
