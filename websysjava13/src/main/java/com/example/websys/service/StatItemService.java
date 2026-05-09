package com.example.websys.service;

import com.example.websys.entity.StatItem;
import com.example.websys.repository.StatItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 数据统计项业务逻辑服务类
 * 提供统计项的增删改查及默认配置管理功能
 */
@Service
@Transactional
public class StatItemService {

    @Autowired
    private StatItemRepository statItemRepository;

    /**
     * 获取所有启用的统计项
     */
    public List<StatItem> getAllEnabled() {
        return statItemRepository.findByStatusOrderBySortOrderAsc(1);
    }

    /**
     * 获取所有默认显示的统计项
     */
    public List<StatItem> getDefaultItems() {
        return statItemRepository.findByIsDefaultTrueAndStatusOrderBySortOrderAsc(1);
    }

    /**
     * 根据ID获取统计项
     */
    public Optional<StatItem> getById(Long id) {
        return statItemRepository.findById(id);
    }

    /**
     * 保存统计项
     */
    public StatItem save(StatItem statItem) {
        return statItemRepository.save(statItem);
    }

    /**
     * 更新统计项
     */
    public StatItem update(StatItem statItem) {
        Optional<StatItem> existingOpt = statItemRepository.findById(statItem.getId());
        if (existingOpt.isPresent()) {
            StatItem existing = existingOpt.get();
            existing.setItemName(statItem.getItemName());
            existing.setItemType(statItem.getItemType());
            existing.setDataSource(statItem.getDataSource());
            existing.setUnit(statItem.getUnit());
            existing.setIcon(statItem.getIcon());
            existing.setColor(statItem.getColor());
            existing.setIsDefault(statItem.getIsDefault());
            existing.setSortOrder(statItem.getSortOrder());
            existing.setStatus(statItem.getStatus());
            existing.setDescription(statItem.getDescription());
            return statItemRepository.save(existing);
        }
        return null;
    }

    /**
     * 删除统计项
     */
    public void deleteById(Long id) {
        statItemRepository.deleteById(id);
    }

    /**
     * 获取所有统计项
     */
    public List<StatItem> getAll() {
        return statItemRepository.findAll();
    }
}
