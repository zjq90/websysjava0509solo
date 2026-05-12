package com.hospital.service;

import com.hospital.entity.Medicine;
import com.hospital.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 药品Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    public Optional<Medicine> findById(Long id) {
        return medicineRepository.findById(id);
    }

    public Medicine findByMedicineCode(String medicineCode) {
        return medicineRepository.findByMedicineCode(medicineCode);
    }

    public List<Medicine> search(String keyword) {
        return medicineRepository.search(keyword);
    }

    public List<Medicine> findByCategory(String category) {
        return medicineRepository.findByCategory(category);
    }

    public List<Medicine> findLowStockMedicines() {
        return medicineRepository.findLowStockMedicines();
    }

    @Transactional
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Transactional
    public void deleteById(Long id) {
        medicineRepository.deleteById(id);
    }

    @Transactional
    public Medicine updateStock(Long id, int quantity) {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        if (medicine == null) {
            return null;
        }
        medicine.setStockQuantity(medicine.getStockQuantity() + quantity);
        return medicineRepository.save(medicine);
    }
}
