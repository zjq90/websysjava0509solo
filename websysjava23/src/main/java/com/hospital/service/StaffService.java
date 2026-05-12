package com.hospital.service;

import com.hospital.entity.Staff;
import com.hospital.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 医护人员Service
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    public Staff findByStaffNo(String staffNo) {
        return staffRepository.findByStaffNo(staffNo);
    }

    public List<Staff> findByRole(String role) {
        return staffRepository.findByRole(role);
    }

    public List<Staff> findAllDoctors() {
        return staffRepository.findAllDoctors();
    }

    public List<Staff> findAllNurses() {
        return staffRepository.findAllNurses();
    }

    public List<Staff> findByDepartment(String department) {
        return staffRepository.findByDepartment(department);
    }

    @Transactional
    public Staff save(Staff staff) {
        if (staff.getId() == null) {
            String staffNo = "S" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            staff.setStaffNo(staffNo);
        }
        return staffRepository.save(staff);
    }

    @Transactional
    public void deleteById(Long id) {
        staffRepository.deleteById(id);
    }
}
