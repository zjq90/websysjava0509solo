package com.hospital.service;

import com.hospital.entity.Department;
import com.hospital.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科室服务类
 * 处理科室信息管理业务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 获取所有启用的科室
     * 
     * @return 科室列表
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findByStatusOrderBySortOrderAsc(1);
    }

    /**
     * 获取一级科室
     * 
     * @return 一级科室列表
     */
    public List<Department> getParentDepartments() {
        return departmentRepository.findByParentIdAndStatusOrderBySortOrderAsc(0L, 1);
    }

    /**
     * 获取子科室
     * 
     * @param parentId 父科室ID
     * @return 子科室列表
     */
    public List<Department> getChildDepartments(Long parentId) {
        return departmentRepository.findByParentIdAndStatusOrderBySortOrderAsc(parentId, 1);
    }

    /**
     * 根据ID获取科室
     * 
     * @param id 科室ID
     * @return 科室信息
     */
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("科室不存在"));
    }
}
