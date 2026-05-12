package com.hospital.service.medicine;

import com.hospital.entity.medicine.Medicine;
import com.hospital.repository.medicine.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void deleteById(Long id) {
        medicineRepository.deleteById(id);
    }

    public Optional<Medicine> findById(Long id) {
        return medicineRepository.findById(id);
    }

    public Optional<Medicine> findByMedicineCode(String medicineCode) {
        return medicineRepository.findByMedicineCode(medicineCode);
    }

    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    public Page<Medicine> findAll(Pageable pageable) {
        return medicineRepository.findAll(pageable);
    }

    public List<Medicine> findByStatus(Integer status) {
        return medicineRepository.findByStatus(status);
    }

    public List<Medicine> findByCategory(String category) {
        return medicineRepository.findByCategory(category);
    }

    public boolean existsByMedicineCode(String medicineCode) {
        return medicineRepository.existsByMedicineCode(medicineCode);
    }

    public Medicine update(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
}
