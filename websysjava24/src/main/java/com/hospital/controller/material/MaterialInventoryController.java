package com.hospital.controller.material;

import com.hospital.entity.material.MaterialInventory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/material/inventory")
@Tag(name = "物资库存管理", description = "物资库存管理接口")
@CrossOrigin(origins = "*")
public class MaterialInventoryController {

    private List<MaterialInventory> inventoryList = new ArrayList<>();

    public MaterialInventoryController() {
        MaterialInventory inv1 = new MaterialInventory();
        inv1.setId(1L);
        inv1.setMaterialId(1L);
        inv1.setMaterialCode("MAT001");
        inv1.setMaterialName("一次性使用无菌注射器");
        inv1.setSpecification("5ml");
        inv1.setBatchNumber("BATCH001");
        inv1.setProductionDate(LocalDate.now().minusMonths(3));
        inv1.setExpiryDate(LocalDate.now().plusMonths(33));
        inv1.setQuantity(2000);
        inv1.setUnit("支");
        inv1.setWarehouseLocation("B区-001");
        inv1.setInventoryStatus(0);
        inv1.setIsHighValue(false);
        inventoryList.add(inv1);

        MaterialInventory inv2 = new MaterialInventory();
        inv2.setId(2L);
        inv2.setMaterialId(4L);
        inv2.setMaterialCode("MAT004");
        inv2.setMaterialName("心脏支架");
        inv2.setSpecification("药物洗脱支架");
        inv2.setBatchNumber("BATCH004");
        inv2.setUniqueIdentifier("UID-SN2024001");
        inv2.setProductionDate(LocalDate.now().minusMonths(1));
        inv2.setExpiryDate(LocalDate.now().plusMonths(23));
        inv2.setQuantity(50);
        inv2.setUnit("个");
        inv2.setWarehouseLocation("C区-001");
        inv2.setInventoryStatus(0);
        inv2.setIsHighValue(true);
        inventoryList.add(inv2);
    }

    @GetMapping
    @Operation(summary = "查询所有物资库存", description = "查询所有物资库存列表")
    public ResponseEntity<List<MaterialInventory>> getAll() {
        return ResponseEntity.ok(inventoryList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询库存", description = "根据ID查询物资库存详情")
    public ResponseEntity<MaterialInventory> getById(@PathVariable Long id) {
        Optional<MaterialInventory> inventory = inventoryList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        return inventory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "新增物资库存", description = "新增物资库存记录")
    public ResponseEntity<MaterialInventory> create(@RequestBody MaterialInventory inventory) {
        inventory.setId((long) (inventoryList.size() + 1));
        inventoryList.add(inventory);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新物资库存", description = "更新物资库存信息")
    public ResponseEntity<MaterialInventory> update(@PathVariable Long id, @RequestBody MaterialInventory inventory) {
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
    @Operation(summary = "删除物资库存", description = "删除物资库存记录")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryList.removeIf(item -> item.getId().equals(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/material/{materialId}")
    @Operation(summary = "根据物资ID查询库存", description = "根据物资ID查询库存列表")
    public ResponseEntity<List<MaterialInventory>> getByMaterialId(@PathVariable Long materialId) {
        List<MaterialInventory> result = new ArrayList<>();
        for (MaterialInventory inventory : inventoryList) {
            if (inventory.getMaterialId().equals(materialId)) {
                result.add(inventory);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/high-value")
    @Operation(summary = "查询高值耗材库存", description = "查询所有高值耗材库存列表")
    public ResponseEntity<List<MaterialInventory>> getHighValueMaterials() {
        List<MaterialInventory> result = new ArrayList<>();
        for (MaterialInventory inventory : inventoryList) {
            if (inventory.getUniqueIdentifier() != null && !inventory.getUniqueIdentifier().isEmpty()) {
                result.add(inventory);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/near-expiry")
    @Operation(summary = "查询近效期物资", description = "查询3个月内到期的物资")
    public ResponseEntity<List<MaterialInventory>> getNearExpiry() {
        LocalDate warningDate = LocalDate.now().plusMonths(3);
        List<MaterialInventory> result = new ArrayList<>();
        for (MaterialInventory inventory : inventoryList) {
            if (inventory.getExpiryDate() != null && inventory.getExpiryDate().isBefore(warningDate)) {
                result.add(inventory);
            }
        }
        return ResponseEntity.ok(result);
    }
}
