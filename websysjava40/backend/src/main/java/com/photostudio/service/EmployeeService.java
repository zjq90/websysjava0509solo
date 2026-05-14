package com.photostudio.service;

import com.photostudio.entity.Employee;
import com.photostudio.entity.Employee.EmployeePosition;
import com.photostudio.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 员工服务类
 * 提供员工管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * 获取所有员工
     * @return 员工列表
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * 获取所有在职员工
     * @return 在职员工列表
     */
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByActiveTrue();
    }

    /**
     * 根据ID获取员工
     * @param id 员工ID
     * @return 员工信息
     */
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * 根据工号获取员工
     * @param employeeNo 员工工号
     * @return 员工信息
     */
    public Optional<Employee> getEmployeeByNo(String employeeNo) {
        return employeeRepository.findByEmployeeNo(employeeNo);
    }

    /**
     * 根据职位获取员工
     * @param position 职位
     * @return 员工列表
     */
    public List<Employee> getEmployeesByPosition(EmployeePosition position) {
        return employeeRepository.findByPositionAndActiveTrue(position);
    }

    /**
     * 根据姓名搜索员工
     * @param name 姓名关键词
     * @return 员工列表
     */
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    /**
     * 创建员工
     * @param employee 员工信息
     * @return 创建的员工
     */
    @Transactional
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * 更新员工信息
     * @param id 员工ID
     * @param employee 员工信息
     * @return 更新后的员工
     */
    @Transactional
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        
        existing.setName(employee.getName());
        existing.setEmployeeNo(employee.getEmployeeNo());
        existing.setPosition(employee.getPosition());
        existing.setPhone(employee.getPhone());
        existing.setEmail(employee.getEmail());
        existing.setActive(employee.getActive());
        existing.setRemark(employee.getRemark());
        
        return employeeRepository.save(existing);
    }

    /**
     * 删除员工
     * @param id 员工ID
     */
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("员工不存在");
        }
        employeeRepository.deleteById(id);
    }

    /**
     * 禁用员工
     * @param id 员工ID
     * @return 更新后的员工
     */
    @Transactional
    public Employee deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        employee.setActive(false);
        return employeeRepository.save(employee);
    }
}
