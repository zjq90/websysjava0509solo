package com.example.websys.service;

import com.example.websys.dto.DashboardItemDTO;
import com.example.websys.entity.BusinessData;
import com.example.websys.entity.StatItem;
import com.example.websys.entity.UserDashboardConfig;
import com.example.websys.repository.BusinessDataRepository;
import com.example.websys.repository.StatItemRepository;
import com.example.websys.repository.UserDashboardConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户移动端看板配置业务逻辑服务类
 * 提供用户看板配置的增删改查及数据组装功能
 */
@Service
@Transactional
public class UserDashboardConfigService {

    @Autowired
    private UserDashboardConfigRepository userDashboardConfigRepository;

    @Autowired
    private StatItemRepository statItemRepository;

    @Autowired
    private BusinessDataRepository businessDataRepository;

    /**
     * 初始化用户默认看板配置
     * 如果用户还没有配置，则使用系统默认配置初始化
     */
    public List<UserDashboardConfig> initDefaultConfig(Long userId) {
        List<UserDashboardConfig> existing = userDashboardConfigRepository.findByUserIdOrderBySortOrderAsc(userId);
        if (!existing.isEmpty()) {
            return existing;
        }

        List<StatItem> defaultItems = statItemRepository.findByIsDefaultTrueAndStatusOrderBySortOrderAsc(1);
        List<UserDashboardConfig> configs = new ArrayList<>();

        for (int i = 0; i < defaultItems.size(); i++) {
            StatItem item = defaultItems.get(i);
            UserDashboardConfig config = new UserDashboardConfig();
            config.setUserId(userId);
            config.setStatItemId(item.getId());
            config.setSortOrder(i);
            config.setIsDisplay(true);
            configs.add(userDashboardConfigRepository.save(config));
        }

        return configs;
    }

    /**
     * 保存用户看板配置
     */
    public void saveUserConfig(Long userId, List<Long> statItemIds, Map<Long, Boolean> displayMap) {
        userDashboardConfigRepository.deleteByUserId(userId);

        for (int i = 0; i < statItemIds.size(); i++) {
            Long statItemId = statItemIds.get(i);
            UserDashboardConfig config = new UserDashboardConfig();
            config.setUserId(userId);
            config.setStatItemId(statItemId);
            config.setSortOrder(i);
            config.setIsDisplay(displayMap.getOrDefault(statItemId, true));
            userDashboardConfigRepository.save(config);
        }
    }

    /**
     * 获取用户看板配置
     */
    public List<UserDashboardConfig> getUserConfig(Long userId) {
        return userDashboardConfigRepository.findByUserIdOrderBySortOrderAsc(userId);
    }

    /**
     * 获取用户需要显示的看板数据（组装好的数据）
     */
    public List<DashboardItemDTO> getUserDashboardData(Long userId, LocalDate date) {
        initDefaultConfig(userId);

        List<UserDashboardConfig> configs = userDashboardConfigRepository
                .findByUserIdAndIsDisplayTrueOrderBySortOrderAsc(userId);

        if (configs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> statItemIds = configs.stream()
                .map(UserDashboardConfig::getStatItemId)
                .collect(Collectors.toList());

        List<StatItem> allItems = statItemRepository.findAllById(statItemIds);
        Map<Long, StatItem> itemMap = allItems.stream()
                .collect(Collectors.toMap(StatItem::getId, item -> item));

        List<String> statCodes = allItems.stream()
                .map(StatItem::getItemCode)
                .collect(Collectors.toList());

        List<BusinessData> dataList = businessDataRepository.findByStatCodesAndDate(statCodes, date);
        Map<String, BusinessData> dataMap = dataList.stream()
                .collect(Collectors.toMap(BusinessData::getStatCode, data -> data));

        Map<Long, UserDashboardConfig> configMap = configs.stream()
                .collect(Collectors.toMap(UserDashboardConfig::getStatItemId, c -> c));

        List<DashboardItemDTO> result = new ArrayList<>();

        for (Long itemId : statItemIds) {
            StatItem item = itemMap.get(itemId);
            if (item == null || item.getStatus() != 1) {
                continue;
            }

            BusinessData data = dataMap.get(item.getItemCode());
            UserDashboardConfig config = configMap.get(itemId);

            DashboardItemDTO dto = new DashboardItemDTO();
            dto.setStatItemId(item.getId());
            dto.setItemCode(item.getItemCode());
            dto.setItemName(item.getItemName());
            dto.setItemType(item.getItemType());
            dto.setUnit(item.getUnit());
            dto.setIcon(item.getIcon());
            dto.setColor(item.getColor());
            dto.setSortOrder(config.getSortOrder());

            if (data != null) {
                dto.setDataValue(data.getDataValue());
                dto.setDataText(data.getDataText());
                dto.setTrendValue(data.getTrendValue());
                dto.setTrendType(data.getTrendType());
                dto.setCompareValue(data.getCompareValue());
                dto.setCompareType(data.getCompareType());
            }

            result.add(dto);
        }

        return result;
    }

    /**
     * 更新某个统计项的显示状态
     */
    public int updateDisplayStatus(Long userId, Long statItemId, Boolean isDisplay) {
        return userDashboardConfigRepository.updateDisplayStatus(userId, statItemId, isDisplay);
    }
}
