package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.Heritage;
import com.heritage.entity.IotDeviceData;
import com.heritage.entity.RiskAlert;
import com.heritage.entity.UserBehaviorLog;
import com.heritage.repository.HeritageRepository;
import com.heritage.repository.UserBehaviorLogRepository;
import com.heritage.service.RecommendationService;
import com.heritage.service.RiskAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@Tag(name = "数据分析", description = "数据分析、推荐、风险预警")
@CrossOrigin(origins = "*")
public class DataAnalysisController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private RiskAlertService riskAlertService;

    @Autowired
    private HeritageRepository heritageRepository;

    @Autowired
    private UserBehaviorLogRepository userBehaviorLogRepository;

    @GetMapping("/recommendations/user/{userId}")
    @Operation(summary = "基于用户行为推荐")
    public Result<List<Heritage>> getUserRecommendations(@PathVariable Long userId) {
        List<UserBehaviorLog> logs = userBehaviorLogRepository.findByUserId(userId);
        return Result.success(recommendationService.recommendByUserBehavior(userId, logs));
    }

    @GetMapping("/recommendations/similar/{heritageId}")
    @Operation(summary = "相似文物推荐")
    public Result<List<Heritage>> getSimilarHeritages(
            @PathVariable Long heritageId,
            @RequestParam(defaultValue = "10") int limit) {
        return Result.success(recommendationService.recommendSimilarHeritages(heritageId, limit));
    }

    @GetMapping("/preferences/{userId}")
    @Operation(summary = "获取用户偏好画像")
    public Result<Map<String, Object>> getUserPreferences(@PathVariable Long userId) {
        List<UserBehaviorLog> logs = userBehaviorLogRepository.findByUserId(userId);
        return Result.success(recommendationService.getUserPreferenceProfile(userId, logs));
    }

    @PostMapping("/risk/check")
    @Operation(summary = "环境风险检测")
    public Result<List<RiskAlert>> checkEnvironmentRisks(@RequestBody List<IotDeviceData> deviceDataList) {
        return Result.success(riskAlertService.checkEnvironmentRisks(deviceDataList));
    }

    @GetMapping("/risk/thresholds")
    @Operation(summary = "获取所有阈值")
    public Result<Map<String, Map<String, BigDecimal>>> getAllThresholds() {
        return Result.success(riskAlertService.getAllThresholds());
    }

    @PostMapping("/risk/thresholds")
    @Operation(summary = "更新阈值")
    public Result<Void> updateThreshold(
            @RequestParam String alertType,
            @RequestParam String level,
            @RequestParam BigDecimal newValue) {
        riskAlertService.updateThreshold(alertType, level, newValue);
        return Result.success();
    }

    @PostMapping("/behavior-log")
    @Operation(summary = "记录用户行为")
    public Result<UserBehaviorLog> recordBehavior(@RequestBody UserBehaviorLog log) {
        log.setBehaviorTime(LocalDateTime.now());
        return Result.success(userBehaviorLogRepository.save(log));
    }

    @GetMapping("/behavior-log/user/{userId}")
    @Operation(summary = "获取用户行为日志")
    public Result<List<UserBehaviorLog>> getUserBehaviorLogs(@PathVariable Long userId) {
        return Result.success(userBehaviorLogRepository.findByUserId(userId));
    }

    @PostMapping("/generate-behavior-logs")
    @Operation(summary = "生成测试行为日志")
    public Result<List<UserBehaviorLog>> generateBehaviorLogs(@RequestParam(defaultValue = "50") int count) {
        String[] behaviorTypes = {"click", "view", "collect", "purchase"};
        List<Heritage> heritages = heritageRepository.findAll();

        for (int i = 0; i < count; i++) {
            UserBehaviorLog log = new UserBehaviorLog();
            log.setUserId(1L);
            log.setSessionId("session_" + (i % 10));
            log.setBehaviorType(behaviorTypes[i % 4]);
            log.setTargetType("heritage");
            if (!heritages.isEmpty()) {
                Heritage heritage = heritages.get(i % heritages.size());
                log.setTargetId(heritage.getId());
                log.setTargetName(heritage.getName());
            }
            log.setIpAddress("192.168.1." + (i % 255));
            log.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            log.setStayDuration((long) (Math.random() * 300));
            log.setClickCount((int) (Math.random() * 10));
            log.setScrollDepth((int) (Math.random() * 100));
            log.setDeviceType(i % 2 == 0 ? "desktop" : "mobile");
            log.setBehaviorTime(LocalDateTime.now().minusHours(i % 24));

            userBehaviorLogRepository.save(log);
        }

        return Result.success("成功生成" + count + "条行为日志", userBehaviorLogRepository.findAll());
    }

    @PostMapping("/generate-iot-data")
    @Operation(summary = "生成测试物联网数据")
    public Result<List<IotDeviceData>> generateIotData(@RequestParam(defaultValue = "30") int count) {
        List<IotDeviceData> dataList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            IotDeviceData data = new IotDeviceData();
            data.setDeviceId("device_" + (i % 5));
            data.setDeviceName("环境监测设备" + (i % 5));
            data.setDeviceType("environment");
            data.setLocation("展厅" + (i % 3 + 1));

            BigDecimal temperature = new BigDecimal(20 + Math.random() * 25);
            BigDecimal humidity = new BigDecimal(40 + Math.random() * 50);
            BigDecimal vibration = new BigDecimal(Math.random() * 3);

            data.setTemperature(temperature);
            data.setHumidity(humidity);
            data.setVibration(vibration);
            data.setLightIntensity(new BigDecimal(500 + Math.random() * 1000));
            data.setBatteryLevel(new BigDecimal(80 + Math.random() * 20));
            data.setSignalStrength(new BigDecimal(70 + Math.random() * 30));
            data.setDataTime(LocalDateTime.now().minusMinutes(i));
            data.setIsValid(true);
            data.setRawData("{\"temperature\":" + temperature + ",\"humidity\":" + humidity + "}");

            dataList.add(data);
        }

        return Result.success("成功生成" + count + "条物联网数据", dataList);
    }
}
