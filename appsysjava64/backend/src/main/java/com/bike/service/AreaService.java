package com.bike.service;

import com.bike.entity.Area;
import com.bike.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 区域管理服务
 * 
 * @author bike-sharing
 */
@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Cacheable(value = "areas", key = "'all'")
    public List<Area> getAllAreas() {
        return areaRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<Area> getAreasByType(String areaType) {
        return areaRepository.findByAreaType(areaType);
    }

    public List<Area> getHighDemandAreas(Integer minLevel) {
        return areaRepository.findByDemandLevelGreaterThanEqual(minLevel);
    }

    public Area getAreaById(Long id) {
        return areaRepository.findById(id).orElse(null);
    }

    public Area getAreaByCode(String areaCode) {
        return areaRepository.findByAreaCode(areaCode).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "areas", allEntries = true)
    public Area createArea(Area area) {
        String areaCode = "AR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        area.setAreaCode(areaCode);
        if (area.getBikeCount() == null) {
            area.setBikeCount(0);
        }
        if (area.getDemandLevel() == null) {
            area.setDemandLevel(5);
        }
        return areaRepository.save(area);
    }

    @Transactional
    @CacheEvict(value = "areas", allEntries = true)
    public Area updateArea(Long id, Area area) {
        Area existing = areaRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (area.getAreaName() != null) existing.setAreaName(area.getAreaName());
        if (area.getCenterLongitude() != null) existing.setCenterLongitude(area.getCenterLongitude());
        if (area.getCenterLatitude() != null) existing.setCenterLatitude(area.getCenterLatitude());
        if (area.getBikeCount() != null) existing.setBikeCount(area.getBikeCount());
        if (area.getDemandLevel() != null) existing.setDemandLevel(area.getDemandLevel());
        if (area.getAreaType() != null) existing.setAreaType(area.getAreaType());
        return areaRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "areas", allEntries = true)
    public void deleteArea(Long id) {
        areaRepository.deleteById(id);
    }
}
