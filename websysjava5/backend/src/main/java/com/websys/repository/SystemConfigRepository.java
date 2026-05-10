package com.websys.repository;

import com.websys.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置Repository接口
 * 
 * @author websys
 * @version 1.0.0
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long>, JpaSpecificationExecutor<SystemConfig> {

    /**
     * 根据配置键名查询
     * 
     * @param configKey 配置键名
     * @return 配置信息
     */
    Optional<SystemConfig> findByConfigKey(String configKey);

    /**
     * 根据配置分组查询配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    List<SystemConfig> findByConfigGroupOrderBySortOrderAsc(String configGroup);

    /**
     * 查询所有启用的配置
     * 
     * @param enabled 是否启用
     * @return 配置列表
     */
    List<SystemConfig> findByEnabledOrderBySortOrderAsc(Integer enabled);

    /**
     * 检查配置键名是否存在
     * 
     * @param configKey 配置键名
     * @return 是否存在
     */
    boolean existsByConfigKey(String configKey);

    /**
     * 检查配置键名是否存在（排除指定ID）
     * 
     * @param configKey 配置键名
     * @param id 排除的ID
     * @return 是否存在
     */
    boolean existsByConfigKeyAndIdNot(String configKey, Long id);

    /**
     * 根据配置分组和键名查询
     * 
     * @param configGroup 配置分组
     * @param configKey 配置键名
     * @return 配置信息
     */
    Optional<SystemConfig> findByConfigGroupAndConfigKey(String configGroup, String configKey);
}
