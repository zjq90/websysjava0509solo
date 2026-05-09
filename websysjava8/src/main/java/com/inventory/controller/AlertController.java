package com.inventory.controller;

import com.inventory.entity.Alert;
import com.inventory.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预警控制器
 * 功能：提供预警的RESTful API接口
 */
@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public List<Alert> findAll() {
        return alertService.findAll();
    }

    @GetMapping("/active")
    public List<Alert> findActive() {
        return alertService.findActiveAlerts();
    }

    @GetMapping("/type/{type}")
    public List<Alert> findByType(@PathVariable String type) {
        return alertService.findByType(type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> findById(@PathVariable Long id) {
        return alertService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolve(@PathVariable Long id) {
        boolean success = alertService.resolveAlert(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (alertService.findById(id).isPresent()) {
            alertService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
