package com.photostudio.controller;

import com.photostudio.entity.VenueBooking;
import com.photostudio.entity.VenueBooking.BookingStatus;
import com.photostudio.service.VenueBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 场地预约控制器
 * 提供场地预约管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/venue-bookings")
@Tag(name = "场地预约", description = "场地预约的管理与冲突检测")
public class VenueBookingController {

    private final VenueBookingService venueBookingService;

    @Autowired
    public VenueBookingController(VenueBookingService venueBookingService) {
        this.venueBookingService = venueBookingService;
    }

    @GetMapping
    @Operation(summary = "获取所有场地预约", description = "获取系统中所有场地预约")
    public ResponseEntity<List<VenueBooking>> getAllBookings() {
        return ResponseEntity.ok(venueBookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取预约", description = "根据预约ID获取详细信息")
    public ResponseEntity<VenueBooking> getBookingById(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        return venueBookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/venue/{venueId}")
    @Operation(summary = "根据场地获取预约", description = "获取指定场地的预约列表")
    public ResponseEntity<List<VenueBooking>> getBookingsByVenue(
            @Parameter(description = "场地ID") @PathVariable Long venueId) {
        return ResponseEntity.ok(venueBookingService.getBookingsByVenue(venueId));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "根据日期获取预约", description = "获取指定日期的场地预约")
    public ResponseEntity<List<VenueBooking>> getBookingsByDate(
            @Parameter(description = "日期") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(venueBookingService.getBookingsByDate(date));
    }

    @GetMapping("/venue/{venueId}/date/{date}")
    @Operation(summary = "获取场地在指定日期的预约", description = "获取指定场地在指定日期的预约")
    public ResponseEntity<List<VenueBooking>> getVenueBookingsByDate(
            @Parameter(description = "场地ID") @PathVariable Long venueId,
            @Parameter(description = "日期") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(venueBookingService.getVenueBookingsByDate(venueId, date));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取预约", description = "获取指定状态的场地预约")
    public ResponseEntity<List<VenueBooking>> getBookingsByStatus(
            @Parameter(description = "预约状态") @PathVariable BookingStatus status) {
        return ResponseEntity.ok(venueBookingService.getBookingsByStatus(status));
    }

    @PostMapping
    @Operation(summary = "创建场地预约", description = "创建新的场地预约")
    public ResponseEntity<VenueBooking> createBooking(@RequestBody VenueBooking booking) {
        return ResponseEntity.ok(venueBookingService.createBooking(booking));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新场地预约", description = "更新场地预约信息")
    public ResponseEntity<VenueBooking> updateBooking(
            @Parameter(description = "预约ID") @PathVariable Long id,
            @RequestBody VenueBooking booking) {
        return ResponseEntity.ok(venueBookingService.updateBooking(id, booking));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除场地预约", description = "删除场地预约")
    public ResponseEntity<Void> deleteBooking(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        venueBookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成预约", description = "标记预约为已完成")
    public ResponseEntity<VenueBooking> completeBooking(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        return ResponseEntity.ok(venueBookingService.completeBooking(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "取消场地预约")
    public ResponseEntity<VenueBooking> cancelBooking(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        return ResponseEntity.ok(venueBookingService.cancelBooking(id));
    }

    @PostMapping("/check-conflict")
    @Operation(summary = "检查预约冲突", description = "检查场地在指定时间是否有预约冲突")
    public ResponseEntity<Map<String, Boolean>> checkConflict(@RequestBody Map<String, Object> request) {
        Long venueId = Long.parseLong(request.get("venueId").toString());
        LocalDate date = LocalDate.parse(request.get("date").toString());
        String startTimeStr = request.get("startTime").toString();
        String endTimeStr = request.get("endTime").toString();
        
        java.time.LocalTime startTime = java.time.LocalTime.parse(startTimeStr);
        java.time.LocalTime endTime = java.time.LocalTime.parse(endTimeStr);
        
        boolean hasConflict = venueBookingService.checkConflict(venueId, date, startTime, endTime);
        return ResponseEntity.ok(Collections.singletonMap("hasConflict", hasConflict));
    }
}
