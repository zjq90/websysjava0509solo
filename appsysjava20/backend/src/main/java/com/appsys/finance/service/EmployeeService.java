package com.appsys.finance.service;

import com.appsys.finance.entity.Employee;
import com.appsys.finance.repository.EmployeeRepository;
import com.appsys.finance.util.AesEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AesEncryptUtil aesEncryptUtil;

    public Employee createEmployee(Employee employee) {
        if (employee.getPhone() != null && !employee.getPhone().isEmpty()) {
            employee.setPhone(aesEncryptUtil.encrypt(employee.getPhone()));
        }
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        employeeOpt.ifPresent(this::decryptEmployee);
        return employeeOpt;
    }

    public Optional<Employee> getEmployeeByEmployeeNo(String employeeNo) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeNo(employeeNo);
        employeeOpt.ifPresent(this::decryptEmployee);
        return employeeOpt;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll().stream()
            .peek(this::decryptEmployee)
            .collect(Collectors.toList());
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setName(employeeDetails.getName());
            employee.setEmployeeNo(employeeDetails.getEmployeeNo());
            if (employeeDetails.getPhone() != null && !employeeDetails.getPhone().isEmpty()) {
                employee.setPhone(aesEncryptUtil.encrypt(employeeDetails.getPhone()));
            }
            employee.setDepartment(employeeDetails.getDepartment());
            employee.setPosition(employeeDetails.getPosition());
            employee.setCommissionRate(employeeDetails.getCommissionRate());
            employee.setIsManager(employeeDetails.getIsManager());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("员工不存在，ID: " + id));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    private void decryptEmployee(Employee employee) {
        if (employee.getPhone() != null) {
            employee.setPhone(aesEncryptUtil.decrypt(employee.getPhone()));
        }
    }
}
