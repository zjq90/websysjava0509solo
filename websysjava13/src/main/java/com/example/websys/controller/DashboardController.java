package com.example.websys.controller;

import com.example.websys.dto.DashboardItemDTO;
import com.example.websys.dto.Result;
import com.example.websys.entity.StatItem;
import com.example.websys.entity.UserDashboardConfig;
import com.example.websys.service.StatItemService;
import com.example.websys.service.UserDashboardConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动端看板REST API控制器
 * 提供看板配置和数据获取接口
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private UserDashboardConfigService userDashboardConfigService;

    @Autowired
    private StatItemService statItemService;

    @GetMapping("/data/{userId}")
    public Result<List<DashboardItemDTO>> getUserDashboardData(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return Result.success(userDashboardConfigService.getUserDashboardData(userId, date));
    }

    @GetMapping("/config/{userId}")
    public Result<List<UserDashboardConfig>> getUserConfig(@PathVariable Long userId) {
        return Result.success(userDashboardConfigService.getUserConfig(userId));
    }

    @PostMapping("/config/{userId}")
    public Result<Void> saveUserConfig(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> config) {
        
        @SuppressWarnings("unchecked")
        List<Number> ids = (List<Number>) config.get("statItemIds");
        List<Long> statItemIds = ids.stream()
                .map(Number::longValue)
                .collect(java.util.stream.Collectors.toList());

        @SuppressWarnings("unchecked")
        Map<String, Boolean> displayStrMap = (Map<String, Boolean>) config.get("displayMap");
        Map<Long, Boolean> displayMap = new HashMap<>();
        if (displayStrMap != null) {
            for (Map.Entry<String, Boolean> entry : displayStrMap.entrySet()) {
                displayMap.put(Long.parseLong(entry.getKey()), entry.getValue());
            }
        }

        userDashboardConfigService.saveUserConfig(userId, statItemIds, displayMap);
        return Result.success();
    }

    @GetMapping("/available-items")
    public Result<List<StatItem>> getAvailableItems() {
        return Result.success(statItemService.getAllEnabled());
    }
}
