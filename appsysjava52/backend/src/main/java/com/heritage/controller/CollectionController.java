package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.CollectionItem;
import com.heritage.entity.EnvironmentData;
import com.heritage.entity.MaintenanceReminder;
import com.heritage.repository.CollectionItemRepository;
import com.heritage.repository.EnvironmentDataRepository;
import com.heritage.repository.MaintenanceReminderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏管理Controller
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/collection")
@Tag(name = "收藏管理", description = "个人收藏、环境监测、保养提醒相关接口")
public class CollectionController {

    @Autowired
    private CollectionItemRepository collectionItemRepository;

    @Autowired
    private EnvironmentDataRepository environmentDataRepository;

    @Autowired
    private MaintenanceReminderRepository maintenanceReminderRepository;

    /**
     * 获取我的收藏列表
     */
    @GetMapping("/my-collection")
    @Operation(summary = "获取我的收藏列表")
    public Result<List<CollectionItem>> getMyCollection(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String tag) {
        List<CollectionItem> items;
        if (category != null) {
            items = collectionItemRepository.findByUserIdAndCategory(userId, category);
        } else if (tag != null && !tag.isEmpty()) {
            items = collectionItemRepository.findByUserIdAndTagsContaining(userId, tag);
        } else {
            items = collectionItemRepository.findByUserId(userId);
        }
        return Result.success(items);
    }

    /**
     * 添加收藏
     */
    @PostMapping("/item")
    @Operation(summary = "添加收藏项")
    public Result<CollectionItem> addCollectionItem(@RequestBody CollectionItem item) {
        CollectionItem saved = collectionItemRepository.save(item);
        return Result.success("收藏成功", saved);
    }

    /**
     * 更新收藏
     */
    @PutMapping("/item/{id}")
    @Operation(summary = "更新收藏项")
    public Result<CollectionItem> updateCollectionItem(
            @PathVariable Long id,
            @RequestBody CollectionItem item) {
        CollectionItem existing = collectionItemRepository.findById(id).orElse(null);
        if (existing == null) {
            return Result.error("收藏项不存在");
        }
        existing.setCustomName(item.getCustomName());
        existing.setCustomDescription(item.getCustomDescription());
        existing.setCustomImages(item.getCustomImages());
        existing.setCategory(item.getCategory());
        existing.setTags(item.getTags());
        existing.setMaterial(item.getMaterial());
        existing.setCollectionDate(item.getCollectionDate());
        existing.setEstimatedValue(item.getEstimatedValue());
        existing.setRemark(item.getRemark());
        existing.setEnvMonitorEnabled(item.getEnvMonitorEnabled());
        existing.setDeviceId(item.getDeviceId());
        collectionItemRepository.save(existing);
        return Result.success("更新成功", existing);
    }

    /**
     * 删除收藏
     */
    @DeleteMapping("/item/{id}")
    @Operation(summary = "删除收藏项")
    public Result<Void> deleteCollectionItem(@PathVariable Long id) {
        if (!collectionItemRepository.existsById(id)) {
            return Result.error("收藏项不存在");
        }
        collectionItemRepository.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 获取收藏项详情
     */
    @GetMapping("/item/{id}")
    @Operation(summary = "获取收藏项详情")
    public Result<CollectionItem> getCollectionItemDetail(@PathVariable Long id) {
        CollectionItem item = collectionItemRepository.findById(id).orElse(null);
        if (item == null) {
            return Result.error("收藏项不存在");
        }
        return Result.success(item);
    }

    /**
     * 获取环境监测数据
     */
    @GetMapping("/environment/{collectionItemId}")
    @Operation(summary = "获取环境监测数据")
    public Result<Map<String, Object>> getEnvironmentData(
            @PathVariable Long collectionItemId,
            @RequestParam(defaultValue = "7") int days) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        
        List<EnvironmentData> dataList = environmentDataRepository
                .findByCollectionItemIdAndRecordTimeBetweenOrderByRecordTimeAsc(
                        collectionItemId, startTime, endTime);
        
        List<EnvironmentData> abnormalList = environmentDataRepository
                .findByCollectionItemIdAndIsAbnormalOrderByRecordTimeDesc(collectionItemId, 1);
        
        Map<String, Object> result = new HashMap<>();
        result.put("dataList", dataList);
        result.put("abnormalList", abnormalList);
        result.put("totalAbnormal", abnormalList.size());
        
        return Result.success(result);
    }

    /**
     * 上报环境数据
     */
    @PostMapping("/environment/report")
    @Operation(summary = "上报环境监测数据")
    public Result<EnvironmentData> reportEnvironmentData(@RequestBody EnvironmentData data) {
        // 检查是否异常
        boolean isAbnormal = false;
        String abnormalDesc = "";
        
        if (data.getHumidity() != null && data.getHumidity().compareTo(new java.math.BigDecimal("70")) > 0) {
            isAbnormal = true;
            abnormalDesc = "湿度超过70%";
            data.setAbnormalType(2);
        } else if (data.getTemperature() != null && 
                (data.getTemperature().compareTo(new java.math.BigDecimal("10")) < 0 ||
                 data.getTemperature().compareTo(new java.math.BigDecimal("30")) > 0)) {
            isAbnormal = true;
            abnormalDesc = "温度超出10-30℃范围";
            data.setAbnormalType(1);
        }
        
        if (isAbnormal) {
            data.setIsAbnormal(1);
            data.setAbnormalDesc(abnormalDesc);
            data.setSuggestion("请检查环境设备，调整保存条件");
        } else {
            data.setIsAbnormal(0);
        }
        
        EnvironmentData saved = environmentDataRepository.save(data);
        return Result.success("数据上报成功", saved);
    }

    /**
     * 获取保养提醒列表
     */
    @GetMapping("/maintenance/{collectionItemId}")
    @Operation(summary = "获取保养提醒列表")
    public Result<List<MaintenanceReminder>> getMaintenanceReminders(
            @PathVariable Long collectionItemId,
            @RequestParam(required = false) Integer status) {
        List<MaintenanceReminder> reminders;
        if (status != null) {
            reminders = maintenanceReminderRepository.findByCollectionItemIdAndStatus(collectionItemId, status);
        } else {
            reminders = maintenanceReminderRepository.findByCollectionItemIdOrderByScheduledDateDesc(collectionItemId);
        }
        return Result.success(reminders);
    }

    /**
     * 添加保养提醒
     */
    @PostMapping("/maintenance")
    @Operation(summary = "添加保养提醒")
    public Result<MaintenanceReminder> addMaintenanceReminder(@RequestBody MaintenanceReminder reminder) {
        reminder.setStatus(0); // 待提醒
        MaintenanceReminder saved = maintenanceReminderRepository.save(reminder);
        return Result.success("提醒添加成功", saved);
    }

    /**
     * 完成保养
     */
    @PostMapping("/maintenance/complete/{id}")
    @Operation(summary = "标记保养完成")
    public Result<Void> completeMaintenance(@PathVariable Long id) {
        MaintenanceReminder reminder = maintenanceReminderRepository.findById(id).orElse(null);
        if (reminder == null) {
            return Result.error("提醒不存在");
        }
        reminder.setStatus(2); // 已完成
        maintenanceReminderRepository.save(reminder);
        return Result.success("保养已完成");
    }
}