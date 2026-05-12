package com.hospital.service.material;

import com.hospital.entity.material.Material;
import com.hospital.repository.material.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public Material save(Material material) {
        return materialRepository.save(material);
    }

    public void deleteById(Long id) {
        materialRepository.deleteById(id);
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }

    public Optional<Material> findByMaterialCode(String materialCode) {
        return materialRepository.findByMaterialCode(materialCode);
    }

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    public Page<Material> findAll(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    public List<Material> findByStatus(Integer status) {
        return materialRepository.findByStatus(status);
    }

    public List<Material> findByCategory(String category) {
        return materialRepository.findByCategory(category);
    }

    public List<Material> findByIsHighValue(Boolean isHighValue) {
        return materialRepository.findByIsHighValue(isHighValue);
    }

    public boolean existsByMaterialCode(String materialCode) {
        return materialRepository.existsByMaterialCode(materialCode);
    }

    public Material update(Material material) {
        return materialRepository.save(material);
    }
}
