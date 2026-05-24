package com.vehicle.controller;

import com.vehicle.common.Result;
import com.vehicle.dto.PlateCheckResult;
import com.vehicle.entity.Vehicle;
import com.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vehicle")
@Tag(name = "车辆管理", description = "车辆信息查询和车牌查验接口")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/check/{plateNumber}")
    @Operation(summary = "查验车牌", description = "扫描车牌后调用此接口查验车辆是否在黑名单中")
    public Result<PlateCheckResult> checkPlate(
            @Parameter(description = "车牌号", required = true)
            @PathVariable String plateNumber) {
        PlateCheckResult result = vehicleService.checkPlate(plateNumber);
        return Result.success(result);
    }

    @GetMapping("/{plateNumber}")
    @Operation(summary = "查询车辆信息", description = "根据车牌号查询车辆详细信息")
    public Result<Vehicle> getVehicle(
            @Parameter(description = "车牌号", required = true)
            @PathVariable String plateNumber) {
        Optional<Vehicle> vehicle = vehicleService.findByPlateNumber(plateNumber);
        return vehicle.map(Result::success)
                .orElse(Result.error("车辆信息不存在"));
    }

    @PostMapping
    @Operation(summary = "新增车辆信息", description = "录入新的车辆信息")
    public Result<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleService.saveVehicle(vehicle);
        return Result.success("车辆信息录入成功", saved);
    }
}
