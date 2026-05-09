package com.breeding.service;

import com.breeding.entity.ParentPlant;
import com.breeding.repository.ParentPlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 亲本植物业务逻辑服务类
 */
@Service
@Transactional
public class ParentPlantService {

    @Autowired
    private ParentPlantRepository parentRepository;

    public List<ParentPlant> findAll() {
        return parentRepository.findAll();
    }

    public Page<ParentPlant> findAll(Pageable pageable) {
        return parentRepository.findAll(pageable);
    }

    public Optional<ParentPlant> findById(Long id) {
        return parentRepository.findById(id);
    }

    public ParentPlant findByParentCode(String parentCode) {
        return parentRepository.findByParentCode(parentCode);
    }

    public ParentPlant save(ParentPlant parent) {
        return parentRepository.save(parent);
    }

    public ParentPlant update(Long id, ParentPlant parentDetails) {
        Optional<ParentPlant> optional = parentRepository.findById(id);
        if (optional.isPresent()) {
            ParentPlant parent = optional.get();
            parent.setParentCode(parentDetails.getParentCode());
            parent.setVarietyName(parentDetails.getVarietyName());
            parent.setParentType(parentDetails.getParentType());
            parent.setOrigin(parentDetails.getOrigin());
            parent.setCropType(parentDetails.getCropType());
            parent.setGeneration(parentDetails.getGeneration());
            parent.setTraits(parentDetails.getTraits());
            parent.setDiseaseResistance(parentDetails.getDiseaseResistance());
            parent.setStressTolerance(parentDetails.getStressTolerance());
            parent.setYieldPotential(parentDetails.getYieldPotential());
            parent.setGrowthPeriod(parentDetails.getGrowthPeriod());
            parent.setCollectionDate(parentDetails.getCollectionDate());
            parent.setStorageLocation(parentDetails.getStorageLocation());
            parent.setRemarks(parentDetails.getRemarks());
            return parentRepository.save(parent);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (parentRepository.existsById(id)) {
            parentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ParentPlant> findByProjectId(Long projectId) {
        return parentRepository.findByProjectId(projectId);
    }

    public List<ParentPlant> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return parentRepository.findAll();
        }
        return parentRepository.searchByKeyword(keyword);
    }
}
