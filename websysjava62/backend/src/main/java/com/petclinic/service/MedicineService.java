package com.petclinic.service;

import com.petclinic.entity.Medicine;
import com.petclinic.entity.MedicineCategory;
import com.petclinic.repository.MedicineCategoryRepository;
import com.petclinic.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 药品管理Service
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineCategoryRepository medicineCategoryRepository;

    // ==================== 药品分类管理 ====================

    public List<MedicineCategory> findAllCategories() {
        return medicineCategoryRepository.findAll();
    }

    public Optional<MedicineCategory> findCategoryById(Long id) {
        return medicineCategoryRepository.findById(id);
    }

    public MedicineCategory saveCategory(MedicineCategory category) {
        return medicineCategoryRepository.save(category);
    }

    public MedicineCategory updateCategory(Long id, MedicineCategory category) {
        Optional<MedicineCategory> optional = medicineCategoryRepository.findById(id);
        if (optional.isPresent()) {
            MedicineCategory existing = optional.get();
            existing.setCategoryName(category.getCategoryName());
            existing.setDescription(category.getDescription());
            existing.setParentId(category.getParentId());
            existing.setSortOrder(category.getSortOrder());
            existing.setStatus(category.getStatus());
            return medicineCategoryRepository.save(existing);
        }
        return null;
    }

    public void deleteCategoryById(Long id) {
        medicineCategoryRepository.deleteById(id);
    }

    public List<MedicineCategory> findCategoriesByStatus(String status) {
        return medicineCategoryRepository.findByStatus(status);
    }

    // ==================== 药品管理 ====================

    public List<Medicine> findAllMedicines() {
        return medicineRepository.findAll();
    }

    public Optional<Medicine> findMedicineById(Long id) {
        return medicineRepository.findById(id);
    }

    public Medicine saveMedicine(Medicine medicine) {
        if (medicine.getCategoryId() != null) {
            Optional<MedicineCategory> category = medicineCategoryRepository.findById(medicine.getCategoryId());
            category.ifPresent(c -> medicine.setCategoryName(c.getCategoryName()));
        }
        return medicineRepository.save(medicine);
    }

    public Medicine updateMedicine(Long id, Medicine medicine) {
        Optional<Medicine> optional = medicineRepository.findById(id);
        if (optional.isPresent()) {
            Medicine existing = optional.get();
            existing.setMedicineName(medicine.getMedicineName());
            existing.setCategoryId(medicine.getCategoryId());
            existing.setDescription(medicine.getDescription());
            existing.setManufacturer(medicine.getManufacturer());
            existing.setPrice(medicine.getPrice());
            existing.setStock(medicine.getStock());
            existing.setPetType(medicine.getPetType());
            existing.setUsageDosage(medicine.getUsageDosage());
            existing.setAttention(medicine.getAttention());
            existing.setStatus(medicine.getStatus());
            if (medicine.getCategoryId() != null) {
                Optional<MedicineCategory> category = medicineCategoryRepository.findById(medicine.getCategoryId());
                category.ifPresent(c -> existing.setCategoryName(c.getCategoryName()));
            }
            return medicineRepository.save(existing);
        }
        return null;
    }

    public void deleteMedicineById(Long id) {
        medicineRepository.deleteById(id);
    }

    public List<Medicine> findMedicinesByCategoryId(Long categoryId) {
        return medicineRepository.findByCategoryId(categoryId);
    }

    public List<Medicine> findMedicinesByStatus(String status) {
        return medicineRepository.findByStatus(status);
    }

    public List<Medicine> searchMedicineByName(String medicineName) {
        return medicineRepository.findByMedicineNameContaining(medicineName);
    }
}
