package com.ops.service;

import com.ops.entity.Technician;
import com.ops.entity.WorkOrder;
import com.ops.repository.TechnicianRepository;
import com.ops.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 工单服务类
 * 提供工单的创建、分配、状态管理等功能
 * 
 * @author ops-admin
 */
@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    /**
     * 创建工单
     */
    @Transactional
    public WorkOrder createWorkOrder(WorkOrder workOrder) {
        String orderNo = "WO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
                + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        workOrder.setOrderNo(orderNo);
        return workOrderRepository.save(workOrder);
    }

    /**
     * 智能派单
     * 根据用户地址、装维人员位置、负载情况进行智能分配
     * 优先级：1.工作区域匹配  2.距离  3.负载情况  4.服务评分
     */
    @Transactional
    public WorkOrder autoAssign(Long orderId) {
        Optional<WorkOrder> orderOpt = workOrderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("工单不存在");
        }

        WorkOrder workOrder = orderOpt.get();
        if (!"PENDING".equals(workOrder.getStatus())) {
            throw new RuntimeException("工单状态不正确，无法分配");
        }

        List<Technician> availableTechnicians = technicianRepository
                .findByStatusAndCurrentLoadLessThan("ONLINE", 10);

        if (availableTechnicians.isEmpty()) {
            throw new RuntimeException("当前没有可用的装维人员");
        }

        Technician bestTechnician = findBestTechnician(workOrder, availableTechnicians);

        if (bestTechnician == null) {
            throw new RuntimeException("未找到合适的装维人员");
        }

        workOrder.setTechnicianId(bestTechnician.getId());
        workOrder.setTechnicianName(bestTechnician.getName());
        workOrder.setTechnicianPhone(bestTechnician.getPhone());
        workOrder.setStatus("ASSIGNED");
        workOrder.setAssignTime(LocalDateTime.now());

        bestTechnician.setCurrentLoad(bestTechnician.getCurrentLoad() + 1);
        technicianRepository.save(bestTechnician);

        return workOrderRepository.save(workOrder);
    }

    /**
     * 选择最优的装维人员
     * 算法逻辑：
     * 1. 优先匹配工作区域完全一致的装维人员
     * 2. 其次匹配相邻区域或地址包含关键词的
     * 3. 最后综合考虑距离、负载、评分
     */
    private Technician findBestTechnician(WorkOrder workOrder, List<Technician> technicians) {
        Technician best = null;
        double bestScore = -1;

        for (Technician tech : technicians) {
            double score = calculateScore(workOrder, tech);
            if (score > bestScore) {
                bestScore = score;
                best = tech;
            }
        }

        return best;
    }

    /**
     * 计算装维人员的匹配分数
     * 考虑因素权重：
     * - 区域匹配：40%（最重要）
     * - 负载情况：30%
     * - 服务评分：20%
     * - 地理距离：10%
     */
    private double calculateScore(WorkOrder workOrder, Technician technician) {
        double areaMatchScore = calculateAreaMatchScore(workOrder, technician);
        double loadScore = 100 - (technician.getCurrentLoad() * 10);
        double ratingScore = technician.getRating() * 20;
        double distanceScore = calculateDistanceScore(workOrder, technician);

        return areaMatchScore * 0.4 + loadScore * 0.3 + ratingScore * 0.2 + distanceScore * 0.1;
    }

    /**
     * 计算区域匹配分数（核心算法修复）
     * 1. 完全匹配：100分（如丰台区任务匹配丰台区师傅）
     * 2. 部分匹配：60分（地址包含关键词）
     * 3. 不匹配：0分
     */
    private double calculateAreaMatchScore(WorkOrder workOrder, Technician technician) {
        String workOrderAddress = workOrder.getUserAddress();
        String techWorkArea = technician.getWorkArea();

        if (workOrderAddress == null || techWorkArea == null) {
            return 50;
        }

        if (workOrderAddress.contains(techWorkArea)) {
            return 100;
        }

        if (isAdjacentArea(workOrderAddress, techWorkArea)) {
            return 60;
        }

        return 0;
    }

    /**
     * 判断是否为相邻区域
     */
    private boolean isAdjacentArea(String orderAddress, String techArea) {
        String[] adjacentAreas = {
            "朝阳区-东城区", "朝阳区-丰台区", "朝阳区-海淀区",
            "海淀区-丰台区", "海淀区-西城区", "东城区-西城区",
            "丰台区-东城区"
        };

        for (String areaPair : adjacentAreas) {
            String[] areas = areaPair.split("-");
            if ((orderAddress.contains(areas[0]) && techArea.equals(areas[1])) ||
                (orderAddress.contains(areas[1]) && techArea.equals(areas[0]))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算地理距离分数（辅助因素，权重降低）
     */
    private double calculateDistanceScore(WorkOrder workOrder, Technician technician) {
        if (workOrder.getLatitude() == null || workOrder.getLongitude() == null
                || technician.getLatitude() == null || technician.getLongitude() == null) {
            return 50;
        }

        double distance = calculateDistance(
                workOrder.getLatitude(), workOrder.getLongitude(),
                technician.getLatitude(), technician.getLongitude()
        );

        return Math.max(0, 100 - distance * 50);
    }

    /**
     * 计算两点之间的欧几里得距离
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }

    /**
     * 接单
     */
    @Transactional
    public WorkOrder acceptOrder(Long orderId) {
        WorkOrder workOrder = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        workOrder.setStatus("IN_PROGRESS");
        workOrder.setAcceptTime(LocalDateTime.now());
        
        if (workOrder.getCreateTime() != null) {
            long minutes = Duration.between(workOrder.getCreateTime(), LocalDateTime.now()).toMinutes();
            workOrder.setResponseDuration(minutes);
        }
        
        return workOrderRepository.save(workOrder);
    }

    /**
     * 完成工单
     */
    @Transactional
    public WorkOrder completeOrder(Long orderId, String photoUrls, String signatureUrl) {
        WorkOrder workOrder = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        workOrder.setStatus("COMPLETED");
        workOrder.setCompleteTime(LocalDateTime.now());
        workOrder.setPhotoUrls(photoUrls);
        workOrder.setSignatureUrl(signatureUrl);
        
        if (workOrder.getAcceptTime() != null) {
            long minutes = Duration.between(workOrder.getAcceptTime(), LocalDateTime.now()).toMinutes();
            workOrder.setHandleDuration(minutes);
        }
        
        if (workOrder.getTechnicianId() != null) {
            Technician tech = technicianRepository.findById(workOrder.getTechnicianId()).orElse(null);
            if (tech != null && tech.getCurrentLoad() > 0) {
                tech.setCurrentLoad(tech.getCurrentLoad() - 1);
                technicianRepository.save(tech);
            }
        }
        
        return workOrderRepository.save(workOrder);
    }

    /**
     * 提交满意度评价
     */
    @Transactional
    public WorkOrder submitEvaluation(Long orderId, Integer score, String evaluation) {
        WorkOrder workOrder = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        workOrder.setSatisfactionScore(score);
        workOrder.setEvaluation(evaluation);
        
        if (workOrder.getTechnicianId() != null) {
            Technician tech = technicianRepository.findById(workOrder.getTechnicianId()).orElse(null);
            if (tech != null) {
                tech.setTotalOrders(tech.getTotalOrders() + 1);
                double newRating = (tech.getRating() * (tech.getTotalOrders() - 1) + score) / tech.getTotalOrders();
                tech.setRating(Math.round(newRating * 10.0) / 10.0);
                technicianRepository.save(tech);
            }
        }
        
        return workOrderRepository.save(workOrder);
    }

    /**
     * 获取所有工单
     */
    public List<WorkOrder> getAllOrders() {
        return workOrderRepository.findAll();
    }

    /**
     * 根据ID获取工单
     */
    public Optional<WorkOrder> getOrderById(Long id) {
        return workOrderRepository.findById(id);
    }

    /**
     * 更新工单
     */
    @Transactional
    public WorkOrder updateOrder(WorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    /**
     * 删除工单
     */
    @Transactional
    public void deleteOrder(Long id) {
        workOrderRepository.deleteById(id);
    }
}
