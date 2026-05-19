package com.petclinic.service;

import com.petclinic.dto.Result;
import com.petclinic.entity.DewormingRecord;
import com.petclinic.entity.Pet;
import com.petclinic.repository.DewormingRecordRepository;
import com.petclinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 健康管理服务类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthService {

    private final DewormingRecordRepository dewormingRecordRepository;
    private final PetRepository petRepository;

    /**
     * 获取宠物健康提醒卡片数据
     * 
     * @param petId 宠物ID
     * @return 健康提醒数据
     */
    public Result<Map<String, Object>> getHealthCards(Long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if (petOpt.isEmpty()) {
            return Result.error("宠物不存在");
        }

        Map<String, Object> result = new HashMap<>();

        List<DewormingRecord> dewormingRecords = dewormingRecordRepository
                .findByPetIdAndDeletedFalseOrderByDewormingDateDesc(petId);
        
        Map<String, Object> dewormingCard = calculateDewormingCard(dewormingRecords);
        result.put("deworming", dewormingCard);

        return Result.success(result);
    }

    /**
     * 计算驱虫卡片数据
     */
    private Map<String, Object> calculateDewormingCard(List<DewormingRecord> records) {
        Map<String, Object> card = new HashMap<>();
        
        if (records.isEmpty()) {
            card.put("status", "pending");
            card.put("daysRemaining", null);
            card.put("lastRecord", null);
            card.put("completed", false);
        } else {
            DewormingRecord latest = records.get(0);
            
            if (latest.getCompleted()) {
                if (latest.getNextDewormingDate() != null) {
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), latest.getNextDewormingDate());
                    card.put("status", days <= 0 ? "overdue" : "upcoming");
                    card.put("daysRemaining", Math.max(0, days));
                } else {
                    card.put("status", "completed");
                    card.put("daysRemaining", null);
                }
                card.put("completed", true);
            } else {
                long days = ChronoUnit.DAYS.between(LocalDate.now(), latest.getDewormingDate());
                card.put("status", days <= 0 ? "overdue" : "upcoming");
                card.put("daysRemaining", Math.max(0, days));
                card.put("completed", false);
            }
            
            Map<String, Object> lastRecord = new HashMap<>();
            lastRecord.put("date", latest.getDewormingDate());
            lastRecord.put("medicineName", latest.getMedicineName());
            lastRecord.put("notes", latest.getNotes());
            card.put("lastRecord", lastRecord);
        }
        
        return card;
    }

    /**
     * 添加驱虫记录
     */
    @Transactional
    public Result<DewormingRecord> addDewormingRecord(DewormingRecord record) {
        DewormingRecord saved = dewormingRecordRepository.save(record);
        log.info("添加驱虫记录成功，ID: {}", saved.getId());
        return Result.success(saved);
    }

    /**
     * 获取驱虫历史记录
     */
    public Result<List<DewormingRecord>> getDewormingHistory(Long petId) {
        List<DewormingRecord> records = dewormingRecordRepository
                .findByPetIdAndDeletedFalseOrderByDewormingDateDesc(petId);
        return Result.success(records);
    }
}