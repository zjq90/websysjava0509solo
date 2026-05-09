package com.inventory.controller;

import com.inventory.entity.Transfer;
import com.inventory.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 调拨控制器
 * 功能：提供调拨的RESTful API接口
 */
@RestController
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping
    public List<Transfer> findAll() {
        return transferService.findAll();
    }

    @GetMapping("/status/{status}")
    public List<Transfer> findByStatus(@PathVariable String status) {
        return transferService.findByStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> findById(@PathVariable Long id) {
        return transferService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Transfer create(@Valid @RequestBody Transfer transfer) {
        return transferService.createTransfer(transfer);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        boolean success = transferService.approveTransfer(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String remark = body != null ? body.get("remark") : null;
        boolean success = transferService.rejectTransfer(id, remark);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (transferService.findById(id).isPresent()) {
            transferService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
