package com.petclinic.service;

import com.petclinic.dto.Result;
import com.petclinic.entity.ExerciseData;
import com.petclinic.entity.Pet;
import com.petclinic.repository.ExerciseDataRepository;
import com.petclinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 运动监测服务类
 * 支持第三方智能项圈设备数据接入
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseDataRepository exerciseDataRepository;
    private final PetRepository petRepository;

    /**
     * 支持的第三方设备类型
     */
    private static final String[] SUPPORTED_DEVICES = {
        "FitBark", "Whistle", "PitPat", "Tractive", "Garmin", "Other"
    };

    /**
     * 同步第三方设备运动数据
     * 
     * @param deviceId 设备ID
     * @param deviceType 设备类型
     * @param dataSource 数据源
     * @param steps 步数
     * @param distance 距离（公里）
     * @param caloriesBurned 消耗卡路里
     * @param activeMinutes 活动分钟数
     * @return 同步结果
     */
    @Transactional
    public Result<ExerciseData> syncThirdPartyData(String deviceId, String deviceType, String dataSource,
                                                    Integer steps, BigDecimal distance, BigDecimal caloriesBurned,
                                                    Integer activeMinutes) {
        List<Pet> pets = petRepository.findBySmartCollarIdIsNotNullAndDeletedFalse();
        Optional<Pet> petOpt = pets.stream()
                .filter(p -> deviceId.equals(p.getSmartCollarId()))
                .findFirst();

        if (petOpt.isEmpty()) {
            return Result.error("未找到绑定该设备的宠物");
        }

        ExerciseData data = new ExerciseData();
        data.setPet(petOpt.get());
        data.setRecordTime(LocalDateTime.now());
        data.setSteps(steps);
        data.setDistance(distance);
        data.setCaloriesBurned(caloriesBurned);
        data.setActiveMinutes(activeMinutes);
        data.setRestingMinutes(24 * 60 - (activeMinutes != null ? activeMinutes : 0));
        data.setDeviceType(deviceType);
        data.setDeviceId(deviceId);
        data.setDataSource(dataSource);
        data.setSyncTime(LocalDateTime.now());

        ExerciseData saved = exerciseDataRepository.save(data);
        log.info("第三方数据同步成功，设备: {}, 宠物ID: {}", deviceType, petOpt.get().getId());
        return Result.success(saved);
    }

    /**
     * 获取宠物今日运动数据统计
     */
    public Result<Map<String, Object>> getTodayStats(Long petId) {
        LocalDateTime today = LocalDateTime.now();
        List<ExerciseData> todayData = exerciseDataRepository.findByPetIdAndDate(petId, today);

        Map<String, Object> stats = new HashMap<>();
        int totalSteps = 0;
        BigDecimal totalDistance = BigDecimal.ZERO;
        BigDecimal totalCalories = BigDecimal.ZERO;
        int totalActiveMinutes = 0;

        for (ExerciseData data : todayData) {
            if (data.getSteps() != null) totalSteps += data.getSteps();
            if (data.getDistance() != null) totalDistance = totalDistance.add(data.getDistance());
            if (data.getCaloriesBurned() != null) totalCalories = totalCalories.add(data.getCaloriesBurned());
            if (data.getActiveMinutes() != null) totalActiveMinutes += data.getActiveMinutes();
        }

        stats.put("steps", totalSteps);
        stats.put("distance", totalDistance);
        stats.put("caloriesBurned", totalCalories);
        stats.put("activeMinutes", totalActiveMinutes);
        stats.put("goalReached", totalSteps >= 10000);
        stats.put("goalSteps", 10000);

        return Result.success(stats);
    }

    /**
     * 获取运动历史数据
     */
    public Result<List<ExerciseData>> getExerciseHistory(Long petId) {
        List<ExerciseData> data = exerciseDataRepository
                .findByPetIdAndDeletedFalseOrderByRecordTimeDesc(petId);
        return Result.success(data);
    }

    /**
     * 获取支持的设备列表
     */
    public Result<String[]> getSupportedDevices() {
        return Result.success(SUPPORTED_DEVICES);
    }

    /**
     * 添加手动记录的运动数据
     */
    @Transactional
    public Result<ExerciseData> addManualRecord(ExerciseData data) {
        data.setDataSource("MANUAL");
        data.setSyncTime(LocalDateTime.now());
        ExerciseData saved = exerciseDataRepository.save(data);
        log.info("手动添加运动数据成功，宠物ID: {}", data.getPet().getId());
        return Result.success(saved);
    }
}