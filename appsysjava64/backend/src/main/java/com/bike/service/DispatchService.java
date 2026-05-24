package com.bike.service;

import com.bike.entity.DispatchTask;
import com.bike.entity.HeatPoint;
import com.bike.repository.DispatchTaskRepository;
import com.bike.repository.HeatPointRepository;
import com.bike.repository.AreaRepository;
import com.bike.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 调度管理服务
 * 
 * @author bike-sharing
 */
@Service
public class DispatchService {

    @Autowired
    private DispatchTaskRepository dispatchTaskRepository;

    @Autowired
    private HeatPointRepository heatPointRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Cacheable(value = "dispatchTasks", key = "'all'")
    public List<DispatchTask> getAllTasks() {
        return dispatchTaskRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<DispatchTask> getTasksByStaffId(Long staffId) {
        return dispatchTaskRepository.findByStaffId(staffId);
    }

    public List<DispatchTask> getTasksByStatus(String status) {
        return dispatchTaskRepository.findByStatus(status);
    }

    public List<DispatchTask> getSortedTasksByPriority() {
        List<DispatchTask> tasks = dispatchTaskRepository.findByStatusIn(Arrays.asList("PENDING", "ACCEPTED"));
        tasks.sort((a, b) -> {
            Map<String, Integer> priorityMap = new HashMap<>();
            priorityMap.put("URGENT", 1);
            priorityMap.put("HIGH", 2);
            priorityMap.put("MEDIUM", 3);
            priorityMap.put("LOW", 4);
            
            int priorityCompare = priorityMap.getOrDefault(a.getPriority(), 5) 
                    - priorityMap.getOrDefault(b.getPriority(), 5);
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            return a.getCreateTime().compareTo(b.getCreateTime());
        });
        return tasks;
    }

    public DispatchTask getTaskById(Long id) {
        return dispatchTaskRepository.findById(id).orElse(null);
    }

    public DispatchTask getTaskByTaskNo(String taskNo) {
        return dispatchTaskRepository.findByTaskNo(taskNo).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "dispatchTasks", allEntries = true)
    public DispatchTask createDispatchTask(DispatchTask task) {
        String taskNo = "DT" + System.currentTimeMillis();
        task.setTaskNo(taskNo);
        if (task.getStatus() == null) {
            task.setStatus("PENDING");
        }
        if (task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }
        return dispatchTaskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = "dispatchTasks", allEntries = true)
    public DispatchTask updateDispatchTask(Long id, DispatchTask task) {
        DispatchTask existing = dispatchTaskRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (task.getStatus() != null) existing.setStatus(task.getStatus());
        if (task.getPriority() != null) existing.setPriority(task.getPriority());
        if (task.getStaffId() != null) existing.setStaffId(task.getStaffId());
        if (task.getStaffName() != null) existing.setStaffName(task.getStaffName());
        if (task.getDescription() != null) existing.setDescription(task.getDescription());
        return dispatchTaskRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "dispatchTasks", allEntries = true)
    public DispatchTask acceptTask(Long taskId, Long staffId, String staffName) {
        DispatchTask task = dispatchTaskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return null;
        }
        task.setStatus("ACCEPTED");
        task.setStaffId(staffId);
        task.setStaffName(staffName);
        task.setAcceptTime(LocalDateTime.now());
        return dispatchTaskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = "dispatchTasks", allEntries = true)
    public DispatchTask completeTask(Long taskId) {
        DispatchTask task = dispatchTaskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return null;
        }
        task.setStatus("COMPLETED");
        task.setCompleteTime(LocalDateTime.now());
        return dispatchTaskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = "dispatchTasks", allEntries = true)
    public void deleteTask(Long id) {
        dispatchTaskRepository.deleteById(id);
    }

    @Cacheable(value = "heatPoints", key = "'all'")
    public List<HeatPoint> getAllHeatPoints() {
        return heatPointRepository.findAll();
    }

    @Cacheable(value = "heatPoints", key = "#hours")
    public List<HeatPoint> getHeatPointsByTimeRange(int hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return heatPointRepository.findTopHeatPoints(startTime);
    }

    @Transactional
    public HeatPoint addHeatPoint(HeatPoint heatPoint) {
        if (heatPoint.getRecordTime() == null) {
            heatPoint.setRecordTime(LocalDateTime.now());
        }
        return heatPointRepository.save(heatPoint);
    }

    public Map<String, Object> generateDispatchSuggestions() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        areaRepository.findByDemandLevelGreaterThanEqual(7).forEach(highDemandArea -> {
            long bikeCount = bikeRepository.countByCurrentAreaId(highDemandArea.getId());
            if (bikeCount < 10) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("toAreaId", highDemandArea.getId());
                suggestion.put("toAreaName", highDemandArea.getAreaName());
                suggestion.put("currentBikes", bikeCount);
                suggestion.put("demandLevel", highDemandArea.getDemandLevel());
                suggestion.put("suggestBikes", 10 - bikeCount);
                
                areaRepository.findAll().forEach(fromArea -> {
                    if (!fromArea.getId().equals(highDemandArea.getId())) {
                        long fromBikeCount = bikeRepository.countByCurrentAreaId(fromArea.getId());
                        if (fromBikeCount > 20) {
                            suggestion.put("fromAreaId", fromArea.getId());
                            suggestion.put("fromAreaName", fromArea.getAreaName());
                            suggestion.put("fromAreaBikes", fromBikeCount);
                        }
                    }
                });
                suggestions.add(suggestion);
            }
        });
        
        result.put("suggestions", suggestions);
        result.put("totalSuggestions", suggestions.size());
        return result;
    }

    @Transactional
    @CacheEvict(value = "dispatchTasks", allEntries = true)
    public DispatchTask autoGenerateDispatchTask(Long fromAreaId, Long toAreaId, int bikeCount) {
        DispatchTask task = new DispatchTask();
        task.setTaskNo("DT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        task.setTaskType("DISPATCH");
        task.setPriority("MEDIUM");
        task.setStatus("PENDING");
        task.setFromAreaId(fromAreaId);
        task.setToAreaId(toAreaId);
        task.setBikeCount(bikeCount);
        task.setDescription("自动生成调度任务：从" + fromAreaId + "区域调度" + bikeCount + "辆车到" + toAreaId + "区域");
        task.setDeadline(LocalDateTime.now().plusHours(4));
        return dispatchTaskRepository.save(task);
    }
}
