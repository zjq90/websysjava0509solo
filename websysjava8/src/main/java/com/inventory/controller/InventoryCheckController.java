package com.inventory.controller;

import com.inventory.entity.InventoryCheck;
import com.inventory.service.InventoryCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 盘点控制器
 * 功能：提供盘点的RESTful API接口
 */
@RestController
@RequestMapping("/api/checks")
@CrossOrigin(origins = "*")
public class InventoryCheckController {

    @Autowired
    private InventoryCheckService inventoryCheckService;

    @GetMapping
    public List<InventoryCheck> findAll() {
        return inventoryCheckService.findAll();
    }

    @GetMapping("/inventory/{inventoryId}")
    public List<InventoryCheck> findByInventoryId(@PathVariable Long inventoryId) {
        return inventoryCheckService.findByInventoryId(inventoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryCheck> findById(@PathVariable Long id) {
        return inventoryCheckService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryCheck> create(@RequestBody Map<String, Object> request) {
        Long inventoryId = Long.valueOf(request.get("inventoryId").toString());
        Integer actualQuantity = Integer.valueOf(request.get("actualQuantity").toString());
        String remark = request.get("remark") != null ? request.get("remark").toString() : null;
        InventoryCheck check = inventoryCheckService.createCheck(inventoryId, actualQuantity, remark);
        return check != null ? ResponseEntity.ok(check) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inventoryCheckService.findById(id).isPresent()) {
            inventoryCheckService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
