package com.lims.service;

import com.lims.entity.Department;
import com.lims.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 科室业务逻辑层
 *
 * @author LIMS Team
 * @version 1.0.0
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
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    /**
     * 根据科室编号查询
     */
    public Department findByDeptCode(String deptCode) {
        return departmentRepository.findByDeptCode(deptCode);
    }

    /**
     * 根据科室类型查询
     */
    public List<Department> findByDeptType(String deptType) {
        return departmentRepository.findByDeptType(deptType);
    }

    /**
     * 根据状态查询
     */
    public List<Department> findByStatus(String status) {
        return departmentRepository.findByStatus(status);
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
