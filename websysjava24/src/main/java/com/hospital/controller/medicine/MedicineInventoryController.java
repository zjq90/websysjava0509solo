package com.hospital.controller.medicine;

import com.hospital.entity.medicine.MedicineInventory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicine/inventory")
@Tag(name = "药品库存管理", description = "药品库存管理接口")
@CrossOrigin(origins = "*")
public class MedicineInventoryController {

    private List<MedicineInventory> inventoryList = new ArrayList<>();

    public MedicineInventoryController() {
        MedicineInventory inv1 = new MedicineInventory();
        inv1.setId(1L);
        inv1.setMedicineId(1L);
        inv1.setMedicineCode("MED001");
        inv1.setMedicineName("阿莫西林胶囊");
        inv1.setSpecification("0.25g*24粒");
        inv1.setBatchNumber("BATCH001");
        inv1.setProductionDate(LocalDate.now().minusMonths(3));
        inv1.setExpiryDate(LocalDate.now().plusMonths(21));
        inv1.setQuantity(500);
        inv1.setUnit("盒");
        inv1.setWarehouseLocation("A区-001");
        inv1.setInventoryStatus(0);
        inventoryList.add(inv1);

        MedicineInventory inv2 = new MedicineInventory();
        inv2.setId(2L);
        inv2.setMedicineId(2L);
        inv2.setMedicineCode("MED002");
        inv2.setMedicineName("布洛芬缓释胶囊");
        inv2.setSpecification("0.3g*20粒");
        inv2.setBatchNumber("BATCH002");
        inv2.setProductionDate(LocalDate.now().minusMonths(2));
        inv2.setExpiryDate(LocalDate.now().plusMonths(22));
        inv2.setQuantity(300);
        inv2.setUnit("盒");
        inv2.setWarehouseLocation("A区-002");
        inv2.setInventoryStatus(0);
        inventoryList.add(inv2);
    }

    @GetMapping
    @Operation(summary = "查询所有药品库存", description = "查询所有药品库存列表")
    public ResponseEntity<List<MedicineInventory>> getAll() {
        return ResponseEntity.ok(inventoryList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询库存", description = "根据ID查询药品库存详情")
    public ResponseEntity<MedicineInventory> getById(@PathVariable Long id) {
        Optional<MedicineInventory> inventory = inventoryList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        return inventory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "新增药品库存", description = "新增药品库存记录")
    public ResponseEntity<MedicineInventory> create(@RequestBody MedicineInventory inventory) {
        inventory.setId((long) (inventoryList.size() + 1));
        inventoryList.add(inventory);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新药品库存", description = "更新药品库存信息")
    public ResponseEntity<MedicineInventory> update(@PathVariable Long id, @RequestBody MedicineInventory inventory) {
        for (int i = 0; i < inventoryList.size(); i++) {
            if (inventoryList.get(i).getId().equals(id)) {
                inventory.setId(id);
                inventoryList.set(i, inventory);
                return ResponseEntity.ok(inventory);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除药品库存", description = "删除药品库存记录")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryList.removeIf(item -> item.getId().equals(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/medicine/{medicineId}")
    @Operation(summary = "根据药品ID查询库存", description = "根据药品ID查询库存列表")
    public ResponseEntity<List<MedicineInventory>> getByMedicineId(@PathVariable Long medicineId) {
        List<MedicineInventory> result = new ArrayList<>();
        for (MedicineInventory inventory : inventoryList) {
            if (inventory.getMedicineId().equals(medicineId)) {
                result.add(inventory);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/near-expiry")
    @Operation(summary = "查询近效期药品", description = "查询3个月内到期的药品")
    public ResponseEntity<List<MedicineInventory>> getNearExpiry() {
        LocalDate warningDate = LocalDate.now().plusMonths(3);
        List<MedicineInventory> result = new ArrayList<>();
        for (MedicineInventory inventory : inventoryList) {
            if (inventory.getExpiryDate() != null && inventory.getExpiryDate().isBefore(warningDate)) {
                result.add(inventory);
            }
        }
        return ResponseEntity.ok(result);
    }
}
