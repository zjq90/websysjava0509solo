package com.appsys.service;

import com.appsys.entity.ServiceOrder;
import com.appsys.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    public List<ServiceOrder> findByUserId(Long userId) {
        return serviceOrderRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public Optional<ServiceOrder> findById(Long id) {
        return serviceOrderRepository.findById(id);
    }

    public Optional<ServiceOrder> findByOrderNo(String orderNo) {
        return serviceOrderRepository.findByOrderNo(orderNo);
    }

    public ServiceOrder save(ServiceOrder serviceOrder) {
        if (serviceOrder.getOrderNo() == null) {
            String orderNo = "SO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + new Random().nextInt(1000);
            serviceOrder.setOrderNo(orderNo);
        }
        if (serviceOrder.getStatus() == null) {
            serviceOrder.setStatus("资料审核中");
            serviceOrder.setCurrentStep(1);
        }
        return serviceOrderRepository.save(serviceOrder);
    }

    public ServiceOrder updateStatus(Long id, String status, Integer step) {
        Optional<ServiceOrder> orderOpt = serviceOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            ServiceOrder order = orderOpt.get();
            order.setStatus(status);
            if (step != null) {
                order.setCurrentStep(step);
            }
            if ("已完成".equals(status)) {
                order.setCompleteTime(LocalDateTime.now());
            }
            return serviceOrderRepository.save(order);
        }
        return null;
    }

    public ServiceOrder updateWorkerLocation(Long id, Double lat, Double lng) {
        Optional<ServiceOrder> orderOpt = serviceOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            ServiceOrder order = orderOpt.get();
            order.setWorkerLat(lat);
            order.setWorkerLng(lng);
            return serviceOrderRepository.save(order);
        }
        return null;
    }

    public void deleteById(Long id) {
        serviceOrderRepository.deleteById(id);
    }
}
