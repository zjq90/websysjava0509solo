package com.inventory.controller;

import com.inventory.entity.Inventory;
import com.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 库存控制器
 * 功能：提供库存的RESTful API接口
 */
@RestController
@RequestMapping("/api/inventories")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<Inventory> findAll() {
        return inventoryService.findAll();
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<Inventory> findByWarehouseId(@PathVariable Long warehouseId) {
        return inventoryService.findByWarehouseId(warehouseId);
    }

    @GetMapping("/store/{storeId}")
    public List<Inventory> findByStoreId(@PathVariable Long storeId) {
        return inventoryService.findByStoreId(storeId);
    }

    @GetMapping("/expiring/{days}")
    public List<Inventory> findExpiring(@PathVariable int days) {
        return inventoryService.findExpiringInventory(days);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable Long id) {
        return inventoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inventory create(@Valid @RequestBody Inventory inventory) {
        return inventoryService.save(inventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(@PathVariable Long id, @Valid @RequestBody Inventory inventory) {
        return inventoryService.findById(id)
                .map(existing -> {
                    inventory.setId(id);
                    return ResponseEntity.ok(inventoryService.save(inventory));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inventoryService.findById(id).isPresent()) {
            inventoryService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
