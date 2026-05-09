package com.inventory.controller;

import com.inventory.entity.Variety;
import com.inventory.service.VarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 品种控制器
 * 功能：提供品种的RESTful API接口
 */
@RestController
@RequestMapping("/api/varieties")
@CrossOrigin(origins = "*")
public class VarietyController {

    @Autowired
    private VarietyService varietyService;

    @GetMapping
    public List<Variety> findAll() {
        return varietyService.findAll();
    }

    @GetMapping("/category/{categoryId}")
    public List<Variety> findByCategoryId(@PathVariable Long categoryId) {
        return varietyService.findByCategoryId(categoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variety> findById(@PathVariable Long id) {
        return varietyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Variety create(@Valid @RequestBody Variety variety) {
        return varietyService.save(variety);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Variety> update(@PathVariable Long id, @Valid @RequestBody Variety variety) {
        return varietyService.findById(id)
                .map(existing -> {
                    variety.setId(id);
                    return ResponseEntity.ok(varietyService.save(variety));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (varietyService.findById(id).isPresent()) {
            varietyService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
