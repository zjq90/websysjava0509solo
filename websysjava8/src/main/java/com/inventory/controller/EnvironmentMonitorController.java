package com.inventory.controller;

import com.inventory.entity.EnvironmentMonitor;
import com.inventory.service.EnvironmentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 温湿度监控控制器
 * 功能：提供温湿度监控的RESTful API接口
 */
@RestController
@RequestMapping("/api/environment")
@CrossOrigin(origins = "*")
public class EnvironmentMonitorController {

    @Autowired
    private EnvironmentMonitorService environmentMonitorService;

    @GetMapping
    public List<EnvironmentMonitor> findAll() {
        return environmentMonitorService.findAll();
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<EnvironmentMonitor> findByWarehouseId(@PathVariable Long warehouseId) {
        return environmentMonitorService.findByWarehouseId(warehouseId);
    }

    @GetMapping("/store/{storeId}")
    public List<EnvironmentMonitor> findByStoreId(@PathVariable Long storeId) {
        return environmentMonitorService.findByStoreId(storeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvironmentMonitor> findById(@PathVariable Long id) {
        return environmentMonitorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EnvironmentMonitor create(@Valid @RequestBody EnvironmentMonitor monitor) {
        return environmentMonitorService.save(monitor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (environmentMonitorService.findById(id).isPresent()) {
            environmentMonitorService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
