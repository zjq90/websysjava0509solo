package com.ops.service;

import com.ops.entity.Technician;
import com.ops.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 装维人员服务类
 * 
 * @author ops-admin
 */
@Service
public class TechnicianService {

    @Autowired
    private TechnicianRepository technicianRepository;

    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    public Optional<Technician> getTechnicianById(Long id) {
        return technicianRepository.findById(id);
    }

    @Transactional
    public Technician createTechnician(Technician technician) {
        return technicianRepository.save(technician);
    }

    @Transactional
    public Technician updateTechnician(Technician technician) {
        return technicianRepository.save(technician);
    }

    @Transactional
    public void deleteTechnician(Long id) {
        technicianRepository.deleteById(id);
    }
}
