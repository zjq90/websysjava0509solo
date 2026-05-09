package com.production.controller.api;

import com.production.entity.Equipment;
import com.production.entity.EquipmentRecord;
import com.production.service.EquipmentRecordService;
import com.production.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 设备数据API控制器
 * 用于接收硬件设备传来的数据
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentApiController {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentRecordService equipmentRecordService;

    /**
     * 接收设备推送的数据
     * 这是模拟硬件设备通信的接口
     */
    @PostMapping("/{equipmentCode}/data")
    public Map<String, Object> receiveEquipmentData(
            @PathVariable String equipmentCode,
            @RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 查找设备
            Optional<Equipment> equipmentOpt = equipmentService.findByEquipmentCode(equipmentCode);
            if (!equipmentOpt.isPresent()) {
                result.put("success", false);
                result.put("message", "设备不存在：" + equipmentCode);
                return result;
            }
            
            Equipment equipment = equipmentOpt.get();
            
            // 创建设备记录
            EquipmentRecord record = new EquipmentRecord();
            record.setEquipment(equipment);
            record.setDataSource("AUTO");
            
            // 解析数据
            if (data.containsKey("runStatus")) {
                record.setRunStatus((String) data.get("runStatus"));
            } else {
                record.setRunStatus("RUNNING");
            }
            
            if (data.containsKey("currentSpeed")) {
                record.setCurrentSpeed(new java.math.BigDecimal(data.get("currentSpeed").toString()));
            }
            if (data.containsKey("productionOutput")) {
                record.setProductionOutput(new java.math.BigDecimal(data.get("productionOutput").toString()));
            }
            if (data.containsKey("runDuration")) {
                record.setRunDuration(new java.math.BigDecimal(data.get("runDuration").toString()));
            }
            if (data.containsKey("temperature")) {
                record.setTemperature(new java.math.BigDecimal(data.get("temperature").toString()));
            }
            if (data.containsKey("pressure")) {
                record.setPressure(new java.math.BigDecimal(data.get("pressure").toString()));
            }
            if (data.containsKey("alarmMessage")) {
                record.setAlarmMessage((String) data.get("alarmMessage"));
            }
            
            // 保存原始数据
            try {
                record.setRawData(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data));
            } catch (Exception e) {
                record.setRawData(data.toString());
            }
            
            equipmentRecordService.receiveEquipmentData(record);
            
            result.put("success", true);
            result.put("message", "数据接收成功");
            result.put("recordId", record.getId());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "数据处理失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取设备最新状态
     */
    @GetMapping("/{equipmentId}/status")
    public Map<String, Object> getEquipmentStatus(@PathVariable Long equipmentId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Equipment> equipmentOpt = equipmentService.findById(equipmentId);
        if (equipmentOpt.isPresent()) {
            result.put("success", true);
            result.put("equipment", equipmentOpt.get());
            
            Optional<EquipmentRecord> latestRecord = equipmentRecordService.findLatestByEquipmentId(equipmentId);
            latestRecord.ifPresent(record -> result.put("latestRecord", record));
        } else {
            result.put("success", false);
            result.put("message", "设备不存在");
        }
        
        return result;
    }

    /**
     * 获取设备历史数据
     */
    @GetMapping("/{equipmentId}/history")
    public List<EquipmentRecord> getEquipmentHistory(
            @PathVariable Long equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return equipmentRecordService.findByTimeRange(equipmentId, start, end);
    }

    /**
     * 获取所有在线设备
     */
    @GetMapping("/online")
    public List<Equipment> getOnlineEquipments() {
        return equipmentService.findAllOnline();
    }

    /**
     * 获取所有运行中设备
     */
    @GetMapping("/running")
    public List<Equipment> getRunningEquipments() {
        return equipmentService.findAllRunning();
    }
}