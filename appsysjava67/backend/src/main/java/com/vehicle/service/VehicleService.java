package com.vehicle.service;

import com.vehicle.dto.PlateCheckResult;
import com.vehicle.entity.Blacklist;
import com.vehicle.entity.Vehicle;
import com.vehicle.repository.BlacklistRepository;
import com.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BlacklistRepository blacklistRepository;

    public VehicleService(VehicleRepository vehicleRepository, BlacklistRepository blacklistRepository) {
        this.vehicleRepository = vehicleRepository;
        this.blacklistRepository = blacklistRepository;
    }

    public PlateCheckResult checkPlate(String plateNumber) {
        PlateCheckResult result = new PlateCheckResult();
        result.setPlateNumber(plateNumber);

        Optional<Blacklist> blacklistOpt = blacklistRepository.findByPlateNumberAndEnabledTrue(plateNumber);
        if (blacklistOpt.isPresent()) {
            Blacklist blacklist = blacklistOpt.get();
            result.setLegal(false);
            result.setMessage("车辆异常");
            result.setRiskLevel(blacklist.getRiskLevel());
            result.setViolationReason(blacklist.getReason());
            result.setViolationDescription(blacklist.getDescription());
        } else {
            result.setLegal(true);
            result.setMessage("车辆合法");
            result.setRiskLevel("LOW");
        }

        Optional<Vehicle> vehicleOpt = vehicleRepository.findByPlateNumber(plateNumber);
        vehicleOpt.ifPresent(vehicle -> {
            result.setBrand(vehicle.getBrand());
            result.setModel(vehicle.getModel());
            result.setColor(vehicle.getColor());
            result.setOwnerName(vehicle.getOwnerName());
        });

        return result;
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> findByPlateNumber(String plateNumber) {
        return vehicleRepository.findByPlateNumber(plateNumber);
    }
}
