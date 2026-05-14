package com.photostudio.controller;

import com.photostudio.entity.Venue;
import com.photostudio.entity.Venue.VenueStatus;
import com.photostudio.entity.Venue.VenueType;
import com.photostudio.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 场地管理控制器
 * 提供场地管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/venues")
@Tag(name = "场地管理", description = "场地信息的增删改查")
public class VenueController {

    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    @Operation(summary = "获取所有场地", description = "获取系统中所有场地信息")
    public ResponseEntity<List<Venue>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @GetMapping("/available")
    @Operation(summary = "获取可用场地", description = "获取所有可用的场地")
    public ResponseEntity<List<Venue>> getAvailableVenues() {
        return ResponseEntity.ok(venueService.getAvailableVenues());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取场地", description = "根据场地ID获取详细信息")
    public ResponseEntity<Venue> getVenueById(
            @Parameter(description = "场地ID") @PathVariable Long id) {
        return venueService.getVenueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取场地", description = "根据场地类型获取场地列表")
    public ResponseEntity<List<Venue>> getVenuesByType(
            @Parameter(description = "场地类型") @PathVariable VenueType type) {
        return ResponseEntity.ok(venueService.getVenuesByType(type));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取场地", description = "根据场地状态获取场地列表")
    public ResponseEntity<List<Venue>> getVenuesByStatus(
            @Parameter(description = "场地状态") @PathVariable VenueStatus status) {
        return ResponseEntity.ok(venueService.getVenuesByStatus(status));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索场地", description = "根据名称关键词搜索场地")
    public ResponseEntity<List<Venue>> searchVenues(
            @Parameter(description = "名称关键词") @RequestParam String name) {
        return ResponseEntity.ok(venueService.searchVenuesByName(name));
    }

    @PostMapping
    @Operation(summary = "创建场地", description = "创建新的场地信息")
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.createVenue(venue));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新场地", description = "更新场地信息")
    public ResponseEntity<Venue> updateVenue(
            @Parameter(description = "场地ID") @PathVariable Long id,
            @RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.updateVenue(id, venue));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除场地", description = "删除场地信息")
    public ResponseEntity<Void> deleteVenue(
            @Parameter(description = "场地ID") @PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/maintenance")
    @Operation(summary = "设置场地维护状态", description = "设置场地为维护中或正常使用")
    public ResponseEntity<Venue> setMaintenance(
            @Parameter(description = "场地ID") @PathVariable Long id,
            @RequestBody boolean maintenance) {
        return ResponseEntity.ok(venueService.setMaintenance(id, maintenance));
    }
}
