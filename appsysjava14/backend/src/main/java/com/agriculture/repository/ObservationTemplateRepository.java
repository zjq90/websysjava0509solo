package com.agriculture.repository;

import com.agriculture.entity.ObservationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 观测模板数据访问层
 * 提供观测模板表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface ObservationTemplateRepository extends JpaRepository<ObservationTemplate, Long> {

    /**
     * 根据作物类型查找模板
     * 
     * @param cropType 作物类型
     * @return 模板列表
     */
    List<ObservationTemplate> findByCropTypeAndStatusOrderByIsSystemDesc(String cropType, String status);

    /**
     * 查找所有系统预设模板
     * 
     * @param isSystem 是否系统模板
     * @param status 状态
     * @return 模板列表
     */
    List<ObservationTemplate> findByIsSystemAndStatus(Boolean isSystem, String status);

    /**
     * 根据创建人查找自定义模板
     * 
     * @param createdBy 创建人ID
     * @param status 状态
     * @return 模板列表
     */
    List<ObservationTemplate> findByCreatedByAndStatusOrderByCreatedAtDesc(Long createdBy, String status);
}
