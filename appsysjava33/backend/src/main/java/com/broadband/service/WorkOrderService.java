package com.broadband.service;

import com.broadband.entity.WorkOrder;
import com.broadband.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 工单服务
 * 处理新装、移机、销户等工单管理
 * 
 * @author broadband
 * @version 1.0.0
 */
@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    public List<WorkOrder> getUserOrders(Long userId) {
        return workOrderRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public List<WorkOrder> getUserOrdersByStatus(Long userId, Integer status) {
        return workOrderRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, status);
    }

    public WorkOrder createInstallOrder(Long userId, Long packageId, String broadbandNumber,
                                          String address, LocalDateTime appointmentTime) {
        WorkOrder order = new WorkOrder();
        order.setOrderNo("WO" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setType(1);
        order.setTitle("宽带新装办理");
        order.setPackageId(packageId);
        order.setBroadbandNumber(broadbandNumber);
        order.setNewAddress(address);
        order.setAppointmentTime(appointmentTime);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return workOrderRepository.save(order);
    }

    public WorkOrder createMoveOrder(Long userId, String oldAddress, String newAddress,
                                       String addressProofs, LocalDateTime appointmentTime) {
        WorkOrder order = new WorkOrder();
        order.setOrderNo("WO" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setType(3);
        order.setTitle("宽带移机办理");
        order.setOldAddress(oldAddress);
        order.setNewAddress(newAddress);
        order.setAddressProofs(addressProofs);
        order.setAppointmentTime(appointmentTime);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return workOrderRepository.save(order);
    }

    public WorkOrder createCancelOrder(Long userId, String broadbandNumber, String description) {
        WorkOrder order = new WorkOrder();
        order.setOrderNo("WO" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setType(4);
        order.setTitle("宽带销户办理");
        order.setBroadbandNumber(broadbandNumber);
        order.setDescription(description);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return workOrderRepository.save(order);
    }

    public WorkOrder getOrderById(Long orderId) {
        return workOrderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("工单不存在"));
    }

    public WorkOrder signContract(Long orderId, String contractPath) {
        WorkOrder order = getOrderById(orderId);
        order.setContractPath(contractPath);
        order.setSignStatus(1);
        order.setSignTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return workOrderRepository.save(order);
    }

    public WorkOrder cancelOrder(Long orderId) {
        WorkOrder order = getOrderById(orderId);
        order.setStatus(4);
        order.setUpdateTime(LocalDateTime.now());
        return workOrderRepository.save(order);
    }
}
