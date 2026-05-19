package com.pethospital.service;

import com.pethospital.entity.Medicine;
import com.pethospital.entity.Prescription;
import com.pethospital.entity.PrescriptionItem;
import com.pethospital.repository.MedicineRepository;
import com.pethospital.repository.PrescriptionItemRepository;
import com.pethospital.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionItemRepository prescriptionItemRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Prescription> getDoctorPrescriptions(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    public List<Prescription> getPetPrescriptions(Long petId) {
        return prescriptionRepository.findByPetId(petId);
    }

    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public List<PrescriptionItem> getPrescriptionItems(Long prescriptionId) {
        return prescriptionItemRepository.findByPrescriptionId(prescriptionId);
    }

    @Transactional
    public Prescription createPrescription(Prescription prescription, List<PrescriptionItem> items) {
        String prescriptionNo = "P" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        prescription.setPrescriptionNo(prescriptionNo);
        prescription.setStatus("DRAFT");
        prescription.setCreateTime(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PrescriptionItem item : items) {
            Optional<Medicine> medicineOpt = medicineRepository.findById(item.getMedicineId());
            if (medicineOpt.isPresent()) {
                Medicine medicine = medicineOpt.get();
                if (medicine.getStock() < item.getQuantity()) {
                    throw new RuntimeException("药品库存不足：" + medicine.getName());
                }
                medicine.setStock(medicine.getStock() - item.getQuantity());
                medicineRepository.save(medicine);
            }
            item.setAmount(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            totalAmount = totalAmount.add(item.getAmount());
        }
        prescription.setTotalAmount(totalAmount);

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        for (PrescriptionItem item : items) {
            item.setPrescriptionId(savedPrescription.getId());
            prescriptionItemRepository.save(item);
        }

        return savedPrescription;
    }

    @Transactional
    public Prescription issuePrescription(Long id) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            prescription.setStatus("ISSUED");
            return prescriptionRepository.save(prescription);
        }
        return null;
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }
}
