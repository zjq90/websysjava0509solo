package com.hospital.clinic.service;

import com.hospital.clinic.entity.Department;
import com.hospital.clinic.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 科室Service
 * 科室业务逻辑处理
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 查询所有科室
     */
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    /**
     * 根据ID查询科室
     */
    public Department findById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.orElse(null);
    }

    /**
     * 根据科室编码查询
     */
    public Department findByDeptCode(String deptCode) {
        return departmentRepository.findByDeptCode(deptCode);
    }

    /**
     * 根据科室名称模糊查询
     */
    public List<Department> findByName(String name) {
        return departmentRepository.findByDeptNameContaining(name);
    }

    /**
     * 新增科室
     */
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * 更新科室
     */
    public Department update(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * 删除科室
     */
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
