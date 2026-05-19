package com.pethospital.service;

import com.pethospital.entity.Pet;
import com.pethospital.entity.VaccineRecord;
import com.pethospital.repository.PetRepository;
import com.pethospital.repository.VaccineRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 宠物管理Service
 * 提供宠物档案管理、疫苗记录管理等功能
 * 
 * @author Pet Hospital Team
 */
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VaccineRecordRepository vaccineRecordRepository;

    /**
     * 获取用户的所有宠物
     */
    public List<Pet> getUserPets(Long userId) {
        return petRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 获取宠物详情
     */
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    /**
     * 添加宠物
     */
    @Transactional
    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * 更新宠物信息
     */
    @Transactional
    public Pet updatePet(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * 删除宠物
     */
    @Transactional
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

    /**
     * 获取宠物疫苗记录
     */
    public List<VaccineRecord> getPetVaccineRecords(Long petId) {
        return vaccineRecordRepository.findByPetIdOrderByVaccinationDateDesc(petId);
    }

    /**
     * 添加疫苗记录
     */
    @Transactional
    public VaccineRecord addVaccineRecord(VaccineRecord record) {
        return vaccineRecordRepository.save(record);
    }

    /**
     * 获取疫苗到期提醒
     */
    public Map<String, Object> getVaccineReminders(Long petId) {
        Map<String, Object> result = new HashMap<>();
        List<VaccineRecord> records = vaccineRecordRepository.findByPetIdOrderByVaccinationDateDesc(petId);
        
        LocalDate today = LocalDate.now();
        for (VaccineRecord record : records) {
            if (record.getExpiryDate() != null && !record.getReminderSent()) {
                long daysUntilExpiry = ChronoUnit.DAYS.between(today, record.getExpiryDate());
                if (daysUntilExpiry >= 0 && daysUntilExpiry <= 30) {
                    Map<String, Object> reminder = new HashMap<>();
                    reminder.put("vaccineName", record.getVaccineName());
                    reminder.put("expiryDate", record.getExpiryDate());
                    reminder.put("daysUntilExpiry", daysUntilExpiry);
                    reminder.put("message", record.getVaccineName() + "还有" + daysUntilExpiry + "天到期");
                    result.put("reminder_" + record.getId(), reminder);
                }
            }
        }
        return result;
    }
}
