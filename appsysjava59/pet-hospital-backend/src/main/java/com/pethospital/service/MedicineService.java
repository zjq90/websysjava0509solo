package com.pethospital.service;

import com.pethospital.entity.Medicine;
import com.pethospital.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public List<Medicine> getMedicinesByCategory(String category) {
        return medicineRepository.findByCategory(category);
    }

    public List<Medicine> searchMedicine(String keyword) {
        return medicineRepository.searchByName(keyword);
    }

    public Optional<Medicine> getMedicineById(Long id) {
        return medicineRepository.findById(id);
    }

    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }

    public boolean checkStock(Long id, int quantity) {
        Optional<Medicine> medicineOpt = medicineRepository.findById(id);
        return medicineOpt.filter(medicine -> medicine.getStock() >= quantity).isPresent();
    }
}
