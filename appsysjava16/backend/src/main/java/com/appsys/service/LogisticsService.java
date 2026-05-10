package com.appsys.service;

import com.appsys.entity.LogisticsStatus;
import com.appsys.entity.LogisticsTracking;
import com.appsys.exception.ResourceNotFoundException;
import com.appsys.repository.LogisticsTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 物流服务类
 * 处理物流跟踪的增删改查及物流状态更新
 * 集成快递API，模拟实时物流跟踪功能
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Service
public class LogisticsService {

    @Autowired
    private LogisticsTrackingRepository logisticsRepository;

    /**
     * 根据ID获取物流信息
     * @param id 物流记录ID
     * @return 物流信息
     */
    public LogisticsTracking getLogisticsById(Long id) {
        return logisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("物流记录不存在，ID: " + id));
    }

    /**
     * 根据订单ID获取物流信息
     * @param orderId 订单ID
     * @return 物流信息
     */
    public LogisticsTracking getLogisticsByOrderId(Long orderId) {
        return logisticsRepository.findByOrderId(orderId)
                .orElse(null);
    }

    /**
     * 根据快递单号获取物流信息
     * @param trackingNo 快递单号
     * @return 物流信息
     */
    public LogisticsTracking getLogisticsByTrackingNo(String trackingNo) {
        return logisticsRepository.findByTrackingNo(trackingNo)
                .orElseThrow(() -> new ResourceNotFoundException("物流记录不存在，单号: " + trackingNo));
    }

    /**
     * 创建物流记录
     * @param logistics 物流信息
     * @return 创建后的物流记录
     */
    @Transactional
    public LogisticsTracking createLogistics(LogisticsTracking logistics) {
        if (logistics.getStatus() == null) {
            logistics.setStatus(LogisticsStatus.PENDING_SHIPMENT);
        }
        return logisticsRepository.save(logistics);
    }

    /**
     * 更新物流信息
     * @param id 物流记录ID
     * @param logisticsDetails 更新的物流信息
     * @return 更新后的物流记录
     */
    @Transactional
    public LogisticsTracking updateLogistics(Long id, LogisticsTracking logisticsDetails) {
        LogisticsTracking logistics = getLogisticsById(id);
        
        if (logisticsDetails.getCompanyName() != null) {
            logistics.setCompanyName(logisticsDetails.getCompanyName());
        }
        if (logisticsDetails.getCompanyCode() != null) {
            logistics.setCompanyCode(logisticsDetails.getCompanyCode());
        }
        if (logisticsDetails.getTrackingNo() != null) {
            logistics.setTrackingNo(logisticsDetails.getTrackingNo());
        }
        if (logisticsDetails.getStatus() != null) {
            logistics.setStatus(logisticsDetails.getStatus());
        }
        if (logisticsDetails.getCurrentLocation() != null) {
            logistics.setCurrentLocation(logisticsDetails.getCurrentLocation());
        }
        if (logisticsDetails.getTrackingDetails() != null) {
            logistics.setTrackingDetails(logisticsDetails.getTrackingDetails());
        }
        if (logisticsDetails.getEstimatedDeliveryAt() != null) {
            logistics.setEstimatedDeliveryAt(logisticsDetails.getEstimatedDeliveryAt());
        }
        
        return logisticsRepository.save(logistics);
    }

    /**
     * 更新物流状态
     * @param id 物流记录ID
     * @param status 新状态
     * @param location 当前位置
     * @return 更新后的物流记录
     */
    @Transactional
    public LogisticsTracking updateLogisticsStatus(Long id, LogisticsStatus status, String location) {
        LogisticsTracking logistics = getLogisticsById(id);
        
        logistics.setStatus(status);
        if (location != null) {
            logistics.setCurrentLocation(location);
        }
        
        if (status == LogisticsStatus.IN_TRANSIT) {
            addTrackingDetail(logistics, "运输中", location);
        } else if (status == LogisticsStatus.OUT_FOR_DELIVERY) {
            addTrackingDetail(logistics, "派送中", location);
        } else if (status == LogisticsStatus.DELIVERED) {
            addTrackingDetail(logistics, "已签收", location);
        }
        
        return logisticsRepository.save(logistics);
    }

    /**
     * 添加物流跟踪详情
     * @param logistics 物流记录
     * @param status 状态描述
     * @param location 位置描述
     */
    private void addTrackingDetail(LogisticsTracking logistics, String status, String location) {
        String existingDetails = logistics.getTrackingDetails();
        List<Map<String, Object>> detailsList;
        
        if (existingDetails == null || existingDetails.isEmpty()) {
            detailsList = new ArrayList<>();
        } else {
            try {
                detailsList = new ArrayList<>();
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                detailsList = mapper.readValue(existingDetails, List.class);
            } catch (Exception e) {
                detailsList = new ArrayList<>();
            }
        }
        
        Map<String, Object> newDetail = new java.util.HashMap<>();
        newDetail.put("time", LocalDateTime.now().toString());
        newDetail.put("status", status);
        newDetail.put("location", location != null ? location : "");
        detailsList.add(0, newDetail);
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            logistics.setTrackingDetails(mapper.writeValueAsString(detailsList));
        } catch (Exception e) {
            logistics.setTrackingDetails("[]");
        }
    }

    /**
     * 模拟从快递API获取物流信息
     * 实际项目中这里会调用真实的快递API
     * 
     * @param trackingNo 快递单号
     * @return 物流信息（模拟数据）
     */
    public LogisticsTracking simulateFetchFromApi(String trackingNo) {
        LogisticsTracking logistics = getLogisticsByTrackingNo(trackingNo);
        
        List<Map<String, Object>> trackingDetails = new ArrayList<>();
        
        Map<String, Object> detail1 = new java.util.HashMap<>();
        detail1.put("time", LocalDateTime.now().minusDays(2).toString());
        detail1.put("status", "已揽收");
        detail1.put("location", "【北京市】快递员已上门取件");
        trackingDetails.add(detail1);
        
        Map<String, Object> detail2 = new java.util.HashMap<>();
        detail2.put("time", LocalDateTime.now().minusDays(1).toString());
        detail2.put("status", "运输中");
        detail2.put("location", "【北京市】快件已到达【北京转运中心】");
        trackingDetails.add(detail2);
        
        Map<String, Object> detail3 = new java.util.HashMap<>();
        detail3.put("time", LocalDateTime.now().toString());
        detail3.put("status", "运输中");
        detail3.put("location", "【上海市】快件已到达【上海转运中心】");
        trackingDetails.add(detail3);
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            logistics.setTrackingDetails(mapper.writeValueAsString(trackingDetails));
        } catch (Exception e) {
            logistics.setTrackingDetails("[]");
        }
        
        logistics.setCurrentLocation("【上海市】快件已到达【上海转运中心】");
        
        return logisticsRepository.save(logistics);
    }

    /**
     * 删除物流记录
     * @param id 物流记录ID
     */
    @Transactional
    public void deleteLogistics(Long id) {
        LogisticsTracking logistics = getLogisticsById(id);
        logisticsRepository.delete(logistics);
    }
}
