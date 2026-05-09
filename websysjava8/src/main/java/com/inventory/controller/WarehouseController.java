package com.inventory.controller;

import com.inventory.entity.Warehouse;
import com.inventory.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 仓库控制器
 * 功能：提供仓库的RESTful API接口
 */
@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin(origins = "*")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping
    public List<Warehouse> findAll() {
        return warehouseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> findById(@PathVariable Long id) {
        return warehouseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Warehouse create(@Valid @RequestBody Warehouse warehouse) {
        return warehouseService.save(warehouse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> update(@PathVariable Long id, @Valid @RequestBody Warehouse warehouse) {
        return warehouseService.findById(id)
                .map(existing -> {
                    warehouse.setId(id);
                    return ResponseEntity.ok(warehouseService.save(warehouse));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (warehouseService.findById(id).isPresent()) {
            warehouseService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
