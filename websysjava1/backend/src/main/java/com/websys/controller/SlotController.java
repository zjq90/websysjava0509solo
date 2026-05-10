package com.websys.controller;

import com.websys.common.Result;
import com.websys.entity.Slot;
import com.websys.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 货道管理控制器
 * 提供货道管理相关的REST API接口
 */
@RestController
@RequestMapping("/api/slot")
@Tag(name = "货道管理", description = "货道管理相关接口")
public class SlotController {

    @Autowired
    private SlotService slotService;

    @GetMapping("/page")
    @Operation(summary = "分页查询货道列表", description = "根据条件分页查询货道列表")
    public Result<Page<Slot>> getPage(
            @Parameter(description = "设备ID") @RequestParam(required = false) Long deviceId,
            @Parameter(description = "设备编号") @RequestParam(required = false) String deviceCode,
            @Parameter(description = "商品名称") @RequestParam(required = false) String productName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Slot> slotPage = slotService.findPage(deviceId, deviceCode, productName, status, pageRequest);
        return Result.success(slotPage);
    }

    @GetMapping("/device/{deviceId}")
    @Operation(summary = "查询设备所有货道", description = "根据设备ID查询所有货道")
    public Result<List<Slot>> getByDeviceId(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        List<Slot> slots = slotService.findByDeviceId(deviceId);
        return Result.success(slots);
    }

    @GetMapping("/device-code/{deviceCode}")
    @Operation(summary = "根据设备编号查询货道", description = "根据设备编号查询所有货道")
    public Result<List<Slot>> getByDeviceCode(@Parameter(description = "设备编号") @PathVariable String deviceCode) {
        List<Slot> slots = slotService.findByDeviceCode(deviceCode);
        return Result.success(slots);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询货道", description = "根据货道ID查询货道详情")
    public Result<Slot> getById(@Parameter(description = "货道ID") @PathVariable Long id) {
        Optional<Slot> slot = slotService.findById(id);
        return slot.map(Result::success).orElseGet(() -> Result.error("货道不存在"));
    }

    @PostMapping
    @Operation(summary = "新增货道", description = "新增货道信息")
    public Result<Slot> save(@RequestBody Slot slot) {
        Slot saved = slotService.save(slot);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    @Operation(summary = "更新货道", description = "更新货道信息")
    public Result<Slot> update(@RequestBody Slot slot) {
        if (slot.getId() == null) {
            return Result.error("货道ID不能为空");
        }
        Slot updated = slotService.save(slot);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除货道", description = "根据ID删除货道")
    public Result<Void> delete(@Parameter(description = "货道ID") @PathVariable Long id) {
        slotService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/device/{deviceId}")
    @Operation(summary = "删除设备所有货道", description = "根据设备ID删除所有货道")
    public Result<Void> deleteByDeviceId(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        slotService.deleteByDeviceId(deviceId);
        return Result.success("删除成功", null);
    }

    @GetMapping("/out-of-stock/{deviceId}")
    @Operation(summary = "统计缺货货道数", description = "统计设备缺货货道数量")
    public Result<Long> countOutOfStock(@Parameter(description = "设备ID") @PathVariable Long deviceId) {
        long count = slotService.countOutOfStock(deviceId);
        return Result.success(count);
    }

    @PostMapping("/{id}/replenish")
    @Operation(summary = "补货", description = "货道补货操作")
    public Result<Slot> replenish(
            @Parameter(description = "货道ID") @PathVariable Long id,
            @RequestBody Map<String, Integer> params) {
        Integer quantity = params.get("quantity");
        Slot slot = slotService.replenish(id, quantity);
        return Result.success("补货成功", slot);
    }

    @PostMapping("/{id}/dispense")
    @Operation(summary = "出货", description = "货道出货操作")
    public Result<Slot> dispense(@Parameter(description = "货道ID") @PathVariable Long id) {
        Slot slot = slotService.dispense(id);
        return Result.success("出货成功", slot);
    }
}
