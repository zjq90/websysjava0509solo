package com.photostudio.service;

import com.photostudio.entity.Venue;
import com.photostudio.entity.Venue.VenueStatus;
import com.photostudio.entity.Venue.VenueType;
import com.photostudio.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 场地服务类
 * 提供场地管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    /**
     * 获取所有场地
     * @return 场地列表
     */
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    /**
     * 获取可用场地
     * @return 场地列表
     */
    public List<Venue> getAvailableVenues() {
        return venueRepository.findByAvailableTrue();
    }

    /**
     * 根据ID获取场地
     * @param id 场地ID
     * @return 场地信息
     */
    public Optional<Venue> getVenueById(Long id) {
        return venueRepository.findById(id);
    }

    /**
     * 根据编号获取场地
     * @param venueNo 场地编号
     * @return 场地信息
     */
    public Venue getVenueByNo(String venueNo) {
        return venueRepository.findByVenueNo(venueNo);
    }

    /**
     * 根据类型获取场地
     * @param type 场地类型
     * @return 场地列表
     */
    public List<Venue> getVenuesByType(VenueType type) {
        return venueRepository.findByType(type);
    }

    /**
     * 根据状态获取场地
     * @param status 场地状态
     * @return 场地列表
     */
    public List<Venue> getVenuesByStatus(VenueStatus status) {
        return venueRepository.findByStatus(status);
    }

    /**
     * 根据名称搜索场地
     * @param name 名称关键词
     * @return 场地列表
     */
    public List<Venue> searchVenuesByName(String name) {
        return venueRepository.findByNameContaining(name);
    }

    /**
     * 创建场地
     * @param venue 场地信息
     * @return 创建的场地
     */
    @Transactional
    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    /**
     * 更新场地信息
     * @param id 场地ID
     * @param venue 场地信息
     * @return 更新后的场地
     */
    @Transactional
    public Venue updateVenue(Long id, Venue venue) {
        Venue existing = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("场地不存在"));
        
        existing.setName(venue.getName());
        existing.setVenueNo(venue.getVenueNo());
        existing.setType(venue.getType());
        existing.setAddress(venue.getAddress());
        existing.setCapacity(venue.getCapacity());
        existing.setDescription(venue.getDescription());
        existing.setAvailable(venue.getAvailable());
        existing.setStatus(venue.getStatus());
        
        return venueRepository.save(existing);
    }

    /**
     * 删除场地
     * @param id 场地ID
     */
    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("场地不存在");
        }
        venueRepository.deleteById(id);
    }

    /**
     * 设置场地维护状态
     * @param id 场地ID
     * @param maintenance 是否维护中
     * @return 更新后的场地
     */
    @Transactional
    public Venue setMaintenance(Long id, boolean maintenance) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("场地不存在"));
        venue.setStatus(maintenance ? VenueStatus.MAINTENANCE : VenueStatus.ACTIVE);
        venue.setAvailable(!maintenance);
        return venueRepository.save(venue);
    }
}
