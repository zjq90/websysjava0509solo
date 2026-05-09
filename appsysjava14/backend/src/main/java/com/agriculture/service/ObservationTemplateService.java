package com.agriculture.service;

import com.agriculture.entity.ObservationTemplate;
import com.agriculture.repository.ObservationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 观测模板服务类
 * 支持模板预设（如玉米、小麦标准观测项），减少重复输入
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class ObservationTemplateService {

    @Autowired
    private ObservationTemplateRepository templateRepository;

    /**
     * 创建模板
     * 
     * @param template 模板信息
     * @return 创建后的模板
     */
    public ObservationTemplate create(ObservationTemplate template) {
        template.setIsSystem(false);
        return templateRepository.save(template);
    }

    /**
     * 更新模板
     * 
     * @param template 模板信息
     * @return 更新后的模板
     */
    public ObservationTemplate update(ObservationTemplate template) {
        Optional<ObservationTemplate> existingOpt = templateRepository.findById(template.getId());
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("模板不存在");
        }
        
        ObservationTemplate existing = existingOpt.get();
        if (existing.getIsSystem()) {
            throw new RuntimeException("系统预设模板不允许修改");
        }
        
        return templateRepository.save(template);
    }

    /**
     * 删除模板
     * 
     * @param id 模板ID
     */
    public void deleteById(Long id) {
        Optional<ObservationTemplate> templateOpt = templateRepository.findById(id);
        if (templateOpt.isPresent()) {
            ObservationTemplate template = templateOpt.get();
            if (template.getIsSystem()) {
                throw new RuntimeException("系统预设模板不允许删除");
            }
            template.setStatus("INACTIVE");
            templateRepository.save(template);
        }
    }

    /**
     * 根据ID查找模板
     * 
     * @param id 模板ID
     * @return 模板对象
     */
    public Optional<ObservationTemplate> findById(Long id) {
        return templateRepository.findById(id);
    }

    /**
     * 查询所有模板
     * 
     * @return 模板列表
     */
    public List<ObservationTemplate> findAll() {
        return templateRepository.findAll();
    }

    /**
     * 根据作物类型查询模板
     * 系统模板优先显示
     * 
     * @param cropType 作物类型
     * @return 模板列表
     */
    public List<ObservationTemplate> findByCropType(String cropType) {
        return templateRepository.findByCropTypeAndStatusOrderByIsSystemDesc(cropType, "ACTIVE");
    }

    /**
     * 查询所有系统预设模板
     * 
     * @return 模板列表
     */
    public List<ObservationTemplate> findSystemTemplates() {
        return templateRepository.findByIsSystemAndStatus(true, "ACTIVE");
    }

    /**
     * 查询用户自定义模板
     * 
     * @param createdBy 创建人ID
     * @return 模板列表
     */
    public List<ObservationTemplate> findUserTemplates(Long createdBy) {
        return templateRepository.findByCreatedByAndStatusOrderByCreatedAtDesc(createdBy, "ACTIVE");
    }
}
