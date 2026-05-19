package com.pethospital.service;

import com.pethospital.entity.Medicine;
import com.pethospital.entity.MedicationReminder;
import com.pethospital.entity.Prescription;
import com.pethospital.repository.MedicineRepository;
import com.pethospital.repository.MedicationReminderRepository;
import com.pethospital.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品购买Service
 * 提供处方药购买、非处方药自选、用药提醒等功能
 * 
 * @author Pet Hospital Team
 */
@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicationReminderRepository medicationReminderRepository;

    /**
     * 获取药品分类列表
     */
    public List<Map<String, Object>> getMedicineCategories() {
        return Arrays.asList(
            createCategory("驱虫药", "deworming", "bug"),
            createCategory("营养膏", "nutrition", "apple"),
            createCategory("感冒药", "cold", "thermometer"),
            createCategory("肠胃药", "stomach", "stomach"),
            createCategory("皮肤药", "skin", "bandage"),
            createCategory("疫苗", "vaccine", "syringe"),
            createCategory("保健品", "health", "heart"),
            createCategory("其他", "other", "box")
        );
    }

    private Map<String, Object> createCategory(String name, String id, String icon) {
        Map<String, Object> category = new HashMap<>();
        category.put("id", id);
        category.put("name", name);
        category.put("icon", icon);
        return category;
    }

    /**
     * 获取药品列表
     */
    public List<Medicine> getMedicines(String category, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return medicineRepository.findByNameContainingAndStatus(keyword, "AVAILABLE");
        }
        if (category != null && !category.isEmpty()) {
            return medicineRepository.findByCategoryAndStatus(category, "AVAILABLE");
        }
        return medicineRepository.findByStatus("AVAILABLE");
    }

    /**
     * 获取药品详情
     */
    public Medicine getMedicineById(Long medicineId) {
        return medicineRepository.findById(medicineId).orElse(null);
    }

    /**
     * 获取处方列表
     */
    public List<Prescription> getUserPrescriptions(Long userId) {
        return prescriptionRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 获取处方详情
     */
    public Prescription getPrescriptionById(Long prescriptionId) {
        return prescriptionRepository.findById(prescriptionId).orElse(null);
    }

    /**
     * 验证处方
     */
    @Transactional
    public boolean validatePrescription(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElse(null);
        if (prescription != null && !prescription.getUsed()) {
            prescription.setUsed(true);
            prescription.setStatus("USED");
            prescriptionRepository.save(prescription);
            return true;
        }
        return false;
    }

    /**
     * 添加用药提醒
     */
    @Transactional
    public MedicationReminder addMedicationReminder(MedicationReminder reminder) {
        reminder.setEnabled(true);
        reminder.setSoundEnabled(true);
        reminder.setVibrationEnabled(true);
        return medicationReminderRepository.save(reminder);
    }

    /**
     * 获取用户用药提醒
     */
    public List<MedicationReminder> getUserMedicationReminders(Long userId) {
        return medicationReminderRepository.findByUserIdOrderByReminderTimeAsc(userId);
    }

    /**
     * 更新用药提醒
     */
    @Transactional
    public MedicationReminder updateMedicationReminder(MedicationReminder reminder) {
        return medicationReminderRepository.save(reminder);
    }

    /**
     * 删除用药提醒
     */
    @Transactional
    public void deleteMedicationReminder(Long reminderId) {
        medicationReminderRepository.deleteById(reminderId);
    }

    /**
     * 多设备同步提醒（模拟）
     */
    public Map<String, Object> syncRemindersToDevices(Long userId, String deviceIds) {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("devices", deviceIds.split(","));
        result.put("synced", true);
        result.put("message", "用药提醒已同步到所有设备");
        return result;
    }
}
