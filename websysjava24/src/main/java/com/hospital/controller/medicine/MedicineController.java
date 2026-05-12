package com.hospital.controller.medicine;

import com.hospital.entity.medicine.Medicine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/medicine")
@Tag(name = "药品管理", description = "药品字典管理接口")
@CrossOrigin(origins = "*")
public class MedicineController {

    private List<Medicine> medicineList = new ArrayList<>();

    public MedicineController() {
        Medicine m1 = new Medicine();
        m1.setId(1L);
        m1.setMedicineCode("MED001");
        m1.setMedicineName("阿莫西林胶囊");
        m1.setGenericName("青霉素类");
        m1.setSpecification("0.25g*24粒");
        m1.setDosageForm("胶囊剂");
        m1.setManufacturer("华北制药");
        m1.setApprovalNumber("国药准字H13021779");
        m1.setPurchasePrice(new BigDecimal("15.50"));
        m1.setRetailPrice(new BigDecimal("25.00"));
        m1.setCategory("抗生素");
        m1.setStatus(1);
        m1.setIsPrescription(true);
        medicineList.add(m1);

        Medicine m2 = new Medicine();
        m2.setId(2L);
        m2.setMedicineCode("MED002");
        m2.setMedicineName("布洛芬缓释胶囊");
        m2.setGenericName("解热镇痛药");
        m2.setSpecification("0.3g*20粒");
        m2.setDosageForm("胶囊剂");
        m2.setManufacturer("中美史克");
        m2.setApprovalNumber("国药准字H10900089");
        m2.setPurchasePrice(new BigDecimal("18.00"));
        m2.setRetailPrice(new BigDecimal("28.50"));
        m2.setCategory("解热镇痛");
        m2.setStatus(1);
        m2.setIsPrescription(false);
        medicineList.add(m2);

        Medicine m3 = new Medicine();
        m3.setId(3L);
        m3.setMedicineCode("MED003");
        m3.setMedicineName("头孢克肟分散片");
        m3.setGenericName("头孢菌素类");
        m3.setSpecification("0.1g*6片");
        m3.setDosageForm("片剂");
        m3.setManufacturer("广州白云山");
        m3.setApprovalNumber("国药准字H10940177");
        m3.setPurchasePrice(new BigDecimal("22.00"));
        m3.setRetailPrice(new BigDecimal("35.00"));
        m3.setCategory("抗生素");
        m3.setStatus(1);
        m3.setIsPrescription(true);
        medicineList.add(m3);

        Medicine m4 = new Medicine();
        m4.setId(4L);
        m4.setMedicineCode("MED004");
        m4.setMedicineName("奥美拉唑肠溶胶囊");
        m4.setGenericName("质子泵抑制剂");
        m4.setSpecification("20mg*14粒");
        m4.setDosageForm("胶囊剂");
        m4.setManufacturer("奥赛康药业");
        m4.setApprovalNumber("国药准字H10950086");
        m4.setPurchasePrice(new BigDecimal("45.00"));
        m4.setRetailPrice(new BigDecimal("68.00"));
        m4.setCategory("消化系统");
        m4.setStatus(1);
        m4.setIsPrescription(true);
        medicineList.add(m4);

        Medicine m5 = new Medicine();
        m5.setId(5L);
        m5.setMedicineCode("MED005");
        m5.setMedicineName("维生素C片");
        m5.setGenericName("维生素类");
        m5.setSpecification("0.1g*100片");
        m5.setDosageForm("片剂");
        m5.setManufacturer("东北制药");
        m5.setApprovalNumber("国药准字H21020713");
        m5.setPurchasePrice(new BigDecimal("3.50"));
        m5.setRetailPrice(new BigDecimal("6.00"));
        m5.setCategory("维生素");
        m5.setStatus(1);
        m5.setIsPrescription(false);
        medicineList.add(m5);
    }

    @PostMapping
    @Operation(summary = "新增药品", description = "新增药品字典信息")
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        medicine.setId((long) (medicineList.size() + 1));
        medicineList.add(medicine);
        return ResponseEntity.ok(medicine);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新药品", description = "更新药品字典信息")
    public ResponseEntity<Medicine> update(
            @Parameter(description = "药品ID") @PathVariable Long id,
            @RequestBody Medicine medicine) {
        for (int i = 0; i < medicineList.size(); i++) {
            if (medicineList.get(i).getId().equals(id)) {
                medicine.setId(id);
                medicineList.set(i, medicine);
                return ResponseEntity.ok(medicine);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除药品", description = "删除药品字典信息")
    public ResponseEntity<Void> delete(@Parameter(description = "药品ID") @PathVariable Long id) {
        medicineList.removeIf(item -> item.getId().equals(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询药品详情", description = "根据ID查询药品详情")
    public ResponseEntity<Medicine> getById(@Parameter(description = "药品ID") @PathVariable Long id) {
        Optional<Medicine> medicine = medicineList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        return medicine.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "根据编码查询药品", description = "根据药品编码查询药品信息")
    public ResponseEntity<Medicine> getByCode(@Parameter(description = "药品编码") @PathVariable String code) {
        Optional<Medicine> medicine = medicineList.stream()
                .filter(item -> item.getMedicineCode().equals(code))
                .findFirst();
        return medicine.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "查询所有药品", description = "查询所有药品列表")
    public ResponseEntity<List<Medicine>> getAll() {
        return ResponseEntity.ok(medicineList);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询药品", description = "分页查询药品列表")
    public ResponseEntity<Map<String, Object>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        int start = page * size;
        int end = Math.min(start + size, medicineList.size());
        List<Medicine> content = medicineList.subList(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", medicineList.size());
        result.put("totalPages", (int) Math.ceil((double) medicineList.size() / size));
        result.put("number", page);
        result.put("size", size);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询药品", description = "根据状态查询药品列表")
    public ResponseEntity<List<Medicine>> getByStatus(@Parameter(description = "状态(0-停用,1-启用)") @PathVariable Integer status) {
        List<Medicine> result = new ArrayList<>();
        for (Medicine medicine : medicineList) {
            if (medicine.getStatus().equals(status)) {
                result.add(medicine);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类查询药品", description = "根据分类查询药品列表")
    public ResponseEntity<List<Medicine>> getByCategory(@Parameter(description = "药品分类") @PathVariable String category) {
        List<Medicine> result = new ArrayList<>();
        for (Medicine medicine : medicineList) {
            if (medicine.getCategory() != null && medicine.getCategory().contains(category)) {
                result.add(medicine);
            }
        }
        return ResponseEntity.ok(result);
    }
}
