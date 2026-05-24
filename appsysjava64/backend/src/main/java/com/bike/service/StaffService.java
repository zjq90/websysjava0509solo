package com.bike.service;

import com.bike.entity.MaintenanceStaff;
import com.bike.repository.MaintenanceStaffRepository;
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
 * 运维人员服务
 * 
 * @author bike-sharing
 */
@Service
public class StaffService {

    @Autowired
    private MaintenanceStaffRepository staffRepository;

    @Cacheable(value = "staffs", key = "'all'")
    public List<MaintenanceStaff> getAllStaffs() {
        return staffRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    public List<MaintenanceStaff> getStaffsByStatus(String status) {
        return staffRepository.findByStatus(status);
    }

    public List<MaintenanceStaff> getStaffsByWorkArea(String workArea) {
        return staffRepository.findByWorkArea(workArea);
    }

    public MaintenanceStaff getStaffById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    public MaintenanceStaff getStaffByStaffNo(String staffNo) {
        return staffRepository.findByStaffNo(staffNo).orElse(null);
    }

    public MaintenanceStaff getStaffByPhone(String phone) {
        return staffRepository.findByPhone(phone).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "staffs", allEntries = true)
    public MaintenanceStaff createStaff(MaintenanceStaff staff) {
        String staffNo = "STF" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        staff.setStaffNo(staffNo);
        if (staff.getStatus() == null) {
            staff.setStatus("ONLINE");
        }
        if (staff.getCompletedTasks() == null) {
            staff.setCompletedTasks(0);
        }
        if (staff.getRating() == null) {
            staff.setRating(5.0f);
        }
        return staffRepository.save(staff);
    }

    @Transactional
    @CacheEvict(value = "staffs", allEntries = true)
    public MaintenanceStaff updateStaff(Long id, MaintenanceStaff staff) {
        MaintenanceStaff existing = staffRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        if (staff.getName() != null) existing.setName(staff.getName());
        if (staff.getPhone() != null) existing.setPhone(staff.getPhone());
        if (staff.getAvatar() != null) existing.setAvatar(staff.getAvatar());
        if (staff.getWorkArea() != null) existing.setWorkArea(staff.getWorkArea());
        if (staff.getStatus() != null) existing.setStatus(staff.getStatus());
        if (staff.getLongitude() != null) existing.setLongitude(staff.getLongitude());
        if (staff.getLatitude() != null) existing.setLatitude(staff.getLatitude());
        return staffRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "staffs", allEntries = true)
    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    public MaintenanceStaff login(String phone, String password) {
        MaintenanceStaff staff = staffRepository.findByPhone(phone).orElse(null);
        if (staff != null && staff.getPassword() != null && staff.getPassword().equals(password)) {
            return staff;
        }
        return null;
    }
}
