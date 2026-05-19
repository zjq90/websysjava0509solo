package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Prescription;
import com.pethospital.entity.PrescriptionItem;
import com.pethospital.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prescription")
@Tag(name = "处方管理", description = "电子处方开具、查询等接口")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "获取医生处方列表")
    public Result<List<Prescription>> getDoctorPrescriptions(@PathVariable Long doctorId) {
        return Result.success(prescriptionService.getDoctorPrescriptions(doctorId));
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "获取宠物处方历史")
    public Result<List<Prescription>> getPetPrescriptions(@PathVariable Long petId) {
        return Result.success(prescriptionService.getPetPrescriptions(petId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取处方详情")
    public Result<Prescription> getPrescriptionById(@PathVariable Long id) {
        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
        return prescription.map(Result::success).orElseGet(() -> Result.error("处方不存在"));
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "获取处方药品明细")
    public Result<List<PrescriptionItem>> getPrescriptionItems(@PathVariable Long id) {
        return Result.success(prescriptionService.getPrescriptionItems(id));
    }

    @PostMapping
    @Operation(summary = "创建处方")
    public Result<Prescription> createPrescription(@RequestBody Map<String, Object> params) {
        try {
            Prescription prescription = new Prescription();
            prescription.setConsultationId(params.get("consultationId") != null ? Long.valueOf(params.get("consultationId").toString()) : null);
            prescription.setDoctorId(Long.valueOf(params.get("doctorId").toString()));
            prescription.setPetId(Long.valueOf(params.get("petId").toString()));
            prescription.setOwnerId(Long.valueOf(params.get("ownerId").toString()));
            prescription.setNotes(params.get("notes") != null ? params.get("notes").toString() : null);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) params.get("items");
            List<PrescriptionItem> items = itemsData.stream().map(item -> {
                PrescriptionItem pi = new PrescriptionItem();
                pi.setMedicineId(Long.valueOf(item.get("medicineId").toString()));
                pi.setMedicineName(item.get("medicineName").toString());
                pi.setSpecification(item.get("specification") != null ? item.get("specification").toString() : null);
                pi.setQuantity(Integer.valueOf(item.get("quantity").toString()));
                pi.setUnit(item.get("unit") != null ? item.get("unit").toString() : null);
                pi.setPrice(new java.math.BigDecimal(item.get("price").toString()));
                pi.setDosage(item.get("dosage") != null ? item.get("dosage").toString() : null);
                pi.setFrequency(item.get("frequency") != null ? item.get("frequency").toString() : null);
                pi.setDuration(item.get("duration") != null ? item.get("duration").toString() : null);
                pi.setNotes(item.get("notes") != null ? item.get("notes").toString() : null);
                return pi;
            }).collect(Collectors.toList());

            return Result.success(prescriptionService.createPrescription(prescription, items));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/issue")
    @Operation(summary = "开具处方")
    public Result<Prescription> issuePrescription(@PathVariable Long id) {
        Prescription prescription = prescriptionService.issuePrescription(id);
        if (prescription != null) {
            return Result.success(prescription);
        }
        return Result.error("处方不存在");
    }

    @GetMapping
    @Operation(summary = "获取所有处方")
    public Result<List<Prescription>> getAllPrescriptions() {
        return Result.success(prescriptionService.getAllPrescriptions());
    }
}
