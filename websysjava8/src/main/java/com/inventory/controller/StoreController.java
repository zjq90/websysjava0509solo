package com.inventory.controller;

import com.inventory.entity.Store;
import com.inventory.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 门店控制器
 * 功能：提供门店的RESTful API接口
 */
@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public List<Store> findAll() {
        return storeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> findById(@PathVariable Long id) {
        return storeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Store create(@Valid @RequestBody Store store) {
        return storeService.save(store);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> update(@PathVariable Long id, @Valid @RequestBody Store store) {
        return storeService.findById(id)
                .map(existing -> {
                    store.setId(id);
                    return ResponseEntity.ok(storeService.save(store));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (storeService.findById(id).isPresent()) {
            storeService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
