package com.photostudio.controller;

import com.photostudio.entity.CostumeBooking;
import com.photostudio.entity.CostumeBooking.CostumeBookingStatus;
import com.photostudio.service.CostumeBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服装预约控制器
 * 提供服装预约管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/costume-bookings")
@Tag(name = "服装预约", description = "服装借用与归还管理")
public class CostumeBookingController {

    private final CostumeBookingService costumeBookingService;

    @Autowired
    public CostumeBookingController(CostumeBookingService costumeBookingService) {
        this.costumeBookingService = costumeBookingService;
    }

    @GetMapping
    @Operation(summary = "获取所有服装预约", description = "获取系统中所有服装预约")
    public ResponseEntity<List<CostumeBooking>> getAllBookings() {
        return ResponseEntity.ok(costumeBookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取预约", description = "根据预约ID获取详细信息")
    public ResponseEntity<CostumeBooking> getBookingById(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        return costumeBookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/costume/{costumeId}")
    @Operation(summary = "根据服装获取预约", description = "获取指定服装的预约列表")
    public ResponseEntity<List<CostumeBooking>> getBookingsByCostume(
            @Parameter(description = "服装ID") @PathVariable Long costumeId) {
        return ResponseEntity.ok(costumeBookingService.getBookingsByCostume(costumeId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取预约", description = "获取指定状态的服装预约")
    public ResponseEntity<List<CostumeBooking>> getBookingsByStatus(
            @Parameter(description = "预约状态") @PathVariable CostumeBookingStatus status) {
        return ResponseEntity.ok(costumeBookingService.getBookingsByStatus(status));
    }

    @PostMapping
    @Operation(summary = "创建服装预约", description = "创建新的服装预约")
    public ResponseEntity<CostumeBooking> createBooking(@RequestBody CostumeBooking booking) {
        return ResponseEntity.ok(costumeBookingService.createBooking(booking));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新服装预约", description = "更新服装预约信息")
    public ResponseEntity<CostumeBooking> updateBooking(
            @Parameter(description = "预约ID") @PathVariable Long id,
            @RequestBody CostumeBooking booking) {
        return ResponseEntity.ok(costumeBookingService.updateBooking(id, booking));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除服装预约", description = "删除服装预约")
    public ResponseEntity<Void> deleteBooking(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        costumeBookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "归还服装", description = "标记服装已归还")
    public ResponseEntity<CostumeBooking> returnCostume(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        return ResponseEntity.ok(costumeBookingService.returnCostume(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "取消服装预约")
    public ResponseEntity<CostumeBooking> cancelBooking(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        return ResponseEntity.ok(costumeBookingService.cancelBooking(id));
    }
}
