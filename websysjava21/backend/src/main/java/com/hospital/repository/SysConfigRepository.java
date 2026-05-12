package com.hospital.repository;

import com.hospital.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系统配置数据访问层
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface SysConfigRepository extends JpaRepository<SysConfig, Long>, JpaSpecificationExecutor<SysConfig> {

    /**
     * 根据配置键查询配置
     */
    Optional<SysConfig> findByConfigKey(String configKey);

    /**
     * 判断配置键是否存在
     */
    boolean existsByConfigKey(String configKey);

    /**
     * 判断配置键是否存在（排除指定ID）
     */
    boolean existsByConfigKeyAndIdNot(String configKey, Long id);
}
