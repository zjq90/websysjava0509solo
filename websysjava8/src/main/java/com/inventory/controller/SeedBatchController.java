package com.inventory.controller;

import com.inventory.entity.SeedBatch;
import com.inventory.service.SeedBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 种子批次控制器
 * 功能：提供种子批次的RESTful API接口
 */
@RestController
@RequestMapping("/api/batches")
@CrossOrigin(origins = "*")
public class SeedBatchController {

    @Autowired
    private SeedBatchService seedBatchService;

    @GetMapping
    public List<SeedBatch> findAll() {
        return seedBatchService.findAll();
    }

    @GetMapping("/variety/{varietyId}")
    public List<SeedBatch> findByVarietyId(@PathVariable Long varietyId) {
        return seedBatchService.findByVarietyId(varietyId);
    }

    @GetMapping("/expiring/{days}")
    public List<SeedBatch> findExpiring(@PathVariable int days) {
        return seedBatchService.findExpiringBatches(days);
    }

    @GetMapping("/expired")
    public List<SeedBatch> findExpired() {
        return seedBatchService.findExpiredBatches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeedBatch> findById(@PathVariable Long id) {
        return seedBatchService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SeedBatch create(@Valid @RequestBody SeedBatch seedBatch) {
        return seedBatchService.save(seedBatch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeedBatch> update(@PathVariable Long id, @Valid @RequestBody SeedBatch seedBatch) {
        return seedBatchService.findById(id)
                .map(existing -> {
                    seedBatch.setId(id);
                    return ResponseEntity.ok(seedBatchService.save(seedBatch));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (seedBatchService.findById(id).isPresent()) {
            seedBatchService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
